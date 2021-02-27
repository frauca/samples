fn main() {
    println!("Hello, world!");

    another_function();

    funct_with_param(32);

    funct_with_param(plus(1, 4));

    conditional();

    infinite_loop();
}

fn another_function() {
    println!("Another function.");
}

fn funct_with_param(x: i32) {
    println!("Param is {}", x);
}

fn plus(x: i32, y: i32) -> i32 {
    x + y
}

fn conditional() {
    let condition = true;
    let number = if condition { 5 } else { 6 };
    println!("The value of number is: {}", number);
}

fn infinite_loop(){
    let mut counter = 0;
    loop {
        counter += 1;
        if counter > 100 {
            counter += 1;
            if counter > 200 {
                println!("insided break");
                break;
            }
        }
    }
}
