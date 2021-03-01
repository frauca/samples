extern crate paho_mqtt as mqtt;
use std::{env, process };
use futures::executor::block_on;
use std::time::Duration;

fn send_sync_message() {
    // Create a client & define connect options
    let cli = mqtt::Client::new("tcp://localhost:1883").unwrap_or_else(|err| {
        println!("Error creating the client: {:?}", err);
        process::exit(1);
    });

    let conn_opts = mqtt::ConnectOptionsBuilder::new()
        .keep_alive_interval(Duration::from_secs(20))
        .clean_session(true)
        .finalize();

    // Connect and wait for it to complete or fail
    if let Err(e) = cli.connect(conn_opts) {
        println!("Unable to connect:\n\t{:?}", e);
        process::exit(1);
    }

    // Create a message and publish it
    let msg = mqtt::Message::new("test", "Hello world!", 0);
    if let Err(e) = cli.publish(msg){
        println!("Error sending message: {:?}", e);
    }

    // Disconnect from the broker
    cli.disconnect(None).unwrap();
}

fn send_asyn_message(){
// Command-line option(s)
    let host = env::args().nth(1).unwrap_or_else(||
        "tcp://localhost:1883".to_string()
    );

    // Create the client
    let cli = mqtt::AsyncClient::new(host).unwrap_or_else(|err| {
        println!("Error creating the client: {}", err);
        process::exit(1);
    });

    if let Err(err) = block_on(async {
        // Connect with default options and wait for it to complete or fail
        println!("Connecting to the MQTT server");
        cli.connect(None).await?;

        // Create a message and publish it
        println!("Publishing a message on the topic 'test'");
        let msg = mqtt::Message::new("test", "Hello Rust MQTT world!", mqtt::QOS_1);
        cli.publish(msg).await?;

        // Disconnect from the broker
        println!("Disconnecting");
        cli.disconnect(None).await?;

        Ok::<(), mqtt::Error>(())
    }) {
        eprintln!("{}", err);
    }
}

fn main() {
    println!("Hello, world!");
    send_asyn_message();
    println!("Message sended");
}
