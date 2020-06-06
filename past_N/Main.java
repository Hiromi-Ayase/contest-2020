import java.util.Arrays;
import java.util.TreeSet;

public class Main {

  private static void solve() {
    int n = ni();
    int q = ni();

    int[] a = new int[n];
    for (int i = 0; i < n; i++) {
      a[i] = i;
    }
    TreeSet<Integer> set = new TreeSet<>();

    for (int i = 0; i < q; i++) {
      int t = ni();
      int x = ni() - 1;
      int y = ni() - 1;
      if (t == 1) {
        update(x, a, set);
      } else {
        while (true) {
          Integer v = set.ceiling(x);
          if (v == null || v + 1 > y) {
            break;
          }
          if (a[v] > a[v + 1]) {
            update(v, a, set);
          }
        }
      }
    }
    for (int i = 0; i < n; i++) {
      out.print(a[i] + 1 + " ");
    }
    out.println();
  }

  private static void update(int i, int[] a, TreeSet<Integer> set) {
    int n = a.length;
    if (i >= n - 1 || i < 0)
      return;

    int tmp = a[i];
    a[i] = a[i + 1];
    a[i + 1] = tmp;

    for (int d = -1; d <= 1; d++) {
      if (i + d < 0 || i + d + 1 >= n)
        continue;
      if (a[i + d] > a[i + d + 1]) {
        set.add(i + d);
      } else {
        set.remove(i + d);
      }
    }
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
