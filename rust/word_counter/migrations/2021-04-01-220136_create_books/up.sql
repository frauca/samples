-- Your SQL goes here
CREATE TABLE books (
  id SERIAL PRIMARY KEY,
  title VARCHAR NOT NULL,
  language VARCHAR NOT NULL,
  state VARCHAR NOT NULL
)