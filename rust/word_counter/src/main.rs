use std::fs;
use word_counter;
use simple_logger::SimpleLogger;
use log::LevelFilter;

fn main() {
    SimpleLogger::new().with_level(LevelFilter::Debug).init().unwrap();
    let filename = "./resources/main/catalog.rdf";
    println!("In file {}", filename);
    word_counter::readCalog(String::from("resources/test/moby_dick.xml")).unwrap();
    let contents = fs::read_to_string(filename)
        .expect("Something went wrong reading the file");

    let root: minidom::Element = contents.parse().unwrap();
    println!("{:#?}", root);
}
