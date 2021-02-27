use std::convert::Infallible;

use warp::{Filter, Rejection, Reply};

use crate::store::{Item, Store};

pub fn routes() -> impl Filter<Extract=(impl Reply, ), Error=Rejection> + Clone {
    let store = Store::new();
    store_routes(store.clone())
        .or(item_routes(store.clone()))
}

fn store_routes(store: Store) -> impl Filter<Extract=(impl Reply, ), Error=Rejection> + Clone {
    let store_path = warp::path("store");
    warp::get()
        .and(store_path)
        .and_then(get_store)
}

fn item_routes(store: Store) -> impl Filter<Extract=(impl Reply, ), Error=Rejection> + Clone {
    let item = warp::path("item");
    item.clone()
        .and(warp::get())
        .and(with_store(store))
        .and_then(get_item)
}

async fn save_item(item: Item, store: Store) -> Result<impl Reply, Rejection> {
    let saved_item = store.add(item).await;
    Ok(warp::reply::json(&saved_item))
}

async fn get_item(store: Store) -> Result<impl Reply, Infallible> {
    Ok(warp::reply::json(&Item::new(String::from("sample"), 1)))
}

async fn get_store() -> Result<impl Reply, Rejection> {
    Ok("get store")
}

fn with_store(store: Store) -> impl Filter<Extract=(Store, ), Error=Infallible> + Clone {
    warp::any().map(move || store.clone())
}