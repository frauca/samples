use serde::Serialize;

#[derive(Clone, Debug)]
pub struct Book {
    pub id: i32,
    pub title: String,
    pub language: isolang::Language,
    pub state: State,
}

#[derive(Serialize, Clone, Debug)]
pub enum State {
    NEW,
    PROCESED,
    ERROR,
}
