use serde::Serialize;

pub struct Book {
    pub id: i32,
    pub title: String,
    pub language: isolang::Language,
    pub state: State,
}

#[derive(Serialize)]
pub enum State {
    NEW,
    PROCESED,
    ERROR,
}
