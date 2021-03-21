use crate::book::Book;
use crate::gutenberg::Error;

mod book;
pub mod gutenberg;


pub fn readCalog(catalogPath: String) -> Result<Vec<Book>, Error> {
    return gutenberg::readCatalog(catalogPath);
}
