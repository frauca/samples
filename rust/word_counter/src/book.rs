pub struct Book {
    pub id: u32,
    pub title: String,
    pub description: String,
    pub language: Language
}

pub enum Language {
    EN
}