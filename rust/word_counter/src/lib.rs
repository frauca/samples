#[macro_use]
extern crate diesel;
extern crate dotenv;

use log::info;

use crate::concurrency::Concurrent;
pub use crate::book::{Book, State};
use crate::gutenberg::Error;

mod book;
pub mod gutenberg;
pub mod schema;
mod persistance;
mod config;
mod concurrency;

pub struct WordCounter {
    background: Concurrent,
}

impl WordCounter {
    pub fn new() -> Self {
        config::init_logger();
        info!("logger started");
        WordCounter {
            background: Concurrent::new()
        }
    }

    pub fn read_catalog(&self, catlog_path: String) -> Result<Vec<Book>, Error> {
        return self.background.read_catalog(catlog_path);
    }
}

