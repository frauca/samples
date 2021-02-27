use std::collections::HashMap;
use std::sync::Arc;

use serde::{Deserialize, Serialize};
use tokio::sync::RwLock;

type Items = HashMap<String, i32>;

pub fn joder(){

}

#[derive(Debug, Deserialize, Serialize, Clone)]
pub struct Item {
    pub name: String,
    pub quantity: i32,
}

#[derive(Clone)]
pub struct Store {
    grocery_list: Arc<RwLock<Items>>
}

impl Store {
    pub fn new() -> Self {
        Store {
            grocery_list: Arc::new(RwLock::new(HashMap::new())),
        }
    }
}

impl Item {
    pub fn new(name: String, quantity: i32) -> Self {
        Item { name, quantity }
    }
}