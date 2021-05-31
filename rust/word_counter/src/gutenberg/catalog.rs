use std::fs;
use std::num::ParseIntError;

use isolang::Language;

use crate::book::{Book};
use crate::gutenberg::error::Error;

static TEXT_TAG: &str = "etext";
static ATT_ID: &str = "rdf:ID";
static DC_NAMESPACE: &str = "http://purl.org/dc/elements/1.1/";
static FIELD_TITLE: &str = "title";
static FIELD_LANGUAGE: &str = "language";
static FIELD_VALUE: &str = "value";
static DEFAULT_LANGUAGE: Language = Language::Eng;

pub(super) fn read_catalog(catalog_path: String) -> Result<Vec<Book>, Error> {
    log::debug!("Reading catalog from {}", catalog_path);
    let root: minidom::Element = read_dom(catalog_path)?;

    read_from_dom(root)
}

fn read_dom(catalog_path: String) -> Result<minidom::Element, Error> {
    let contents = fs::read_to_string(catalog_path)?;
    log::debug!("Parsing catlog from dom");
    Ok(contents.parse()?)
}

fn read_from_dom(root: minidom::Element) -> Result<Vec<Book>, Error> {
    log::debug!("Converting dom to books");
    root.nodes()
        .filter_map(|node| match node {
            minidom::Node::Element(element) => Some(element),
            _ => None
        }).filter_map(book_from)
        .collect()
}

fn book_from(posible_book: &minidom::Element) -> Option<Result<Book, Error>> {
    if posible_book.name() != TEXT_TAG {
        return None;
    }
    if let Some(error) = get_format_error(posible_book) {
        log::error!("Could not read element {:?} the error is {:?}",posible_book.attr(ATT_ID),error);
        return None;
    }
    let id = id_from(posible_book.attr(ATT_ID).expect("id already verified")).unwrap();
    let title = get_field(posible_book, FIELD_TITLE).expect("title already verified");
    let language = get_language(posible_book);
    Some(Ok(Book::new(id,title,language)))
}

fn get_language(posible_book: &minidom::Element) -> Language {
    posible_book.get_child(FIELD_LANGUAGE, DC_NAMESPACE)
        .map(|language_field| all_childs_recursive(language_field).into_iter()
            .filter(|child| child.name() == FIELD_VALUE)
            .next())
        .flatten()
        .map( |language_value|{Language::from_639_1(language_value.text().as_str())})
        .flatten()
        .unwrap_or(DEFAULT_LANGUAGE)
}

fn get_format_error(posible_book: &minidom::Element) -> Option<Error> {
    if posible_book.attr(ATT_ID).is_none() {
        return Some(Error::Format(format!("Id attribute is needed")));
    } else if get_field(posible_book, FIELD_TITLE).is_none() {
        return Some(Error::Format(format!("Title is mandatory")));
    }
    None
}

fn get_field(posible_book: &minidom::Element, field: &str) -> Option<String> {
    let field_element = posible_book.get_child(field, DC_NAMESPACE);
    Some(field_element?.text())
}

fn all_childs_recursive(element: &minidom::Element) -> Vec<&minidom::Element> {
    let mut all_childs = vec![];
    for child in element.children() {
        all_childs.push(child);
        all_childs.append(&mut all_childs_recursive(child));
    }
    all_childs
}

/// # Examples
///
/// ``
/// assert_eq(ìdFrom("etext2489"), Ok(2489));
/// ```
/// ```
///assert_eq(ìdFrom("etext2489"), Ok(2489));
///```
fn id_from(etext_id: &str) -> Result<i32, ParseIntError> {
    etext_id[5..].parse()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn read_moby_dick() {
        let moby_dick_path = String::from("resources/test/moby_dick.xml");
        let books = read_catalog(moby_dick_path).unwrap();

        assert_eq!(books.len(), 1);
        let moby_dick = books.get(0).unwrap();
        assert_eq!(moby_dick.title, "Moby Dick; Or, The Whale");
        assert!(moby_dick.language == Language::Eng)
    }
}