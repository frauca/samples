use log::debug;

use crate::Book;
use crate::gutenberg::Error;

pub async fn download_content(book: &Book) -> Result<String, Error> {
    let urls = book_url(book);
    for url in urls {
        debug!("Get the url {:?}", url);
        let response = reqwest::get(url.clone()).await?;
        if response.status().is_success() {
            return Ok(response.text().await?);
        }
        debug!("url {:?} has returned {}", url, response.status());
    }
    Err(Error::NoUrlFound(format!("could not found url for book {}", book.id)))
}

fn book_url(book: &Book) -> Vec<String> {
    let mut urls = vec![format!("http://gutenberg.org/files/{id}/{id}.txt", id = book.id)];
    for i in 0..10 {
        urls.push(format!("http://gutenberg.org/files/{id}/{id}-{i}.txt", id = book.id, i = i))
    }
    urls
}

#[cfg(test)]
mod tests {
    use crate::{Book, State};
    use crate::gutenberg::fetcher::book_url;

    #[test]
    fn url() {
        let book = Book {
            id: 2489,
            title: String::from("moby dick"),
            language: isolang::Language::Cat,
            state: State::NEW,
        };

        assert_eq!(book_url(&book)[0], "http://gutenberg.org/files/2489/2489-0.txt")
    }
}