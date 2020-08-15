
public class Main {

  private static void solve() {
    int n = ni();
    char[] s = ns();

    int cnt = 0;
    for (int i = 0; i < n; i++) {
      if (s[i] == '1') {
        cnt++;
      }
    }
    if (cnt == 1) {
      for (int i = 0; i < n; i++) {
        int d = s[i] - '0';

        if (d == 1) {
          out.println(0);
        } else {
          out.println(i == n - 1 ? 2 : 1);
        }
      }
      return;
    } else if (cnt == 0) {
      for (int i = 0; i < n; i++) {
        out.println(1);
      }
      return;
    }

    int mod1 = 0;
    int mod2 = 0;
    for (int i = 0; i < n; i++) {
      int d = s[i] - '0';
      mod1 = (mod1 * 2 + d) % (cnt - 1);
      mod2 = (mod2 * 2 + d) % (cnt + 1);
    }

    int[] a = new int[n];
    int f1 = 1;
    int f2 = 1;
    for (int i = n - 1; i >= 0; i--) {
      int d = s[i] - '0';

      if (d == 1) {
        a[i] = (mod1 - f1 + cnt - 1) % (cnt - 1);
      } else {
        a[i] = (mod2 + f2 + cnt + 1) % (cnt + 1);
      }

      f1 = f1 * 2 % (cnt - 1);
      f2 = f2 * 2 % (cnt + 1);
    }

    for (int v : a) {
      out.println(f(v));
    }
  }

  private static int f(int v) {
    int ret = 1;
    while (v > 0) {
      ret++;
      v = v % Integer.bitCount(v);
    }
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
