use std::io::{Read, stdin};

use diesel_hello::{create_company, establish_connection};

fn main() {
    let connection = establish_connection();

    println!("What would you like your title to be?");
    let mut name = String::new();
    stdin().read_line(&mut name).unwrap();
    let name = &name[..(name.len() - 1)]; // Drop the newline character
    println!("\nOk! Let's write {} (Press {} when finished)\n", name, EOF);
    let mut purpose = String::new();
    stdin().read_to_string(&mut purpose).unwrap();
    let purpose: Option<&str> = { if purpose.is_empty() { None } else { Some(&purpose) } };
    println!("\nOk! Let's write {} (Press {} when finished)\n", name, EOF);
    let mut phylosofy = String::new();
    stdin().read_to_string(&mut phylosofy).unwrap();
    let phylosofy: Option<&str> = { if phylosofy.is_empty() { None } else { Some(&phylosofy) } };
    let post = create_company(&connection, name, purpose, phylosofy);
    println!("\nSaved draft {} with id {}", post.name, post.id);
}

#[cfg(not(windows))]
const EOF: &'static str = "CTRL+D";

#[cfg(windows)]
const EOF: &'static str = "CTRL+Z";