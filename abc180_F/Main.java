import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static int mod = (int) 1e9 + 7;
  private static int[][] fif = enumFIF(300, mod);
  private static long inv2 = invl(2, mod);
  private static long[][] c;
  static {
    c = new long[301][301];
    for (int i = 0; i <= 300; i++) {
      for (int j = 0; j <= 300; j++) {
        c[i][j] = CX(i, j, mod, fif);
      }
    }
  }

  private static void solve() {
    int n = ni();
    int m = ni();
    int l = ni();

    // dp[使用済み点][使用済み辺]

    long ret = (f(n, m, l) - f(n, m, l - 1) + mod) % mod;
    System.out.println(ret);
  }

  private static long f(int n, int m, int l) {
    long[][] dp = new long[n + 1][m + 1];
    dp[0][0] = 1;
    for (int vcnt = 0; vcnt < n; vcnt++) {
      for (int ecnt = 0; ecnt <= m; ecnt++) {
        // 1point
        dp[vcnt + 1][ecnt] += dp[vcnt][ecnt];
        dp[vcnt + 1][ecnt] %= mod;

        for (int k = 2; k <= l && k + vcnt <= n; k++) {
          // path
          if (k + ecnt - 1 <= m) {
            dp[vcnt + k][ecnt + k - 1] += dp[vcnt][ecnt] * c[n - vcnt - 1][k - 1] % mod * fif[0][k] % mod * inv2 % mod;
            dp[vcnt + k][ecnt + k - 1] %= mod;
          }
          // loop
          if (k + ecnt <= m) {
            if (2 <= l && k == 2) {
              dp[vcnt + k][ecnt + k] += dp[vcnt][ecnt] * (n - vcnt - 1) % mod;
            } else {
              dp[vcnt + k][ecnt + k] += dp[vcnt][ecnt] * c[n - vcnt - 1][k - 1] % mod * fif[0][k - 1] % mod * inv2
                  % mod;
            }
            dp[vcnt][ecnt] %= mod;
          }
        }
      }
    }
    return dp[n][m];
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

  public static long CX(long n, long r, int p, int[][] fif) {
    if (n < 0 || r < 0 || r > n)
      return 0;
    int np = (int) (n % p), rp = (int) (r % p);
    if (np < rp)
      return 0;
    if (n == 0 && r == 0)
      return 1;
    int nrp = np - rp;
    if (nrp < 0)
      nrp += p;
    return (long) fif[0][np] * fif[1][rp] % p * fif[1][nrp] % p * CX(n / p, r / p, p, fif) % p;
  }

  public static int[][] enumFIF(int n, int mod) {
    int[] f = new int[n + 1];
    int[] invf = new int[n + 1];
    f[0] = 1;
    for (int i = 1; i <= n; i++) {
      f[i] = (int) ((long) f[i - 1] * i % mod);
    }
    long a = f[n];
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
    invf[n] = (int) (p < 0 ? p + mod : p);
    for (int i = n - 1; i >= 0; i--) {
      invf[i] = (int) ((long) invf[i + 1] * (i + 1) % mod);
    }
    return new int[][] { f, invf };
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
