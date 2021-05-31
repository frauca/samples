use std::env;

use diesel::{Connection, PgConnection};
use dotenv::dotenv;
use log::{debug, error, info, trace};

use crate::book::Book;
use crate::persistance::error::Error;
use crate::persistance::models::NewBook;

mod error;
mod models;
mod dao;

pub struct Database {
    connection: PgConnection,
}

impl Database {
    pub fn new() -> Self {
        let connection = establish_connection();
        Database {
            connection: connection
        }
    }

    pub fn save_book(&self, book: &Book) -> Result<(), Error> {
        let new_book = NewBook::from(book).expect("temprary implementation");
        let words = dao::save_words(&self.connection, &book.words, &book.language);
        let saved_book = dao::save(&self.connection, &new_book);
        dao::relate_word_book(&self.connection, &saved_book?, words?);
        Ok(())
    }
}

fn establish_connection() -> PgConnection {
    dotenv().ok();

    let database_url = env::var("DATABASE_URL")
        .expect("DATABASE_URL must be set");
    PgConnection::establish(&database_url)
        .expect(&format!("Error connecting to {}", database_url))
}