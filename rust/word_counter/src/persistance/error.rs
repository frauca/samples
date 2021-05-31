use core::fmt;
use std::fmt::Formatter;

#[derive(Debug)]
pub enum Error {
    Conversion(String),
    Saving(String)
}

impl fmt::Display for Error {
    fn fmt(&self, f: &mut Formatter<'_>) -> fmt::Result {
        match self {
            Error::Conversion(error) => write!(f, "Could not convert that book:: {}", error),
            Error::Saving(error) => write!(f, "Could not save that book:: {}", error)

        }
    }
}

impl From<diesel::result::Error> for Error {
    fn from(error: diesel::result::Error) -> Self {
        Error::Saving(error.to_string())
    }
}