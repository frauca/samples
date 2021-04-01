use word_counter;
use simple_logger::SimpleLogger;
use log::LevelFilter;

fn main() {
    SimpleLogger::new().with_level(LevelFilter::Debug).init().unwrap();
    let books = word_counter::read_catalog(String::from("./resources/main/catalog.rdf")).unwrap();
    log::info!("catalog readed {:?}",books.len());

}
