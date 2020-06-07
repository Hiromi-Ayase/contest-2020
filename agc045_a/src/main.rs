use proconio::input;
use proconio::marker::Chars;
use std::cmp::min;

fn main() {
    input! {
        t:usize
    }

    'outer: for _ in 0..t {
        input! {
            n:usize,
            a:[i64; n],
            s:Chars,
        }
        let mut v: Vec<i64> = Vec::new();
        for i in 0..n {
            let mut now = a[n - i - 1];
            for u in &v {
                now = min(now, now ^ u);
            }
            if now > 0 {
                if s[n - i - 1] == '0' {
                    v.push(now);
                } else {
                    println!("1");
                    continue 'outer;
                }
            }
        }
        println!("0");
    }
}
