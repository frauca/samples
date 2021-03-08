extern crate diesel_hello;
extern crate diesel;

use self::diesel::prelude::*;
use self::diesel_hello::*;
use std::env::args;
use diesel_hello::establish_connection;
use diesel_hello::models::Company;

fn main() {
    use diesel_hello::schema::companies::dsl::{companies, philosofy};

    let id = args().nth(1).expect("publish_company requires a company id")
        .parse::<i32>().expect("Invalid ID");
    let connection = establish_connection();

    let company = diesel::update(companies.find(id))
        .set(philosofy.eq("big text here"))
        .get_result::<Company>(&connection)
        .expect(&format!("Unable to find post {}", id));
    println!("Published post {}", company.name);
}