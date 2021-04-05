use diesel::{PgConnection, RunQueryDsl};

use crate::persistance::models::NewBook;
use crate::schema::books;
use crate::schema::books::columns::id;

pub fn save(connection: &PgConnection, book: &NewBook) {
    diesel::insert_into(books::table)
        .values(book)
        .on_conflict(id)
        .do_update()
        .set(book)
        .execute(connection)
        .expect("Could not save book");
}