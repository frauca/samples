use log::debug;

use crate::Book;
use crate::gutenberg::Error;

pub async fn download_content(book: &Book) -> Result<String, Error> {
    let urls = book_url(book);
    for url in urls {
        debug!("Get the url {:?}", url);
        let response = reqwest::get(url.clone()).await?;
        if response.status().is_success() {
            let book_content = response.text().await?;
            debug!("url {:?} downloaded the book ", url);
            return Ok(book_content);
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
    use crate::{Book};
    use crate::gutenberg::fetcher::book_url;

    #[test]
    fn url() {
        let id = 2489;
        let title = String::from("moby dick");
        let language = isolang::Language::Cat;
        let book = Book::new(id,title,language);

        assert_eq!(book_url(&book)[0], "http://gutenberg.org/files/2489/2489.txt");
        assert_eq!(book_url(&book)[1], "http://gutenberg.org/files/2489/2489-0.txt");
        assert_eq!(book_url(&book)[2], "http://gutenberg.org/files/2489/2489-1.txt");
    }
}