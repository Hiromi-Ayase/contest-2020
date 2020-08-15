import java.util.Arrays;

public class Main {

  private static void solve() {
    char[] s = ns();
    int n = s.length;

    int mod = (int) 1e9 + 7;

    long[][] dp = new long[n + 1][27];
    long[][] dp2 = new long[n + 1][27];
    dp[0][0] = 1;

    for (int i = 0; i < n; i++) {
      int c = s[i] - 'a' + 1;
      for (int j = 1; j <= 26; j++) {
        dp[i + 1][j] += dp[i][j];
        dp[i + 1][c] += dp[i][j];

        dp2[i + 1][j] += dp2[i][j];
        if (c == j) {
          dp2[i + 1][c] += dp2[i][j];
        } else {
          dp2[i + 1][c] += dp2[i][j] + dp[i][j];
        }
      }
      dp[i + 1][0] += dp[i][0];
      dp[i + 1][c] += dp[i][0];

      dp2[i + 1][c] += dp[i][0];

      for (int j = 1; j <= 26; j++) {
        dp[i + 1][j] %= mod;
        dp2[i + 1][j] %= mod;
      }
    }

    long ret = 0;
    for (int i = 1; i <= 26; i++) {
      ret += dp2[n][i];
      ret %= mod;
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