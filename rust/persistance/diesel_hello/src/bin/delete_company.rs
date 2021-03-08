extern crate diesel_hello;
extern crate diesel;

use std::env::args;

use crate::diesel::query_dsl::filter_dsl::FindDsl;
use diesel_hello::establish_connection;
use diesel::RunQueryDsl;

fn main() {
    use diesel_hello::schema::companies::dsl::{companies};
    
    let id = args().nth(1).expect("I need you tell me what do you want to delete")
        .parse::<i32>().expect("Invalid ID");
    let connection = establish_connection();

    let num_deleted = diesel::delete(companies.find(id))
        .execute(&connection)
        .expect(format!("Could not delete {}", id).as_ref());
    println!("I have deleted {}", num_deleted)
}