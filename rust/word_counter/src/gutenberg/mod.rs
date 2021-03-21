use crate::book::Book;
pub use crate::gutenberg::error::Error;

mod reader;
mod error;


pub(super) fn readCatalog(catalogPath: String) -> Result<Vec<Book>, Error> {
    return reader::readCatalog(catalogPath);
}
