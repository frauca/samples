extern crate diesel;
extern crate diesel_hello;

use self::diesel::prelude::*;
use self::diesel_hello::*;
use self::models::*;

fn main() {
    use diesel_hello::schema::companies::dsl::*;

    let connection = establish_connection();
    let results = companies
        .load::<Company>(&connection)
        .expect("Error loading posts");

    println!("Displaying {} companies", results.len());
    for post in results {
        println!("{}-{}", post.id, post.name);
        println!("----------\n");
        println!("{:?}", post.purpose);
        println!("{:?}", post.philosofy);
    }
}