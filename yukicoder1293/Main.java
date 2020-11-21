import java.util.*;

class DisjointSet {
  public int[] upper; // minus:num_element(root) plus:root(normal)
  // public int[] w;

  public DisjointSet(int n) {
    upper = new int[n];
    Arrays.fill(upper, -1);
    // w = new int[n];
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

  public int[][] toBucket() {
    int n = upper.length;
    int[][] ret = new int[n][];
    int[] rp = new int[n];
    for (int i = 0; i < n; i++) {
      if (upper[i] < 0)
        ret[i] = new int[-upper[i]];
    }
    for (int i = 0; i < n; i++) {
      int r = root(i);
      ret[r][rp[r]++] = i;
    }
    return ret;
  }
}

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int d = ni();
    int w = ni();
    DisjointSet ds1 = new DisjointSet(n);
    for (int i = 0; i < d; i++) {
      ds1.union(ni() - 1, ni() - 1);
    }

    List<Set<Integer>> list = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      Set<Integer> set = new HashSet<>();
      set.add(ds1.root(i));
      list.add(set);
    }

    DisjointSet ds2 = new DisjointSet(n);
    for (int i = 0; i < w; i++) {
      int a = ds2.root(ni() - 1);
      int b = ds2.root(ni() - 1);

      var set1 = list.get(a);
      var set2 = list.get(b);

      if (ds2.union(a, b)) {
        continue;
      }
      list.set(a, null);
      list.set(b, null);

      if (set1.size() < set2.size()) {
        var tmp = set1;
        set1 = set2;
        set2 = tmp;
      }
      for (int v : set2) {
        set1.add(v);
      }
      list.set(ds2.root(a), set1);
    }

    long ret = 0;
    int[][] buc1 = ds1.toBucket();
    int[][] buc2 = ds2.toBucket();

    for (int[] b : buc2) {
      if (b == null)
        continue;
      long x = 0;
      for (int root : list.get(ds2.root(b[0]))) {
        x += buc1[root].length;
      }
      ret += (x - 1) * b.length;
    }
    System.out.println(ret);
  }

  public static void main(String[] args) {
    new Thread(null, new Runnable() {
      @Override
      public void run() {
        long start = System.currentTimeMillis();
        String debug = args.length > 0 ? args[0] : null;
        if (debug != null) {
          try {
            is = java.nio.file.Files.newInputStream(java.nio.file.Paths.get(debug));
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
        reader = new java.io.BufferedReader(new java.io.InputStreamReader(is), 32768);
        solve();
        out.flush();
        tr((System.currentTimeMillis() - start) + "ms");
      }
    }, "", 64000000).start();
  }

  private static java.io.InputStream is = System.in;
  private static java.io.PrintWriter out = new java.io.PrintWriter(System.out);
  private static java.util.StringTokenizer tokenizer = null;
  private static java.io.BufferedReader reader;

  public static String next() {
    while (tokenizer == null || !tokenizer.hasMoreTokens()) {
      try {
        tokenizer = new java.util.StringTokenizer(reader.readLine());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return tokenizer.nextToken();
  }

  private static double nd() {
    return Double.parseDouble(next());
  }

  private static long nl() {
    return Long.parseLong(next());
  }

  private static int[] na(int n) {
    int[] a = new int[n];
    for (int i = 0; i < n; i++)
      a[i] = ni();
    return a;
  }

  private static char[] ns() {
    return next().toCharArray();
  }

  private static long[] nal(int n) {
    long[] a = new long[n];
    for (int i = 0; i < n; i++)
      a[i] = nl();
    return a;
  }

  private static int[][] ntable(int n, int m) {
    int[][] table = new int[n][m];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        table[i][j] = ni();
      }
    }
    return table;
  }

  private static int[][] nlist(int n, int m) {
    int[][] table = new int[m][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        table[j][i] = ni();
      }
    }
    return table;
  }

  private static int ni() {
    return Integer.parseInt(next());
  }

  private static void tr(Object... o) {
    if (is != System.in)
      System.out.println(java.util.Arrays.deepToString(o));
  }
}
