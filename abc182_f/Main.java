import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    long x = nl();
    long[] a = nal(n);

    long ret = dfs(x, n - 1, a);
    System.out.println(ret);
  }

  private static Map<Long, Long> memo = new HashMap<>();

  private static long dfs(long x, int i, long[] a) {
    long h = x * 100 + i;
    if (memo.containsKey(h))
      return memo.get(h);

    long ret = 0;
    long y = Math.abs(x) / a[i];
    if (i <= a.length - 2 && y >= a[i + 1] / a[i])
      return 0;
    if (i == 0 || x == 0) {
      return 1;
    }

    if (x > 0) {
      ret += dfs(x - (y + 0) * a[i], i - 1, a);

      if (i == a.length - 1 || y + 1 < a[i + 1] / a[i])
        ret += dfs(x - (y + 1) * a[i], i - 1, a);
    } else {
      ret += dfs(x + (y + 0) * a[i], i - 1, a);

      if (i == a.length - 1 || y + 1 < a[i + 1] / a[i])
        ret += dfs(x + (y + 1) * a[i], i - 1, a);
    }

    memo.put(h, ret);
    return ret;
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
