pub mod schema;
pub mod models;

#[macro_use]
extern crate diesel;
extern crate dotenv;

use diesel::prelude::*;
use diesel::pg::PgConnection;
use dotenv::dotenv;
use std::env;
use crate::models::{Company, NewCompany};

pub fn establish_connection() -> PgConnection {
    dotenv().ok();

    let database_url = env::var("DATABASE_URL")
        .expect("DATABASE_URL must be set");
    PgConnection::establish(&database_url)
        .expect(&format!("Error connecting to {}", database_url))
}

pub fn create_company<'a>(conn: &PgConnection, name: &'a str, purpose: Option<&'a str>, philosofy: Option<&'a str>) -> Company {
    use schema::companies;

    let new_post = NewCompany {
        name: name,
        purpose: purpose,
        philosofy: philosofy,
    };

    diesel::insert_into(companies::table)
        .values(&new_post)
        .get_result(conn)
        .expect("Error saving new post")
}