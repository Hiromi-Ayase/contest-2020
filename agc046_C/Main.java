import java.util.Arrays;

public class Main {

  private static void solve() {
    char[] s = ns();
    int k = ni();

    int mod = 998244353;
    int n = s.length;
    k = Math.min(k, n);

    int[] a = new int[n + 1];
    int m = 0;

    for (char c : s) {
      if (c == '1') {
        a[m]++;
      } else {
        m++;
      }
    }
    m++;
    a = Arrays.copyOf(a, m);

    long[][][] dp = new long[m + 1][k + 1][k + 1];
    dp[0][0][0] = 1;

    for (int i = 1; i <= m; i++) {
      long[] sum1 = new long[k * 2 + 1];

      for (int j = 0; j <= k; j++) {

        long sum2 = 0;

        for (int t = 0; t <= k; t++) {
          sum1[j - t + k] += dp[i - 1][j][t];
          sum1[j - t + k] %= mod;
        }

        // for (int q = 0; q <= k; q++) {
        // // 1をq回追加
        // if (j - q >= 0 && t - q >= 0)
        // dp[i][j][t] += dp[i - 1][j - q][t - q];
        // }
        for (int t = 0; t <= k; t++) {
          dp[i][j][t] += sum1[j - t + k];
        }

        for (int t = k, cnt = 0; t >= 0; t--, cnt++) {
          sum2 += dp[i - 1][j][t];
          if (cnt > a[i - 1]) {
            sum2 += mod - dp[i - 1][j][t + a[i - 1] + 1];
          }
          sum2 %= mod;
          // for (int q = 0; q <= k; q++) {
          // if (q <= a[i - 1] && t + q <= k && q > 0)
          // dp[i][j][t] += dp[i - 1][j][t + q];
          // }
          dp[i][j][t] += mod - dp[i - 1][j][t];
          dp[i][j][t] %= mod;
          dp[i][j][t] += sum2;

          // for (int q = 0; q <= k; q++) {
          // // 1をq回追加
          // if (j - q >= 0 && t - q >= 0)
          // dp[i][j][t] += dp[i - 1][j - q][t - q];

          // // 1をq回補充
          // if (q <= a[i - 1] && t + q <= k && q > 0)
          // dp[i][j][t] += dp[i - 1][j][t + q];
          // }
          dp[i][j][t] %= mod;
        }
      }
      // System.err.println(Arrays.deepToString(dp[i]) + " " + Arrays.toString(sum1));

    }

    long ret = 0;
    for (int i = 0; i <= k; i++) {
      ret += dp[m][i][0];
      ret %= mod;
    }

    // System.err.println(Arrays.toString(a));
    // System.err.println(Arrays.deepToString(dp));
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
