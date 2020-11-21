import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int m = ni();
    int k = ni();

    int[][] map = new int[n][m];
    for (int i = 0; i < n; i++) {
      char[] s = ns();
      for (int j = 0; j < m; j++) {
        map[i][j] = s[j] - '0';
      }
    }

    int ret = 0;
    for (int d = 0; d <= 9; d++) {
      int[][] sum = new int[n + 1][m + 1];
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
          sum[i + 1][j + 1] = sum[i][j + 1] + sum[i + 1][j] - sum[i][j] + (map[i][j] == d ? 1 : 0);
        }
      }

      for (int len = 1; len <= Math.min(n, m); len++) {
        if (check(d, len, k, sum)) {
          ret = Math.max(ret, len);
        }
      }
    }
    System.out.println(ret);
  }

  private static boolean check(int d, int len, int k, int[][] sum) {
    int n = sum.length - 1;
    int m = sum[0].length - 1;

    for (int i = 0; i <= n - len; i++) {
      for (int j = 0; j <= m - len; j++) {
        int x = sum[i + len][j + len] - sum[i][j + len] - sum[i + len][j] + sum[i][j];
        int y = len * len - x;
        if (y <= k) {
          return true;
        }
      }
    }
    return false;
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
