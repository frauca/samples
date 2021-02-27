use std::fs;
use std::fs::File;
use std::io::ErrorKind;

// fn do_the_panic() {
//     let v = vec![1, 2, 3];
//
//     v[99];
// }

fn open_file() {
    let f = File::open("sample.txt");

    if let Err(error) = f {
        match error.kind() {
            ErrorKind::NotFound => match File::create("sample.txt") {
                Ok(fc) => println!("all good {:?}", fc),
                Err(e) => panic!("Problem creating the file: {:?}", e),
            },
            other_error => {
                panic!("Problem opening the file: {:?}", other_error)
            }
        }
    }
}

fn main() {
    println!("Hello, world!");
    //do_the_panic();
    open_file();
    let content = fs::read_to_string("sample.txt");
    println!("{}", content.unwrap());
}
