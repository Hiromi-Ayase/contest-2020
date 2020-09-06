import java.util.Arrays;

class Solution {

  public static void main(String[] args) {
    int[][] e = { { 1, 1, 2 }, { 2, 2, 3 }, { 2, 3, 4 }, { 1, 3, 5 }, { 3, 2, 6 }, { 2, 3, 7 }, { 3, 7, 8 },
        { 3, 2, 9 }, { 2, 4, 10 }, { 2, 9, 11 }, { 1, 2, 12 }, { 3, 4, 13 }, { 2, 2, 7 }, { 1, 1, 9 }, { 1, 2, 13 },
        { 2, 7, 13 }, { 3, 2, 3 }, { 1, 7, 10 }, { 2, 8, 11 }, { 1, 2, 7 }, { 2, 1, 9 }, { 2, 2, 9 }, { 1, 5, 6 },
        { 2, 4, 9 }, { 1, 7, 8 }, { 1, 4, 6 }, { 1, 4, 9 }, { 3, 7, 13 }, { 2, 2, 8 }, { 2, 2, 6 }, { 1, 1, 10 },
        { 1, 1, 11 }, { 2, 5, 10 }, { 1, 2, 9 }, { 2, 1, 2 }, { 1, 3, 4 }, { 3, 6, 8 }, { 3, 6, 13 }, { 1, 3, 8 },
        { 1, 1, 6 }, { 3, 3, 9 }, { 1, 2, 3 }, { 1, 11, 13 } };
     int n = 13;

     System.out.println(new Solution().maxNumEdgesToRemove(n, e));
  }

  public int maxNumEdgesToRemove(int n, int[][] edges) {
    int ret = kruskal(edges, n);
    return ret;
  }

  public static int kruskal(int[][] edges, int n) {
    Arrays.sort(edges, (o1, o2) -> o2[0] - o1[0]);

    DisjointSet ds1 = new DisjointSet(n); // alice
    DisjointSet ds2 = new DisjointSet(n); // bob

    int p = edges.length;

    int d = 0;
    for (int i = 0; i < p; i++) {
      int type = edges[i][0];
      int u = edges[i][1] - 1;
      int v = edges[i][2] - 1;

      if (type == 1) {
        if (!ds1.equiv(u, v)) {
          ds1.union(u, v);
          d++;
        }
      } else if (type == 2) {
        if (!ds2.equiv(u, v)) {
          ds2.union(u, v);
          d++;
        }
      } else {
        if (!ds2.equiv(u, v)) {
          ds1.union(u, v);
          ds2.union(u, v);
          d++;
        }
      }
    }
    if (ds1.count() > 1 || ds2.count() > 1) {
      return -1;
    } else {
      return p - d;
    }
  }

  static class DisjointSet {
    public int[] upper;

    public DisjointSet(int n) {
      upper = new int[n];
      Arrays.fill(upper, -1);
    }

    public DisjointSet(DisjointSet ds) {
      this.upper = Arrays.copyOf(ds.upper, ds.upper.length);
    }

    public int root(int x) {
      return upper[x] < 0 ? x : (upper[x] = root(upper[x]));
    }

    public boolean equiv(int x, int y) {
      return root(x) == root(y);
    }

    public boolean union(int x, int y) {
      x = root(x);
      y = root(y);
      if (x != y) {
        if (upper[y] < upper[x]) {
          int d = x;
          x = y;
          y = d;
        }
        // w[x] += w[y];
        upper[x] += upper[y];
        upper[y] = x;
      }
      return x == y;
    }

    public int count() {
      int ct = 0;
      for (int u : upper) {
        if (u < 0)
          ct++;
      }
      return ct;
    }
  }
}