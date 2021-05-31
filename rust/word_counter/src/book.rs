use std::fmt;

#[derive( Clone, Debug)]
pub struct Book {
    pub id: i32,
    pub title: String,
    pub language: isolang::Language,
    pub words: Vec<Word>,
}

#[derive( Clone, Debug)]
pub struct Word {
    pub id: i32,
    pub word: String,
    pub ocurrences: u32,
}


impl Book{
    pub fn new(id: i32, title:String, language:isolang::Language) -> Self{
        Book{
            id: id,
            title: title,
            language: language,
            words: vec![]
        }
    }
}

impl Word{
    pub fn new(id: i32, word: String, ocurrences:u32) -> Self {
        Word{
            id: id,
            word: word,
            ocurrences: ocurrences
        }
    }
}

impl fmt::Display for Book {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        // Customize so only `x` and `y` are denoted.
        write!(f, "{:?}", self)
    }
}