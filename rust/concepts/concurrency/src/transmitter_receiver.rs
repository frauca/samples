use std::sync::mpsc;
use std::thread;
use std::time::Duration;
use std::sync::mpsc::TryRecvError::Disconnected;

pub fn iterating(){
    let (tx, rx) = mpsc::channel();

    let join = thread::spawn(move || {
        let vals = vec![
            String::from("hi"),
            String::from("from"),
            String::from("the"),
            String::from("thread"),
        ];

        for val in vals {
            tx.send(val).unwrap();
            thread::sleep(Duration::from_secs(1));
        }
        println!("I'm done");
    });

    for received in rx {
        println!("Got: {}", received);
    }
    if let Err(error) = join.join() {
        println!("Could not wait reporter {:?}", error);
    }
}

pub fn async_rec(){
    let (tx, rx) = mpsc::channel();

    let join = thread::spawn(move || {
        let vals = vec![
            String::from("hi"),
            String::from("from"),
            String::from("the"),
            String::from("thread"),
        ];

        for val in vals {
            tx.send(val).unwrap();
            thread::sleep(Duration::from_secs(1));
        }
        println!("I'm done");
    });

    while true {
        let message_event = rx.try_recv();
        match message_event  {
            Ok(message) => println!("I hot this message {}", message),
            Err(error) => {
                if error == Disconnected {
                    println!("Ei sender has nothing else to say");
                    break;
                }
                thread::sleep(Duration::from_millis(500));
            }
        }
    }
}

pub fn multiple_transmitters(){
    let (tx, rx) = mpsc::channel();

    let tx1 = tx.clone();
    thread::spawn(move || {
        let vals = vec![
            String::from("hi"),
            String::from("from"),
            String::from("the"),
            String::from("thread"),
        ];

        for val in vals {
            tx1.send(val).unwrap();
            thread::sleep(Duration::from_secs(1));
        }
    });

    thread::spawn(move || {
        let vals = vec![
            String::from("more"),
            String::from("messages"),
            String::from("for"),
            String::from("you"),
        ];

        for val in vals {
            tx.send(val).unwrap();
            thread::sleep(Duration::from_secs(1));
        }
    });

    for received in rx {
        println!("Got: {}", received);
    }
}