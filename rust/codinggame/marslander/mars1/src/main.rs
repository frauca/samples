#[derive(Debug)]
struct Point {
    x: i32,
    y: i32,
}

impl Point {
    const fn new(x: i32, y: i32) -> Self {
        Point {x,y}
    }
}

#[derive(Debug)]
struct Surface {
    points: Vec<Point>
}

impl Surface{
    const fn new(points:Vec<Point>) -> Self{
        Surface{
            points
        }
    }
}

fn main() {
    let mut surface = Surface::new(Vec::new());
    surface.add_point(1,2);
    println!("Hello, world! {:?}",surface);
}
