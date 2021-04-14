use std::fmt;

use serde::Serialize;

#[derive( Clone, Debug)]
pub struct Book {
    pub id: i32,
    pub title: String,
    pub language: isolang::Language,
    pub state: State,
}

#[derive( Serialize, Clone, Debug)]
pub enum State {
    NEW,
    PROCESED,
    ERROR,
}

impl fmt::Display for Book {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        // Customize so only `x` and `y` are denoted.
        write!(f, "{:?}", self)
    }
}

impl fmt::Display for State {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        // Customize so only `x` and `y` are denoted.
        write!(f, "{:?}", self)
    }
}