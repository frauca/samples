use word_counter;
use log::LevelFilter;
use word_counter::WordCounter;

fn main() {
    let counter = WordCounter::new();
    let books = counter.read_catalog(String::from("./resources/main/catalog.rdf")).unwrap();
    log::info!("catalog readed {:?}",books.len());

}
