fn main() {
    let s = String::from("Just a sample");
    takes_ownershipt(s);
    //println!("This is out of scope as I have add it to function {}", s); //uncomment this will not compile as the s has been invalidated calling the function

    let s = String::from("Just a sample");
    let s = return_ownership(s);
    println!("Here is the value {}", s);

    just_use_it(&s);
    println!("Here is the value {}", s);
}

fn takes_ownershipt(s: String) {
    println!("Here is the value {}", s);
}

fn return_ownership(s: String) -> String {
    println!("Here is the value {}", s);
    s
}

fn just_use_it(s: &String) {
    println!("Here is the value {}", s);
}
