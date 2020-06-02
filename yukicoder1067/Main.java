import java.util.Arrays;

public class Main {

  private static void solve() {
    int n = ni();
    int q = ni();
    int[] a = na(n);
    int mod = 998244353;

    Arrays.sort(a);
    for (int i = 0, j = n - 1; i < j; i ++ , j --) {
      int tmp = a[i];
      a[i] = a[j];
      a[j] = tmp;
    }

    long[][] dp = new long[n + 1][n + 2];
    dp[0][0] = 1;

    for (int i = 0; i < n; i ++) {
      for (int j = 0; j <= n; j ++) {
        dp[i + 1][j] = dp[i][j] * (a[i]-1) % mod;
        if (j > 0)
          dp[i + 1][j] += dp[i][j - 1];

        dp[i + 1][j] %= mod;
      }
    }

    long[] sum = new long[n + 1];
    sum[n] = 1;
    for (int i = n; i > 0; i --) {
      sum[i-1] = sum[i] * a[i-1] % mod;
    }

    for (int i = 0; i < q; i ++){ 
      int l = ni();
      int r = ni();
      int p = ni();

      long ret = 0;
      for (int k = l; k <= r; k ++) {
        int idx = bisect(k, a);

        long now = dp[idx + 1][p] * sum[idx + 1] % mod;
        ret ^= now;
      }
      out.println(ret);
    }

  }

  private static int bisect(int k, int[] a) {
    int n = a.length;
    int ok = 0;
    int ng = n;
    while (ng - ok > 1) {
      int v = (ng + ok) / 2;
      if (a[v] >= k) {
        ok = v;
      } else {
        ng = v;
      }
    }
    return ok;
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

