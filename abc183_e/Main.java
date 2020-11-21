import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int h = ni();
    int w = ni();
    char[][] s = new char[h + 2][w + 2];
    for (int i = 1; i <= h; i++) {
      char[] line = ns();
      for (int j = 1; j <= w; j++) {
        s[i][j] = line[j - 1];
      }
    }

    // dp[][][][0:right 1:down 2:right-down 3:stop]
    long[][][] dp = new long[h + 2][w + 2][4];
    dp[1][1][3] = 1;

    for (int i = 1; i <= h; i++) {
      for (int j = 1; j <= w; j++) {
        if (i == 1 && j == 1)
          continue;
        if (s[i][j] != '.')
          continue;
        dp[i][j][0] += (dp[i][j - 1][0] + dp[i][j - 1][3]) % mod;
        dp[i][j][1] += (dp[i - 1][j][1] + dp[i - 1][j][3]) % mod;
        dp[i][j][2] += (dp[i - 1][j - 1][2] + dp[i - 1][j - 1][3]) % mod;
        dp[i][j][3] += (dp[i][j - 1][0] + dp[i][j - 1][3] + dp[i - 1][j][1] + dp[i - 1][j][3] + dp[i - 1][j - 1][2]
            + dp[i - 1][j - 1][3]) % mod;

        for (int k = 0; k < 4; k++) {
          dp[i][j][k] %= mod;
        }
      }
    }
    System.out.println(dp[h][w][3]);
  }

  private static int mod = (int) 1e9 + 7;

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
