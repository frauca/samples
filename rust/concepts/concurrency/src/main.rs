use std::sync::mpsc;
use std::thread;
use std::time::Duration;

mod moving;
mod transmitter_receiver;
mod mutex;

fn main() {
    println!("Hello, world!");
    moving::move_concept();
    transmitter_receiver::iterating();
    transmitter_receiver::async_rec();
    transmitter_receiver::multiple_transmitters();
    mutex::simple_counter();
}
