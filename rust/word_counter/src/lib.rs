#[macro_use]
extern crate diesel;
extern crate dotenv;

pub use crate::book::{Book, State};
use crate::gutenberg::Error;
use crate::persistance::Database;


mod book;
pub mod gutenberg;
pub mod schema;
mod persistance;

pub struct WordCounter {
    database: Database,
}

impl WordCounter {
    pub fn new() -> Self {
        WordCounter{
            database: Database::new()
        }
    }

    pub fn read_catalog(&self, catlog_path: String) -> Result<Vec<Book>, Error> {
        let readed_books = gutenberg::read_catalog(catlog_path);
        log::info!("Saving books");
        let mut books: Vec<Book> = vec![];
        for book in readed_books?.iter() {
            self.database.save_book(book);
            books.push(book.to_owned())
        }
        log::info!("books saved");
        Ok(books)
    }
}

