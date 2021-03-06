use crate::gutenberg::Error;

static STARTING_TEXT: &str = "*** START OF";
static OTHER_STARTING_TEXT: &str = "***START OF";
static END_STARTING_TEXT: &str = "***\n";
static OTHER_ENDINGTEXT: &str = "***\r";
static ENDING_TEXT: &str = "\n*** END OF";
static OTHER_ENDING_TEXT: &str = "\n***END OF";
static POSIBLE_ENDING_TEXT: &str = "\nEnd of the Project Gutenberg EBook";
static INTERMEIDATE_TEXT: &str = "*******************************************************************\n";

pub fn parse(book: &str) -> Result<&str, Error> {
    let filtered = filter_start(book)?;
    let filtered = filter_end(filtered)?;
    filter_intermediates(filtered)
}

fn filter_start(book: &str) -> Result<&str, Error> {
    if let Some(partial_start) = book.find(STARTING_TEXT)
        .or(book.find(OTHER_STARTING_TEXT)) {
        let filtered_book = &book[partial_start + STARTING_TEXT.len()..];
        if let Some(start) = filtered_book.find(END_STARTING_TEXT)
            .or(filtered_book.find(OTHER_ENDINGTEXT)){
            return Ok(&filtered_book[start + END_STARTING_TEXT.len()..]);
        }
    }
    return Err(Error::BookFormat(
        String::from("Could not parse the start of the book")));
}

fn filter_end(book: &str) -> Result<&str, Error> {
    if let Some(end) = book.rfind(ENDING_TEXT).or(book.find(OTHER_ENDING_TEXT)) {
        let mut filtered = &book[..end];
        if let Some(finner_end) = filtered.rfind(POSIBLE_ENDING_TEXT) {
            filtered = &filtered[..finner_end];
        }
        return Ok(filtered);
    }
    return Err(Error::BookFormat(
        String::from("Could not parse the end of the book")));
}

fn filter_intermediates(book: &str) -> Result<&str, Error> {
    let mut intermediate = book;
    while let Some(new_start) = intermediate.find(INTERMEIDATE_TEXT) {
        println!("Found new start {}", new_start);
        intermediate = &intermediate[new_start + INTERMEIDATE_TEXT.len()..];
    }
    Ok(intermediate)
}

#[cfg(test)]
mod tests {
    use std::fs;

    use super::*;

    #[test]
    fn hello_world() {
        let hello_book = "*** START OF THIS PROJECT GUTENBERG EBOOK MOBY-DICK ***\n\
        hello\n\
        *** END OF THIS PROJECT GUTENBERG EBOOK MOBY-DICK ***";
        let hello = parse(hello_book).unwrap();

        assert_eq!(hello, "hello");
    }

    #[test]
    fn moby_dick_little() {
        let input = fs::read_to_string("resources/test/moby_dick_little.txt")
            .expect("Could not read input");

        assert_eq!(parse(&input).unwrap(), "moby dick book");
    }

    #[test]
    fn moby_dick() {
        let input = fs::read_to_string("resources/test/moby_dick.txt")
            .expect("Could not read input");
        let expected = fs::read_to_string("resources/test/moby_dick.extracted.txt")
            .expect("Could not read expected");

        assert_eq!(parse(&input).unwrap(), expected);
    }
}