use word_counter;
use simple_logger::SimpleLogger;
use log::LevelFilter;
use word_counter::WordCounter;

fn main() {
    SimpleLogger::new().with_level(LevelFilter::Debug).init().unwrap();
    let counter = WordCounter::new();
    let books = counter.read_catalog(String::from("./resources/main/catalog.rdf")).unwrap();
    log::info!("catalog readed {:?}",books.len());

}
