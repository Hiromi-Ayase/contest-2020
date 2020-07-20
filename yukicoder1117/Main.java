import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class Main {

  private static void solve() {
    int n = ni();
    int k = ni();
    int m = ni();

    long[] a = nal(n);

    long[][] dp = new long[n + 1][k + 1];
    for (long[] v : dp)
      Arrays.fill(v, Long.MIN_VALUE / 2);
    dp[0][0] = 0;

    long[] sum = new long[n + 1];
    for (int i = 0; i < n; i++) {
      sum[i + 1] = sum[i] + a[i];
    }

    Deque<long[]> q1 = new ArrayDeque<>();
    Deque<long[]> q2 = new ArrayDeque<>();
    for (int j = 1; j <= k; j++) {
      q1.clear();
      q2.clear();
      for (int i = 1; i <= n; i++) {
        if (n - i < k - j)
          break;

        long v1 = dp[i - 1][j - 1] - sum[i - 1];
        long v2 = dp[i - 1][j - 1] + sum[i - 1];

        long[] e1 = { v1, i };
        long[] e2 = { v2, i };

        while (q1.size() > 0 && q1.peekLast()[0] <= e1[0]) {
          q1.pollLast();
        }
        while (q2.size() > 0 && q2.peekLast()[0] <= e2[0]) {
          q2.pollLast();
        }
        q1.addLast(e1);
        q2.addLast(e2);
        if (q1.peekFirst()[1] == i - m) {
          q1.pollFirst();
        }
        if (q2.peekFirst()[1] == i - m) {
          q2.pollFirst();
        }

        dp[i][j] = Math.max(dp[i][j], q1.peekFirst()[0] + sum[i]);
        dp[i][j] = Math.max(dp[i][j], q2.peekFirst()[0] - sum[i]);
      }
    }
    System.out.println(dp[n][k]);

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
