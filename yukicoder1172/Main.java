import java.util.Arrays;

public class Main {

  private static void solve() {
    int k = ni();
    int n = ni();
    int m = ni();
    long[] a = nal(k);
    long[] c = nal(k);
    int mod = (int) 1e9 + 7;

    {
      long[] b = new long[n + k + 1];
      for (int i = 0; i < b.length; i++) {
        if (i < k) {
          b[i] = a[i];
        } else {
          for (int j = 1; j <= k; j++) {
            b[i] += c[j - 1] * b[i - j] % mod;
            b[i] %= mod;
          }
        }
      }
      a = b;
    }

    long[] x = new long[k];
    int px = 0;

    int[][] q = ntable(m, 2);

    int[][] q1 = Arrays.copyOf(q, m);
    int[][] q2 = Arrays.copyOf(q, m);
    Arrays.sort(q1, (o1, o2) -> o1[0] - o2[0]);
    Arrays.sort(q2, (o1, o2) -> o1[1] - o2[1]);

    int p1 = 0, p2 = 0;

    long[] ret = new long[n];
    for (int i = 0; i < n; i++) {
      while (p1 < m && q1[p1][0] == i) {
        for (int j = 0; j < k; j++) {
          x[(px + j) % k] += a[j];
          x[(px + j) % k] %= mod;
        }
        p1++;
      }

      while (p2 < m && q2[p2][1] == i) {
        for (int j = 0; j < k; j++) {
          int d = q2[p2][1] - q2[p2][0];
          x[(px + j) % k] += mod - a[j + d];
          x[(px + j) % k] %= mod;
        }
        p2++;
      }

      ret[i] += x[px];

      long nex = 0;
      for (int j = 0; j < k; j++) {
        nex += x[(px + j) % k] * c[k - j - 1] % mod;
        nex %= mod;
      }
      x[px++] = nex;
      px %= k;
    }

    for (long v : ret) {
      out.println(v);
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
