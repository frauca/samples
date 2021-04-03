use core::fmt;
use std::fmt::Formatter;

#[derive(Debug)]
pub enum Error {
    Conversion(String)
}

impl fmt::Display for Error {
    fn fmt(&self, f: &mut Formatter<'_>) -> fmt::Result {
        match self {
            Error::Conversion(error) => write!(f, "Could not convert that book:: {}", error)
        }
    }
}