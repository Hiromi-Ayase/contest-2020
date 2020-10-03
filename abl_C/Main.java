import java.util.*;

class DSU {
  private int n;
  private int[] parentOrSize;

  public DSU(int n) {
    this.n = n;
    this.parentOrSize = new int[n];
    java.util.Arrays.fill(parentOrSize, -1);
  }

  int merge(int a, int b) {
    if (!(0 <= a && a < n))
      throw new IndexOutOfBoundsException("a=" + a);
    if (!(0 <= b && b < n))
      throw new IndexOutOfBoundsException("b=" + b);

    int x = leader(a);
    int y = leader(b);
    if (x == y)
      return x;
    if (-parentOrSize[x] < -parentOrSize[y]) {
      int tmp = x;
      x = y;
      y = tmp;
    }
    parentOrSize[x] += parentOrSize[y];
    parentOrSize[y] = x;
    return x;
  }

  boolean same(int a, int b) {
    if (!(0 <= a && a < n))
      throw new IndexOutOfBoundsException("a=" + a);
    if (!(0 <= b && b < n))
      throw new IndexOutOfBoundsException("b=" + b);
    return leader(a) == leader(b);
  }

  int leader(int a) {
    if (parentOrSize[a] < 0) {
      return a;
    } else {
      parentOrSize[a] = leader(parentOrSize[a]);
      return parentOrSize[a];
    }
  }

  int size(int a) {
    if (!(0 <= a && a < n))
      throw new IndexOutOfBoundsException("" + a);
    return -parentOrSize[leader(a)];
  }

  java.util.ArrayList<java.util.ArrayList<Integer>> groups() {
    int[] leaderBuf = new int[n];
    int[] groupSize = new int[n];
    for (int i = 0; i < n; i++) {
      leaderBuf[i] = leader(i);
      groupSize[leaderBuf[i]]++;
    }
    java.util.ArrayList<java.util.ArrayList<Integer>> result = new java.util.ArrayList<>(n);
    for (int i = 0; i < n; i++) {
      result.add(new java.util.ArrayList<>(groupSize[i]));
    }
    for (int i = 0; i < n; i++) {
      result.get(leaderBuf[i]).add(i);
    }
    result.removeIf(java.util.ArrayList::isEmpty);
    return result;
  }
}

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int m = ni();

    DSU ds = new DSU(n);
    for (int i = 0; i < m; i++) {
      ds.merge(ni() - 1, ni() - 1);
    }

    int ret = ds.groups().size() - 1;
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
