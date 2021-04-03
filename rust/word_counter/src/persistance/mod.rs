use diesel::{PgConnection, Connection, RunQueryDsl};
use std::env;
use crate::book::Book;
use crate::persistance::models::NewBook;
use crate::schema::books;
use dotenv::dotenv;
use crate::schema::books::columns::id;

mod error;
mod models;

pub fn save(book: &Book){
    let connection = establish_connection();
    log::debug!("Saving book {}", book.id);

    let new_book = NewBook::from(book).expect("temprary implementation");

    diesel::insert_into(books::table)
        .values(&new_book)
        .on_conflict(id)
        .do_update()
        .set(&new_book)
        .execute(&connection)
        .expect("Could not save book");
}

fn establish_connection() -> PgConnection {
    dotenv().ok();

    let database_url = env::var("DATABASE_URL")
        .expect("DATABASE_URL must be set");
    PgConnection::establish(&database_url)
        .expect(&format!("Error connecting to {}", database_url))
}