use std::thread;
use std::io::Write;

pub fn init_logger() {
    let start = std::time::Instant::now();
    env_logger::Builder::from_default_env().format(move |buf, rec| {
        let t = start.elapsed().as_secs_f32();
        writeln!(buf, "{:.03} [{:?}-{}] - {}", t, thread::current().id(), rec.level(), rec.args())
    }).init();
}