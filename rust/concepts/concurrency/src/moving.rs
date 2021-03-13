use std::thread;

pub fn move_concept() {
    let a = String::from("test");
    let b = String::from("me");
    let join = thread::spawn(move || {
        println!("got {}", a);
    });

    println!("i still have {}", b);
    join.join();
}