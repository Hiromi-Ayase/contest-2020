import java.util.Arrays;

public class Main {

  private static void solve() {
    int n = ni();
    int k = ni();
    int[] p = na(n);
    int[] c = na(n);

    for (int i = 0; i < n; i++) {
      p[i]--;
    }

    long ret = Long.MIN_VALUE;
    boolean[] ved = new boolean[n];
    for (int i = 0; i < n; i++) {
      Arrays.fill(ved, false);
      max = Long.MIN_VALUE;
      dfs(p[i], k - 1, p, c, ved, c[p[i]]);
      ret = Math.max(ret, max);
    }
    System.out.println(ret);
  }

  static long max;

  private static void dfs(int cur, int k, int[] p, int[] c, boolean[] ved, long score) {
    max = Math.max(score, max);

    if (k == 0) {
      return;
    }
    if (ved[cur]) {
      int len = 0;
      int now = cur;
      long sum = 0;
      while (true) {
        len++;
        now = p[now];
        sum += c[now];

        if (now == cur) {
          break;
        }
      }

      long ret = score + sum * (k / len);
      max = Math.max(ret, max);

      int last = k % len;
      now = p[cur];
      for (int i = 0; i < last; i++) {
        ret += c[now];
        now = p[now];

        max = Math.max(ret, max);
      }
      return;
    }

    ved[cur] = true;
    dfs(p[cur], k - 1, p, c, ved, score + c[p[cur]]);
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
