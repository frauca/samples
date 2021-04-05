use std::env;

use diesel::{PgConnection, Connection};
use dotenv::dotenv;

use crate::book::Book;
use crate::persistance::dao::save;
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

    pub fn save_book(&self, book: &Book) {
        let new_book = NewBook::from(book).expect("temprary implementation");
        save(&self.connection, &new_book);
    }
}

fn establish_connection() -> PgConnection {
    dotenv().ok();

    let database_url = env::var("DATABASE_URL")
        .expect("DATABASE_URL must be set");
    PgConnection::establish(&database_url)
        .expect(&format!("Error connecting to {}", database_url))
}