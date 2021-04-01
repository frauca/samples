use crate::book::Book;
pub use crate::gutenberg::error::Error;

mod reader;
mod error;


pub(super) fn read_catalog(catalog_path: String) -> Result<Vec<Book>, Error> {
    return reader::read_catalog(catalog_path);
}
