use std::{fmt, io};
use std::fmt::Formatter;
use std::num::ParseIntError;

use strum::ParseError;
use tokio::task::JoinError;

#[derive(Debug)]
pub enum Error {
    ReadFile(io::Error),
    ParseXml(minidom::Error),
    Format(String),
    InvalidId(ParseIntError),
    InvalidLanguage(ParseError),
    BookFormat(String),
    BookFetch(reqwest::Error),
    NoUrlFound(String),
    Concurrenty(JoinError),
}

impl fmt::Display for Error {
    fn fmt(&self, f: &mut Formatter<'_>) -> fmt::Result {
        match self {
            Error::ReadFile(error) => write!(f, "Could not open file. Source reson:: {:?}", error),
            Error::ParseXml(error) => write!(f, "Could not parse xml. Source reason:: {:?}", error),
            Error::Format(error) => write!(f, "Found an incorrect tag. Source reason:: {}", error),
            Error::InvalidId(error) => write!(f, "Invalid id. Source reason:: {:?}", error),
            Error::InvalidLanguage(error) => write!(f, "Invalid language. Source reason:: {:?}", error),
            Error::BookFormat(error) => write!(f, "Could not parse the book. Source reason:: {:?}", error),
            Error::BookFetch(error) => write!(f, "Could not fetch the book. Source reason:: {:?}", error),
            Error::Concurrenty(error) => write!(f, "Could not spawn tasks. Source reason:: {:?}", error),
            Error::NoUrlFound(error) => write!(f, "Could not find content url. Source reason:: {:?}", error),

        }
    }
}

impl From<io::Error> for Error {
    fn from(error: io::Error) -> Self {
        Error::ReadFile(error)
    }
}

impl From<minidom::Error> for Error {
    fn from(error: minidom::Error) -> Self {
        Error::ParseXml(error)
    }
}

impl From<ParseIntError> for Error {
    fn from(error: ParseIntError) -> Self {
        Error::InvalidId(error)
    }
}

impl From<reqwest::Error> for Error {
    fn from(error: reqwest::Error) -> Self {
        Error::BookFetch(error)
    }
}

impl From<JoinError> for Error {
    fn from(error: JoinError) -> Self {
        Error::Concurrenty(error)
    }
}