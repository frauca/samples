use crate::gutenberg::Error;

#[macro_use]
extern crate diesel;
extern crate dotenv;

mod book;
pub mod gutenberg;
pub mod schema;
mod persistance;

pub use crate::book::{Book,State};

pub fn read_catalog(catlog_path: String) -> Result<Vec<Book>, Error> {
    return gutenberg::read_catalog(catlog_path);
}

pub fn save(book: &Book) {
    persistance::save(book);
}