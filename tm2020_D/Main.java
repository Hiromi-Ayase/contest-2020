import java.util.Arrays;

public class Main {

  private static void solve() {
    int n = ni();
    int[] v = new int[n + 1];
    int[] w = new int[n + 1];
    for (int i = 1; i <= n; i++) {
      v[i] = ni();
      w[i] = ni();
    }
    int[][] q = ntable(ni(), 2);

    int maxu = Math.min(512, n);
    int maxw = 100000;
    int[][] dp = new int[maxu + 1][maxw + 1];

    for (int i = 1; i <= maxu; i++) {
      Arrays.fill(dp[i], Integer.MIN_VALUE / 4);
      dp[i][0] = 0;
      for (int k = i; k > 0; k /= 2) {
        for (int j = maxw - w[k]; j >= 0; j--) {
          dp[i][j + w[k]] = Math.max(dp[i][j + w[k]], dp[i][j] + v[k]);
        }
      }
      for (int j = 1; j <= maxw; j++) {
        dp[i][j] = Math.max(dp[i][j - 1], dp[i][j]);
      }
    }

    int[] a = new int[30];
    int[] cv = new int[1 << 10];
    int[] cw = new int[1 << 10];

    for (int[] x : q) {
      int u = x[0];
      int l = x[1];

      if (u <= maxu) {
        out.println(dp[u][l]);
      } else {
        int m = 0;
        int y = u;
        for (; y > maxu; y /= 2) {
          a[m++] = y;
        }
        int ret = Integer.MIN_VALUE;
        cv[1] = v[a[0]];
        cw[1] = w[a[0]];
        for (int j = 1; j < m; j++) {
          for (int i = 0; i < (1 << j); i++) {
            cv[i | (1 << j)] = cv[i] + v[a[j]];
            cw[i | (1 << j)] = cw[i] + w[a[j]];
          }
        }
        for (int i = 0; i < (1 << m); i++) {
          if (cw[i] <= l) {
            ret = Math.max(ret, dp[y][l - cw[i]] + cv[i]);
          }
        }
        out.println(ret);
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
