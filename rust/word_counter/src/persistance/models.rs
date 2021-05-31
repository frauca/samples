use crate::book::Book;
use crate::persistance::error::Error;
use crate::schema::books;
use crate::schema::words;
use crate::schema::words_books;
use crate::Word;

#[derive(Insertable, AsChangeset)]
#[table_name = "books"]
pub struct NewBook {
    pub id: i32,
    pub title: String,
    pub language: String,
}

#[derive(Queryable, AsChangeset)]
#[table_name = "books"]
pub struct DBBook {
    pub id: i32,
    pub title: String,
    pub language: String,
}

impl NewBook {
    pub fn from(book: &Book) -> Result<Self, Error> {
        let language = language_to_string(&book.language)?;

        Ok(NewBook {
            id: book.id,
            title: book.title.clone(),
            language: language,
        })
    }
}

impl DBBook {
    pub fn toBook(self) -> Result<Book,Error> {
        Ok(Book::new(self.id, self.title, string_to_language(&self.language)?))
    }
}

#[derive(Queryable, AsChangeset)]
#[table_name = "words"]
pub struct DBWord {
    pub id: i32,
    pub word: String,
    pub language: String,
    pub word_language: String
}

#[derive(Insertable, AsChangeset)]
#[table_name = "words"]
pub struct NewWord {
    pub word: String,
    pub language: String,
    pub word_language: String
}

impl NewWord {
    pub fn mew(word: &str, language: &isolang::Language) -> Result<Self, Error> {
        let language = language_to_string(language)?;

        Ok(NewWord {
            word: String::from(word),
            language: String::from(&language),
            word_language: format!("{}_{}",word,&language)
        })
    }
}

impl DBWord {
    pub fn toWord(self, ocurrence_in_book: u32) -> Word {
        Word::new(self.id, self.word, ocurrence_in_book)
    }
}

#[derive(Insertable, AsChangeset)]
#[table_name = "words_books"]
pub struct NewWordToBook {
    pub book_id: i32,
    pub word_id: i32,
    pub occurences: i32,
}

impl NewWordToBook {
    pub fn new(book_id: i32, word_id: i32, ocurrences: i32) -> Result<Self, Error> {
        Ok(NewWordToBook {
            book_id: book_id,
            word_id: word_id,
            occurences: ocurrences,
        })
    }
}


fn language_to_string(language: &isolang::Language) -> Result<String, Error> {
    let language = language.to_639_1()
        .ok_or(Error::Conversion(format!("invalid language {:?}", language)))?.to_string();
    Ok(language)
}

fn string_to_language(slang: &str) -> Result<isolang::Language,Error>{
    let language = isolang::Language::from_639_1(slang);
    language.ok_or(Error::Conversion(format!("invalid langauge {}",slang)))
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn convert_book() {
        let id = 37;
        let title = String::from("sample title");
        let language = isolang::Language::Cat;
        let book = Book::new(id, title, language);

        let new_book = NewBook::from(&book).unwrap();

        assert_eq!(new_book.id, id);
        assert_eq!(new_book.title, "sample title");
        assert_eq!(new_book.language, "ca");
    }

    #[test]
    fn convert_word_without_id() {
        let word = String::from("hello");
        let language = isolang::Language::Cat;
        let word = Word::new(word, 2);

        let new_word = NewWord::new(&word, &language).unwrap();

        assert_eq!(new_word.word, "hello");
        assert_eq!(new_word.language, "ca");
    }
}