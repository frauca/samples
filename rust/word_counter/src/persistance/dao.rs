use diesel::{PgConnection, RunQueryDsl};

use crate::book::Word;
use crate::persistance::error::Error;
use crate::persistance::models::{DBWord, NewBook, NewWord, NewWordToBook, DBBook};
use crate::schema::{books, words, words_books};
use crate::schema::books::columns::id;
use crate::Book;

pub fn save(connection: &PgConnection, book: &NewBook) -> Result<Book, Error> {
    let savedBook:DBBook = diesel::insert_into(books::table)
        .values(book)
        .on_conflict(id)
        .do_update()
        .set(book)
        .get_result(connection)?;
    Ok(savedBook.toBook()?)
}

pub fn save_words(connection: &PgConnection, words: &Vec<Word>, language: &isolang::Language) -> Result<Vec<Word>, Error> {
    let mut saved_words: Vec<Word> = vec![];
    for word in words {
        saved_words.push(save_word(connection, &NewWord::mew(&word.word, language)?)?
            .toWord(word.ocurrences));
    }
    Ok(saved_words)
}

pub fn relate_word_book(connection: &PgConnection, book: &Book, words: Vec<Word>) -> Result<(),Error>{
    for word in words {
        let relation = NewWordToBook::new(book.id, word.id, word.ocurrences as u32 as i32);
        save_relation(connection, &relation?)?;
    }
    Ok(())
}

fn save_word(connection: &PgConnection, word: &NewWord) -> Result<DBWord, Error> {
    Ok(diesel::insert_into(words::table)
        .values(word)
        .on_conflict(words::columns::word_language)
        .do_nothing()
        .get_result(connection)?)
}

fn save_relation(connection: &PgConnection, relation: &NewWordToBook) -> Result<usize, Error> {
    Ok(diesel::insert_into(words_books::table)
        .values(relation)
        .on_conflict_do_nothing()
        .execute(connection)?)
}