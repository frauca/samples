use word_counter;
use word_counter::{WordCounter, Book, Word};

fn main() {
    let counter = WordCounter::new();
    let books = counter.read_catalog(String::from("./resources/main/catalog.rdf")).unwrap();
    /*let books: Vec<Book> = vec![];
    let mut book = Book::new(1,String::from("test 1"),isolang::Language::Cat);
    book.words = vec![Word::new(-1,String::from("hola"),1)];
    counter.test_save(&book);*/
    log::info!("catalog readed {:?}",books.len());

}
