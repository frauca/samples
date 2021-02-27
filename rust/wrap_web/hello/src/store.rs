use std::collections::HashMap;
use std::io::Write;
use std::sync::Arc;

use serde::{Deserialize, Serialize};
use tokio::sync::RwLock;

type Items = Vec<Item>;


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
            grocery_list: Arc::new(RwLock::new(Vec::new())),
        }
    }

    pub async fn add(&self, item: Item) -> Item {
        let mut unlocked_items = self.grocery_list.write().await;
        unlocked_items.push(item);
        unlocked_items.last().unwrap().clone()
    }
}

impl Item {
    pub fn new(name: String, quantity: i32) -> Self {
        Item { name, quantity }
    }
}