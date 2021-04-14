#[macro_use]
extern crate diesel;
extern crate dotenv;

use log::info;

use crate::concurrency::Async;
pub use crate::book::{Book, State};
use crate::gutenberg::Error;
use crate::persistance::Database;

mod book;
pub mod gutenberg;
pub mod schema;
mod persistance;
mod config;
mod concurrency;

pub struct WordCounter {
    background: Async,
}

impl WordCounter {
    pub fn new() -> Self {
        config::init_logger();
        info!("logger started");
        WordCounter {
            background: Async::new()
        }
    }

    pub fn read_catalog(&self, catlog_path: String) -> Result<Vec<Book>, Error> {
        return self.background.read_catalog(catlog_path);
    }
}

