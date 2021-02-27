use warp::{Filter, Rejection, Reply};


pub fn routes() -> impl Filter<Extract=(impl Reply, ), Error=Rejection> + Clone {
    store_routes()
        .or(item_routes())
}

fn store_routes() -> impl Filter<Extract=(impl Reply, ), Error=Rejection> + Clone {
    let store = warp::path("store");
    warp::get()
        .and(store)
        .and_then(getStore)
}

fn item_routes() -> impl Filter<Extract=(impl Reply, ), Error=Rejection> + Clone {
    let item = warp::path("item");
    item.clone()
        .and(warp::get())
        .and_then(getItem)
}

async fn getItem() -> Result<impl Reply, Rejection> {
    Ok("warp::reply::json(Sample item, 1))")
}

async fn getStore() -> Result<impl Reply, Rejection> {
    Ok("get store")
}