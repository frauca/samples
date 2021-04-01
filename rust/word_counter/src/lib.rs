use crate::book::Book;
use crate::gutenberg::Error;

mod book;
pub mod gutenberg;


pub fn read_catalog(catlog_path: String) -> Result<Vec<Book>, Error> {
    return gutenberg::read_catalog(catlog_path);
}
