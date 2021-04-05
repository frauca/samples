use crate::book::Book;
pub use crate::gutenberg::error::Error;

mod catalog;
mod error;
mod parser;


pub(super) fn read_catalog(catalog_path: String) -> Result<Vec<Book>, Error> {
    return catalog::read_catalog(catalog_path);
}
