use crate::book::Book;
use log::debug;
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
    debug!("parsing book size {}", raw_content.len());
    Ok(String::from(parser::parse(&raw_content).or::<&str>(Ok(&raw_content[..])).unwrap()))
}

