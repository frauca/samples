use std::sync::Arc;

use log::{debug, error, info, trace};
use tokio::sync::{mpsc, Semaphore};
use tokio::sync::mpsc::{Receiver, Sender};
use tokio::task::JoinHandle;
use crate::counter;

use crate::{Book,Word, gutenberg};
use crate::gutenberg::Error;
use crate::persistance::Database;

static PARALEL_GUTENBERG_FETCH: usize = 100;


async fn add_words(book: &mut Book, content: String) {
        let mut words: Vec<Word> = vec![];
        for(word,times) in counter::count(&content[..]) {
            words.push(Word::new(-1,word, times));
        }
        book.words=words;
}

async fn process_book(book: &mut Book) {
    debug!("Fetching the book content {}", book.id);
    let book_content = {
        match gutenberg::book_content(book).await {
            Ok(content) => content,
            Err(error) => {
                error!("Could not get book {}. Reason {:?}", book.id, error);
                String::from("")
            }
        }
    };
    debug!("book {} have  chars{}", book.id, book_content.len());
    add_words(book,book_content).await;
}

async fn fetch_all_books(books: &Vec<Book>, sender: Sender<Book>) {
    let sem = Arc::new(Semaphore::new(PARALEL_GUTENBERG_FETCH));
    let mut futures: Vec<JoinHandle<()>> = vec![];

    for book in books.iter() {
        let mut book = book.clone();
        let sender = sender.clone();
        let sem_clone = Arc::clone(&sem);
        let future: JoinHandle<()> = tokio::spawn(
            async move {
                let book_id = book.id;
                debug!("Before semaphre on book {}", book_id);
                let aq = sem_clone.acquire().await;
                trace!("semaphore {:?}", aq);
                process_book(&mut book).await;
                debug!("After semaphre on book {}", book_id);
                if let Err(error) = sender.send(book).await {
                    error!("Could not send to save {}. Reason {:?}", book_id, error);
                }
                debug!("Book sended to save {}", book_id);
                ()
            }
        );
        futures.push(future);
    }
    debug!("All books has beeb fetched");
}

async fn save_received_books(db: &Database, mut receiver: Receiver<Book>) -> Vec<Book> {
    let mut books = vec![];
    debug!("Going to receive {:?}", receiver);
    while let Some(book) = receiver.recv().await {
        debug!("Saving book {}", book.id);
        if let Err(err) = db.save_book(&book) {
            error!("could not save book {:?}", err)
        } else {
            books.push(book);
        }
        if books.len() % 100 == 0 {
            info!("{} books procesed", books.len());
        }
    }
    books
}

async fn async_read_catalog(db: &Database, catalog_path: String) -> Result<Vec<Book>, Error> {
    let catalog = gutenberg::read_catalog(catalog_path)?;
    info!("Catalog has been readed. {} books will be procesed", catalog.len());

    let (tx, rx): (Sender<Book>, Receiver<Book>) = mpsc::channel(32);
    fetch_all_books(&catalog, tx).await;

    let books = save_received_books(db, rx).await;
    info!("End processing an saving {} books", books.len());
    Ok(books)
}

pub struct Concurrent {
    database: Database,
}

impl Concurrent {
    pub fn new() -> Self {
        info!("Connecting to database");
        Concurrent {
            database: Database::new()
        }
    }

    pub fn read_catalog(&self, catlog_path: String) -> Result<Vec<Book>, Error> {
        let rt = tokio::runtime::Runtime::new().unwrap();
        let books = rt.block_on(async_read_catalog(&self.database, catlog_path))?;
        info!("all books {} has been saved", books.len());
        Ok(books)
    }

    pub fn save(&self, book: &Book) {
        let rt = tokio::runtime::Runtime::new().unwrap();
        let (tx, rx): (Sender<Book>, Receiver<Book>) = mpsc::channel(32);

        let books = rt.block_on(async move {
            let cloned_book = book.clone();
            tokio::spawn(async move {
                tx.send(cloned_book).await;
            });

            let books: Vec<Book> = save_received_books(&self.database, rx).await;
            books
        });

        info!(" books {} has been saved", books.len());
    }
}
