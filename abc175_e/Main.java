import java.util.Arrays;

public class Main {

  private static void solve() {
    int r = ni();
    int c = ni();

    int k = ni();

    long[][] table = new long[r][c];
    for (int i = 0; i < k; i++) {
      int y = ni() - 1;
      int x = ni() - 1;
      int v = ni();
      table[y][x] = v;
    }

    long[][][] dp = new long[r][c][4];
    for (long[][] v : dp)
      for (long[] u : v)
        Arrays.fill(u, Long.MIN_VALUE / 2);
    dp[0][0][0] = 0;

    for (int i = 0; i < r; i++) {
      for (int j = 0; j < c; j++) {

        if (i > 0) {
          for (int s = 0; s < 4; s++) {
            dp[i][j][0] = Math.max(dp[i][j][0], dp[i - 1][j][s]);
          }
        }
        if (j > 0)
          for (int s = 0; s < 4; s++) {
            dp[i][j][s] = Math.max(dp[i][j][s], dp[i][j - 1][s]);
          }

        if (table[i][j] > 0)
          for (int s = 3; s > 0; s--) {
            dp[i][j][s] = Math.max(dp[i][j][s], dp[i][j][s - 1] + table[i][j]);
          }
      }
    }

    long ret = 0;
    for (int s = 0; s < 4; s++) {
      ret = Math.max(dp[r - 1][c - 1][s], ret);
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
