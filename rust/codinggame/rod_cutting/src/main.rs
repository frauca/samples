use std::collections::HashMap;

fn main() {
    println!("Hello, world!");
}

fn max_price(rod_lenght: i32, memory: &mut HashMap<i32, i32>, market: &Vec<MarketPiece>) -> i32 {
    if memory.contains_key(&rod_lenght) {
        return *memory.get(&rod_lenght).unwrap();
    } else {
        let max = recursive_max_price(rod_lenght, memory, market);
        memory.insert(rod_lenght, max);
        return max;
    }
}

fn recursive_max_price(rod_lenght: i32, memory: &mut HashMap<i32, i32>, market: &Vec<MarketPiece>) -> i32 {
    let mut max = 0;
    for pice in market {
        if pice.lenght <= rod_lenght {
            let max_subprice = max_price(rod_lenght - pice.lenght, memory, market);
            let price = pice.price + max_subprice;
            if price > max {
                max = price;
            }
        }
    }
    return max;
}

#[derive(Clone)]
#[derive(Debug)]
struct MarketPiece {
    lenght: i32,
    price: i32,
}

impl MarketPiece {
    const fn new(lenght: i32, price: i32) -> Self {
        MarketPiece { lenght, price }
    }
}


#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test1() {
        let mut market: Vec<MarketPiece> = vec![
            MarketPiece::new(1, 1),
            MarketPiece::new(2, 5),
            MarketPiece::new(3, 8),
            MarketPiece::new(4, 9),
        ];
        let mut memory = HashMap::new();
        assert_eq!(max_price(4, &mut memory, &market), 10);
    }

    #[test]
    fn test2() {
        let mut market: Vec<MarketPiece> = vec![
            MarketPiece::new(1, 1),
            MarketPiece::new(2, 5),
            MarketPiece::new(3, 8),
            MarketPiece::new(4, 9),
        ];
        let mut memory = HashMap::new();
        assert_eq!(max_price(12, &mut memory, &market), 32);
    }
}

// fn max_price(rod_lenght: i32,market: &mut Vec<MarketPiece>) -> i32 {
//     market.sort_by(|a, b| a.comp_value(b));
//     println!("{:?}",market);
//     return cut_prioritizing_firsts(rod_lenght, market.to_vec())
//         .iter().map(|pice| pice.price).sum();
// }

fn cut_prioritizing_firsts(rod_lenght: i32, market: Vec<MarketPiece>) -> Vec<MarketPiece> {
    let mut result = Vec::new();
    let mut current_lenght = rod_lenght;
    for pice in market {
        while pice.lenght <= current_lenght {
            result.push(pice.clone());
            current_lenght = current_lenght - pice.lenght;
        }
        if current_lenght == 0 {
            break;
        }
    }
    return result;
}


