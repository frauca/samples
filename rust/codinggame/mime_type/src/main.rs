use std::collections::HashMap;

fn main() {
    let mut extensions = HashMap::new();
    extensions.insert("gif".to_lowercase(), "image");
    let extension = extension("animated.gif");
    println!("{}", format(extensions, extension));
}

fn extension(file_name: &str) -> &str {
    let splited: Vec<&str> = file_name.split(".").collect();
    let unkown = "UNKNOWN";
    if splited.len() > 1 {
        return splited.last().unwrap_or(&unkown);
    }
    return unkown;
}

fn format(extensions: HashMap<String, &str>, extension: &str) -> String {
    let mime_type = extensions.get(extension).unwrap_or(&"undefined");
    format!("{}/{}", mime_type, extension)
}
