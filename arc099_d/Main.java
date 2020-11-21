import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    char[] s = ns();
    for (int i = 0, j = n - 1; i < j; i++, j--) {
      char tmp = s[i];
      s[i] = s[j];
      s[j] = tmp;
    }

    int mod = (int) 1e9 + 7;

    long[] x = { 34781947, 12893821, 1231231, 41241241, 88881312 };
    int k = x.length;

    long[][][] hash = new long[k][][];
    for (int i = 0; i < k; i++) {
      hash[i] = hash(x[i], mod, s);
    }

    Map<String, Long> cnt = new HashMap<>();
    long ret = 0;
    for (int i = 0; i <= n; i++) {
      StringBuilder sl = new StringBuilder();
      StringBuilder sr = new StringBuilder();
      for (long[][] h : hash) {
        long l = h[0][i] * h[1][i] % mod;
        long r = (h[0][i] - h[0][n] + mod) % mod * h[1][i] % mod;
        sl.append(l);
        sl.append(":");
        sr.append(r);
        sr.append(":");
      }
      ret += cnt.getOrDefault(sr.toString(), 0L);
      cnt.putIfAbsent(sl.toString(), 0L);
      cnt.compute(sl.toString(), (key, val) -> val + 1);
    }
    System.out.println(ret);
  }

  private static long[][] hash(long x, long mod, char[] s) {
    int n = s.length;

    int p = 0;
    long[][] ret = new long[2][n + 1];
    ret[1][0] = 1;
    long invX = invl(x, mod);

    for (int i = 0; i < n; i++) {
      char c = s[i];
      long h = ret[0][i];
      long px = ret[1][i];
      if (c == '>') {
        h = h * x % mod;
        px = px * invX % mod;
      } else if (c == '<') {
        h = h * invX % mod;
        px = px * x % mod;
      } else if (c == '+') {
        h = (h + 1) % mod;
      } else if (c == '-') {
        h = (h - 1 + mod) % mod;
      }

      ret[0][i + 1] = h;
      ret[1][i + 1] = px;
    }
    return ret;
  }

  public static long invl(long a, long mod) {
    long b = mod;
    long p = 1, q = 0;
    while (b > 0) {
      long c = a / b;
      long d;
      d = a;
      a = b;
      b = d % b;
      d = p;
      p = q;
      q = d - c * q;
    }
    return p < 0 ? p + mod : p;
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
