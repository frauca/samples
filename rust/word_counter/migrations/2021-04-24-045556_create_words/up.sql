-- Your SQL goes here
CREATE TABLE words (
   id SERIAL PRIMARY KEY,
  word VARCHAR NOT NULL,
  language VARCHAR NOT NULL,
  CONSTRAINT uk_word_language UNIQUE( word, language )
);

CREATE INDEX ON words (word,language);

CREATE TABLE words_books(
    book_id SERIAL,
    word_id SERIAL,
    occurences int NOT NULL,
    PRIMARY KEY(book_id,word_id),
    CONSTRAINT fk_book FOREIGN KEY(book_id) REFERENCES books(id),
    CONSTRAINT fk_word FOREIGN KEY(word_id) REFERENCES words(id)
);