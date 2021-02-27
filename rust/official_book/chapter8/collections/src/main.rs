use std::collections::HashMap;

fn main() {
    let mut v: Vec<i32> = Vec::new();
    let v2 = vec![1, 2, 3];
    v.push(1);
    v.push(2);
    v.push(3);
    println!("{:?}", v);
    print!("{:?}", v2);

    for c in "नमस्ते".chars() {
        println!("{}", c);
    }


    for b in "नमस्ते".bytes() {
        println!("{}", b);
    }
    let mut scores = HashMap::new();

    scores.insert(String::from("sample"), 1);

    use std::collections::HashMap;

    let text = "hello world wonderful world";

    let mut map = HashMap::new();

    for word in text.split_whitespace() {
        let count = map.entry(word).or_insert(0);
        *count += 1;
    }

    println!("{:?}", map);
}
