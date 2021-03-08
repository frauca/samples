use std::thread;

pub fn move_concept(){
    let a=1;
    let b = 1;
    let join = thread::spawn(move||{
        println!("got {}",a)
    });

    println!("i still have {}",b);
    join.join();
}