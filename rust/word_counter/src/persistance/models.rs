use crate::schema::books;
use crate::book::Book;
use crate::persistance::error::Error;

#[derive(Insertable,AsChangeset)]
#[table_name = "books"]
pub struct NewBook {
    pub id: i32,
    pub title: String,
    pub language: String,
    pub state: String,
}


impl NewBook{
    pub fn from(book: &Book) -> Result<Self, Error> {
        let language = book.language.to_639_1()
            .ok_or(Error::Conversion(format!("invalid language {:?}", book.language)))?.to_string();
        let state = serde_plain::to_string(&book.state)
            .map_err(|error| Error::Conversion(format!("invalid state. Source error is {:?}",error)))?;

        Ok(NewBook{
            id: book.id,
            title: book.title.clone(),
            language: language,
            state: state
        })

    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use crate::book::State;

    #[test]
    fn convert_book() {
        let book = Book{
            id: 37,
            title: String::from("sample title"),
            language: isolang::Language::Cat,
            state: State::NEW
        };

        let new_book = NewBook::from(&book).unwrap();

        assert_eq!(new_book.id, 37);
        assert_eq!(new_book.title, "sample title");
        assert_eq!(new_book.language, "ca");
        assert_eq!(new_book.state, "NEW");
    }
}