#include <bits/stdc++.h>
using namespace std;

void update(int i, int n, vector<int> &a, set<int> &st) {
  swap(a[i], a[i + 1]);

  for (int d = -1; d <= 1; d++) {
    if (i + d < 0 || i + d + 1 >= n) continue;
    if (a[i + d] > a[i + d + 1])
      st.insert(i + d);
    else
      st.erase(i + d);
  }
}

int main() {
  int n, q;
  scanf("%d%d", &n, &q);
  vector<int> a(n);
  iota(a.begin(), a.end(), 0);

  set<int> st;

  for (int i = 0; i < q; i++) {
    int t, x, y;
    scanf("%d%d%d", &t, &x, &y);
    x--;
    y--;

    if (t == 1) {
      update(x, n, a, st);
    } else {
      while (true) {
        set<int>::iterator v = st.lower_bound(x);
        if (v == st.end() || *v + 1 > y) break;

        update(*v, n, a, st);
      }
    }
  }
  for (int i = 0; i < n; i++) {
    printf("%d ", a[i] + 1);
  }
  printf("\n");
}
