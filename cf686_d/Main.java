import java.util.*;

@SuppressWarnings("unused")
public class Main {
  static class FactorL {

    static long[][] factorX(long n, int[] primes) {
      long[][] ret = new long[20][2];
      int rp = 0;
      for (int p : primes) {
        if ((long) p * p > n)
          break;
        int i;
        for (i = 0; n % p == 0; n /= p, i++)
          ;
        if (i > 0) {
          ret[rp][0] = p;
          ret[rp][1] = i;
          rp++;
        }
      }
      if (n == 1)
        return Arrays.copyOf(ret, rp);

      // P^2
      long sq = (long) Math.sqrt(n);
      for (long u = Math.max(2, sq - 2); u <= sq + 2; u++) {
        if (u * u == n) {
          ret[rp][0] = u;
          ret[rp][1] = 2;
          rp++;
          return Arrays.copyOf(ret, rp);
        }
      }

      // Prime
      if (doMirrorRabin(n)) {
        ret[rp][0] = n;
        ret[rp][1] = 1;
        rp++;
        return Arrays.copyOf(ret, rp);
      }

      // P*Q
      long f = rho(n);
      if (f == -1)
        throw new ArithmeticException();
      if (f > n / f)
        f = n / f;
      ret[rp][0] = f;
      ret[rp][1] = 1;
      rp++;
      ret[rp][0] = n / f;
      ret[rp][1] = 1;
      rp++;
      return Arrays.copyOf(ret, rp);
    }

    public static long mul(long a, long b, long mod) {
      a %= mod;
      long ret = 0;
      int x = 63 - Long.numberOfLeadingZeros(b);
      for (; x >= 0; x--) {
        ret += ret;
        if (ret >= mod)
          ret -= mod;
        if (b << ~x < 0) {
          ret += a;
          if (ret >= mod)
            ret -= mod;
        }
      }
      return ret;
    }

    public static boolean doMirrorRabin(long n) {
      // int[] P = {2, 7, 61}; // n<4759123141
      int[] P = { 2, 3, 5, 7, 11, 13, 17, 19, 23 }; // n=long
      int s = Long.numberOfTrailingZeros(n - 1);
      long d = n - 1 >> s;
      outer: for (int a : P) {
        if (a >= n)
          continue;

        long mul = a;
        long ad = 1;
        for (long e = d; e > 0; e >>>= 1) {
          if ((e & 1) == 1)
            ad = mul(ad, mul, n);
          mul = mul(mul, mul, n);
        }
        if (ad == 1)
          continue;

        for (int r = 0; r < s; r++) {
          if (ad == n - 1)
            continue outer;
          ad = mul(ad, ad, n);
        }
        return false;
      }
      return true;
    }

    static long rho(long n) {
      Random gen = new Random();
      for (int u = 0; u < 100; u++) {
        long ran = (gen.nextLong() & Long.MAX_VALUE) % n;
        long x = 2, y = 2, d = 1;
        while (d == 1) {
          x = (mul(x, x, n) + ran) % n;
          y = (mul(y, y, n) + ran) % n;
          y = (mul(y, y, n) + ran) % n;
          d = gcd(Math.abs(x - y), n);
        }
        if (d < n)
          return d;
      }
      return -1;
    }

    public static long gcd(long a, long b) {
      while (b > 0) {
        long c = a;
        a = b;
        b = c % b;
      }
      return a;
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
      int[] magic = { 0, 1, 23, 2, 29, 24, 19, 3, 30, 27, 25, 11, 20, 8, 4, 13, 31, 22, 28, 18, 26, 10, 7, 12, 21, 17,
          9, 6, 16, 5, 15, 14 };
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
  }

  private static void solve() {
    int t = ni();
    for (int i = 0; i < t; i++) {
      long n = nl();
      long[] a = solve(n);
      StringBuilder sb = new StringBuilder();
      for (long v : a) {
        sb.append(v + " ");
      }
      out.println(a.length);
      out.println(sb.substring(0, sb.length() - 1));
    }
  }

  static int[] primes = FactorL.sieveEratosthenes(1000000);

  private static long[] solve(long n) {
    long[][] f = FactorL.factorX(n, primes);

    Arrays.sort(f, (o1, o2) -> Long.signum(o2[1] - o1[1]));
    int k = (int) f[0][1];
    long[] ret = new long[k];
    ret[k - 1] = n;

    for (int i = 0; i < k - 1; i++) {
      ret[k - 1] /= f[0][0];
      ret[i] = f[0][0];
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
