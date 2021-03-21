use std::{io, fmt};
use std::fmt::Formatter;
use std::str::FromStr;

#[derive(Debug)]
pub enum Error{
    ReadFile(io::Error),
    ParseXml(minidom::Error)
}

impl fmt::Display for Error{
    fn fmt(&self, f: &mut Formatter<'_>) -> fmt::Result {
        match self {
            Error::ReadFile(error)=>write!(f,"Could not open file. Source reson:: {:?}",error),
            Error::ParseXml(error)=>write!(f,"Could not parse xml. Source reason:: {:?}",error)
        }
    }
}

impl From<io::Error> for Error{
    fn from(error: io::Error) -> Self {
        Error::ReadFile(error)
    }
}

impl From<minidom::Error> for Error{
    fn from(error: minidom::Error) -> Self {
        Error::ParseXml(error)
    }
}

pub type Result<T> = ::std::result::Result<T, Error>;