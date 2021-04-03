use word_counter;
use simple_logger::SimpleLogger;
use log::LevelFilter;
use crate::word_counter::{Book,State};

fn main() {
    SimpleLogger::new().with_level(LevelFilter::Debug).init().unwrap();
    let book = Book{
        id: 37,
        title: String::from("sample title."),
        language: isolang::Language::Cat,
        state: State::NEW
    };
    word_counter::save(&book);
    log::info!("book saved");

}
