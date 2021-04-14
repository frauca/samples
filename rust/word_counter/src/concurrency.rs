use std::sync::Arc;

use log::{debug, info, trace};
use tokio::sync::Semaphore;
use tokio::task::JoinHandle;

use crate::{Book, gutenberg};
use crate::gutenberg::Error;
use crate::persistance::Database;

static PARALEL_GUTENBERG_FETCH: usize = 100;

async fn process(book: Book) -> Result<Book, Error> {
    debug!("Fetching the book content {}", book.id);
    let book_content = gutenberg::book_content(&book).await?;
    debug!("book {} have  chars{}", book.id, book_content.len());
    Ok(book.clone())
}

pub struct Async {
    database: Database,
}

impl Async {
    pub fn new() -> Self {
        info!("Connecting to database");
        Async {
            database: Database::new()
        }
    }

    async fn async_read_catalog(&self, catalog_path: String) -> Result<Vec<Book>, Error> {
        let catalog = gutenberg::read_catalog(catalog_path)?;
        let mut futures: Vec<JoinHandle<Result<Book, Error>>> = vec![];
        let mut books: Vec<Book> = vec![];
        let sem = Arc::new(Semaphore::new(PARALEL_GUTENBERG_FETCH));
        for book in catalog.iter() {
            let book = book.clone();
            books.push(book.clone());
            let sem_clone = Arc::clone(&sem);
            let future: JoinHandle<Result<Book, Error>> = tokio::spawn(
                async move {
                    let book_id = book.id;
                    debug!("Before semaphre on book {}", book_id);
                    let aq = sem_clone.acquire().await;
                    trace!("semaphore {:?}", aq);
                    let result = process(book).await;
                    debug!("After semaphre on book {}", book_id);
                    info!("Procesed book {} content", book_id);
                    return result;
                }
            );
            futures.push(future);
        }
        futures::future::join_all(futures).await;
        Ok(books)
    }

    pub fn read_catalog(&self, catlog_path: String) -> Result<Vec<Book>, Error> {
        let mut rt = tokio::runtime::Runtime::new().unwrap();
        let mut books = rt.block_on(self.async_read_catalog(catlog_path))?;
        info!("all books {} has been readed", books.len());

        for book in books.iter_mut() {
            let book = book.clone();
            debug!("saving book {} has been saved", book.id);
            self.database.save_book(&book);
        }
        info!("all books {} has been saved", books.len());
        Ok(books)
    }
}

