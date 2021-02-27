use warp::Filter;

use crate::store_controllers;

#[tokio::main]
pub async fn serve() {

    // GET /hello/warp => 200 OK with body "Hello, warp!"
    let hello = warp::path!("hello" / String)
        .map(|name| format!("Hello, {}!", name))
        .or(store_controllers::routes());

    warp::serve(hello)
        .run(([127, 0, 0, 1], 3030))
        .await;
}