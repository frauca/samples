use crate::book::Book;
pub use crate::gutenberg::error::Error;

mod catalog;
mod error;
mod parser;
mod fetcher;


pub(super) fn read_catalog(catalog_path: String) -> Result<Vec<Book>, Error> {
    return catalog::read_catalog(catalog_path);
}

pub(super) async fn book_content(book: &Book) -> Result<String, Error> {
    let raw_content = fetcher::download_content(book).await?;
    Ok(String::from(parser::parse(&raw_content)?))
}