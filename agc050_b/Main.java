import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int[] a = na(n);

    int m = n / 3 * 3;
    int k = n % 3;
    int ret = Integer.MIN_VALUE;

    for (int i = 0; i <= k; i++) {
      ret = Math.max(ret, dfs(i, m + i, a));
    }
    System.out.println(ret);
  }

  private static int[][] memo = new int[501][501];
  private static int minInf = Integer.MIN_VALUE / 1000;
  static {
    for (int[] v : memo)
      Arrays.fill(v, Integer.MIN_VALUE / 1000);
  }

  private static int dfs(int l, int r, int[] a) {
    if (l == r)
      return 0;
    else if (l > r)
      return minInf;

    if (memo[l][r] > minInf) {
      return memo[l][r];
    }

    int ret = minInf;
    ret = Math.max(ret, dfs(l, r - 3, a));
    ret = Math.max(ret, dfs(l + 3, r, a));

    if (r - l >= 3) {
      ret = Math.max(ret, dfs(l, r - 3, a) + a[r - 3] + a[r - 2] + a[r - 1]);
      ret = Math.max(ret, dfs(l + 1, r - 2, a) + a[l] + a[r - 2] + a[r - 1]);
      ret = Math.max(ret, dfs(l + 2, r - 1, a) + a[l] + a[l + 1] + a[r - 1]);
      ret = Math.max(ret, dfs(l + 3, r, a) + a[l] + a[l + 1] + a[l + 2]);
    }

    memo[l][r] = ret;
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
