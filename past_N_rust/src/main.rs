use proconio::input;
use std::collections::BTreeSet;

fn main() {
    input! {
        n:i32,
        q:i32,
    }

    let mut a = (0..n).collect();
    let mut st = BTreeSet::new();
    for _i in 0..q {
        input! {
            t:i32,
            rx:usize,
            ry:usize,
        }
        if t == 1 {
            let x = rx - 1;
            update(x, &mut a, &mut st);
        } else {
            let x = rx - 1;
            let y = ry - 1;
            loop {
                match st.range(x..y).next() {
                    Some(i) => {
                        if a[*i] > a[*i + 1] {
                            update(*i, &mut a, &mut st);
                        }
                    }
                    None => break,
                }
            }
        }
    }
    for v in a {
        print!("{} ", v + 1);
    }
    println!();
}

fn update(x: usize, a: &mut Vec<i32>, st: &mut BTreeSet<usize>) {
    a.swap(x, x + 1);
    for d in 0..3 {
        if x + d == 0 || x + d >= a.len() {
            continue;
        }
        if a[x + d - 1] > a[x + d] {
            st.insert(x + d - 1);
        } else {
            st.remove(&(x + d - 1));
        }
    }
}
