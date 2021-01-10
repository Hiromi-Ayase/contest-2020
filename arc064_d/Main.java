import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int k = ni();

    int[] primes = sieveEratosthenes(100000);
    int mod = (int) 1e9 + 7;
    int[] divs = divisors(n, primes);
    int m = divs.length;
    Arrays.sort(divs);

    Map<Integer, Long> dp = new HashMap<>();
    dp.put(1, (long) k);

    for (int i = 1; i < m; i++) {
      int v = divs[i];
      int[] d = divisors(v, primes);

      long x = pow(k, (v + 1) / 2, mod);
      for (int u : d) {
        if (u == v)
          continue;
        x += mod - dp.get(u);
        x %= mod;
      }
      dp.put(v, x);
    }

    long ret = 0;
    for (var e : dp.entrySet()) {
      int d = e.getKey();
      ret += (d % 2 == 0 ? d / 2 : d) * e.getValue() % mod;
      ret %= mod;
    }
    System.out.println(ret);
  }

  private static long pow(long a, long n, long mod) {
    // a %= mod;
    long ret = 1;
    int x = 63 - Long.numberOfLeadingZeros(n);
    for (; x >= 0; x--) {
      ret = ret * ret % mod;
      if (n << 63 - x < 0)
        ret = ret * a % mod;
    }
    return ret;
  }

  public static int[] divisors(int n, int[] primes) {
    int[] div = new int[1600];
    div[0] = 1;
    int r = 1;

    for (int p : primes) {
      if (p * p > n)
        break;
      int i;
      for (i = 0; n % p == 0; n /= p, i++)
        ;
      if (i > 0) {
        for (int j = r - 1; j >= 0; j--) {
          int base = div[j];
          for (int k = 0; k < i; k++) {
            base *= p;
            div[r++] = base;
          }
        }
      }
    }
    if (n != 1) {
      for (int j = r - 1; j >= 0; j--) {
        div[r++] = div[j] * n;
      }
    }
    return Arrays.copyOf(div, r);
  }

  public static int[] sieveEratosthenes(int n) {
    if (n <= 32) {
      int[] primes = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31 };
      for (int i = 0; i < primes.length; i++) {
        if (n < primes[i]) {
          return Arrays.copyOf(primes, i);
        }
      }
      return primes;
    }

    int u = n + 32;
    double lu = Math.log(u);
    int[] ret = new int[(int) (u / lu + u / lu / lu * 1.5)];
    ret[0] = 2;
    int pos = 1;

    int[] isp = new int[(n + 1) / 32 / 2 + 1];
    int sup = (n + 1) / 32 / 2 + 1;

    int[] tprimes = { 3, 5, 7, 11, 13, 17, 19, 23, 29, 31 };
    for (int tp : tprimes) {
      ret[pos++] = tp;
      int[] ptn = new int[tp];
      for (int i = (tp - 3) / 2; i < tp << 5; i += tp)
        ptn[i >> 5] |= 1 << (i & 31);
      for (int i = 0; i < tp; i++) {
        for (int j = i; j < sup; j += tp)
          isp[j] |= ptn[i];
      }
    }

    // 3,5,7
    // 2x+3=n
    int[] magic = { 0, 1, 23, 2, 29, 24, 19, 3, 30, 27, 25, 11, 20, 8, 4, 13, 31, 22, 28, 18, 26, 10, 7, 12, 21, 17, 9,
        6, 16, 5, 15, 14 };
    int h = n / 2;
    for (int i = 0; i < sup; i++) {
      for (int j = ~isp[i]; j != 0; j &= j - 1) {
        int pp = i << 5 | magic[(j & -j) * 0x076be629 >>> 27];
        int p = 2 * pp + 3;
        if (p > n)
          break;
        ret[pos++] = p;
        for (int q = pp; q <= h; q += p)
          isp[q >> 5] |= 1 << (q & 31);
      }
    }

    return Arrays.copyOf(ret, pos);
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
