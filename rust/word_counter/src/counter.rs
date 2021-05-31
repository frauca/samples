use std::collections::HashMap;

use unicode_segmentation::UnicodeSegmentation;

pub fn count(text: &str) -> HashMap<String, u32> {
    let mut words: HashMap<String, u32> = HashMap::new();
    let uppercase_splited: Vec<String> = text.unicode_words()
        .map(|word| word.to_lowercase())
        .collect();
    for word in uppercase_splited.iter() {
        match words.get_mut(word) {
            Some(num) => *num = *num + 1,
            None => {
                words.insert(word.clone(), 1);
                ()
            }
        }
    }

    words
}

#[cfg(test)]
mod tests {
    use std::collections::HashMap;
    use unicode_segmentation::UnicodeSegmentation;
    use crate::counter::count;
    use std::fs;

    #[test]
    fn unicode_play_test() {
        let text = "hello world";
        let words: Vec<&str> = text.unicode_words().collect();

        assert_eq!(2, words.len());
        assert_eq!("hello", words[0]);
        assert_eq!("world", words[1]);
    }

    #[test]
    fn simple_count() {
        let text = "Hello world, Hello everyone";
        let words: HashMap<String, u32> = count(&text);

        assert_eq!(3, words.len());
        assert_eq!(Some(&2), words.get("hello"));
        assert_eq!(Some(&1), words.get("world"));
        assert_eq!(Some(&1), words.get("everyone"));
    }

    #[test]
    fn moby_dick_words() {
        let mobydick = fs::read_to_string("resources/test/moby_dick.extracted.txt")
            .expect("Could not read expected");

        assert_eq!(17623, count(&mobydick).len());
    }
}