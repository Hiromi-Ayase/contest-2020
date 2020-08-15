import java.util.Arrays;

public class Main {

  private static void solve() {
    int n = ni();
    int t = ni();

    long[][] a = new long[n][2];
    long[] b = new long[n];
    int ap = 0, bp = 0;

    for (int i = 0; i < n; i++) {
      long x = ni();
      long y = ni();
      if (x == 0) {
        b[bp++] = y + 1;
      } else {
        a[ap++] = new long[] { x, y };
      }
    }
    a = Arrays.copyOf(a, ap);
    b = Arrays.copyOf(b, bp);

    Arrays.sort(b);
    Arrays.sort(a, (o1, o2) -> {
      long x = (o2[0]) * (o1[1] + 1) - (o1[0]) * (o2[1] + 1);
      return Long.signum(x);
    });

    for (int i = 1; i < bp; i++) {
      b[i] += b[i - 1];
    }

    int m = 50;
    long[][] dp = new long[ap + 1][m + 1];
    for (long[] v : dp)
      Arrays.fill(v, Integer.MAX_VALUE);

    for (int i = 0; i <= ap; i++) {
      dp[i][0] = 0;
    }
    for (int i = 0; i < ap; i++) {
      for (int j = 0; j < m; j++) {
        long now = dp[i][j];
        now += 1;
        now += a[i][0] * now + a[i][1];
        if (now >= Integer.MAX_VALUE) {
          now = Integer.MAX_VALUE;
        }
        dp[i + 1][j + 1] = Math.min(dp[i][j + 1], now);
      }
    }

    int ret = 0;
    for (int j = 0; j <= m; j++) {
      if (dp[ap][j] <= t) {
        ret = Math.max(ret, j + upperBound(b, t - dp[ap][j]) + 1);
      }
    }
    System.out.println(ret);
  }

  public static int upperBound(long[] a, long v) {
    int low = -1, high = a.length;
    while (high - low > 1) {
      int h = high + low >>> 1;
      if (a[h] <= v) {
        low = h;
      } else {
        high = h;
      }
    }
    return low;
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
