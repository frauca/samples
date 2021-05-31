use std::error::Error;
use std::io::Write;
use std::thread;

use futures::TryFutureExt;
use log::info;
use tokio::task;
use tokio::task::{JoinError, JoinHandle};

fn init_logger() {
    let start = std::time::Instant::now();
    env_logger::Builder::from_default_env().format(move |buf, rec| {
        let t = start.elapsed().as_secs_f32();
        writeln!(buf, "{:.03} [{:?}-{}] - {}", t, thread::current().id(), rec.level(), rec.args())
    }).init();
}

//https://httpbin.org/delay/2.5
fn slowwly(delay_ms: u32) -> reqwest::Url {
    let url = format!(
        "http://slowwly.robertomurray.co.uk/delay/{}/url/http://www.google.co.uk",
        delay_ms,
    );
    reqwest::Url::parse(&url).unwrap()
}

fn fib_cpu_intensive(n: u32) -> u32 {
    match n {
        0 => 0,
        1 => 1,
        n => fib_cpu_intensive(n - 1) + fib_cpu_intensive(n - 2),
    }
}

fn analyze(txt: &str) -> (u64, u64) {
    let txt = txt.as_bytes();
    // Let's spend as much time as we can and count them in two passes
    let ones = txt.iter().fold(0u64, |acc, b: &u8| acc + b.count_ones() as u64);
    let zeros = txt.iter().fold(0u64, |acc, b: &u8| acc + b.count_zeros() as u64);
    (ones, zeros)
}

async fn request(n: usize) -> Result<(u64, u64), String> {
    info!("Requesting {}", n);
    let response: reqwest::Response = reqwest::get(slowwly(1000)).await.map_err(|e| String::from(format!("could not reqeust {}", e)))?;
    info!("Got response {}", n);
    let txt = response.text().map_err(|e| String::from(format!("could not get text {:?}", e))).await?;
    info!("Got text of {}", n);
    let res = task::spawn_blocking(move || analyze(&txt)).map_err(|e| String::from(format!("could not get text {:?}", e))).await?;
    info!("Processed {}", n);
    let fib = task::spawn(async{ fib_cpu_intensive(30)}).map_err(|e| String::from(format!("could not get text {:?}", e))).await?;
    info!("Fibonacio calculated {} in {}", fib, n);
    Ok(res)
}

async fn app() -> Result<(), String> {
    let mut responses: Vec<JoinHandle<Result<(u64, u64), String>>> = vec![];

    for i in 1..16 {
        responses.push(task::spawn(request(i)));
    }
    futures::future::join_all(responses).await;
    Ok(())
}

fn main() {
    init_logger();
    info!("Hello, world!");

    let mut rt = tokio::runtime::Runtime::new().unwrap();

    rt.block_on(app());
}
