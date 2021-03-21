use std::fs;

use crate::book::Book;
use crate::gutenberg::error::Error;

pub(super) fn readCatalog(catalogPath: String) -> Result<Vec<Book>,Error> {
    log::debug!("Reading catalog from {}", catalogPath);
    let root: minidom::Element = readDom(catalogPath)?;

    Ok(readFromDom(root))
}

fn readFromDom(root: minidom::Element) -> Vec<Book> {
    let books: Vec<Book> = vec![];
    return books;
}

fn readDom(catalogPath: String) -> Result<minidom::Element, Error> {
    let contents = fs::read_to_string(catalogPath)?;

    Ok(contents.parse()?)
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn read_moby_dick() {
        let moby_dick_path = String::from("resources/test/moby_dick.xml");
        let books = readCatalog(moby_dick_path).unwrap();

        assert_eq!(books.len(), 1);
        let moby_dick = books.get(1).unwrap();
        assert_eq!(moby_dick.title, "Moby Dick; Or, The Whale");
    }
}