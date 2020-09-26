import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int t = ni();
    int[] primes = sieveEratosthenes(1000000);

    for (int i = 0; i < t; i++) {
      int n = ni();
      int[][] f = factor(n, primes);
      int[] d = enumDivisors(f);

      int[] ret = new int[d.length];
      int v = solve(d, f, ret);
      StringBuilder sb = new StringBuilder();
      for (int u : ret) {
        sb.append(u + " ");
      }
      System.out.println(sb.substring(0, sb.length() - 1));
      System.out.println(v);
    }
  }

  static int solve(int[] d, int[][] f, int[] ret) {
    if (f.length == 1) {
      for (int i = 0; i < ret.length; i++) {
        ret[i] = d[i];
      }
      return 0;
    } else {

      int n = d.length;
      boolean[] used = new boolean[n];

      Map<Integer, Integer> map = new HashMap<>();
      for (int i = 0; i < n; i++) {
        map.put(d[i], i);
      }

      int p = 0;
      for (int j = 0; j < f.length; j++) {
        int x = f[j][0] * f[(j + 1) % f.length][0];
        used[map.get(x)] = true;
      }

      int z = -1;
      if (f.length == 2) {
        int a = Arrays.stream(d).max().getAsInt();
        int b = f[0][0];
        int c = f[1][0];

        if (b * c != a) {
          used[map.get(a)] = true;
          z = a;
        }
      }

      for (int k = 0; k < f.length; k++) {
        int[] v = f[k];
        for (int i = 0; i < n; i++) {
          if (used[i])
            continue;
          if (d[i] % v[0] == 0) {
            ret[p++] = d[i];
            used[i] = true;
          }
        }

        if (k < f.length - 1) {
          int x = v[0] * f[k + 1][0];
          ret[p++] = x;
        } else {
          if (f.length >= 3) {
            int x = v[0] * f[0][0];
            ret[p++] = x;
          } else if (z > 0) {
            ret[p++] = z;
          }
        }
      }
      return f.length == 2 && z < 0 ? 1 : 0;
    }
  }

  public static int[] enumDivisors(int[][] f) {
    int n = 1;
    for (int j = 0; j < f.length; j++) {
      n *= f[j][1] + 1;
    }
    int[] divs = new int[n];
    int p = 1;
    divs[0] = 1;
    for (int j = 0; j < f.length; j++) {
      for (int q = p - 1; q >= 0; q--) {
        int b = divs[q];
        for (int k = 0; k < f[j][1]; k++) {
          b *= f[j][0];
          divs[p++] = b;
        }
      }
    }
    divs = Arrays.copyOfRange(divs, 1, divs.length);
    return divs;
  }

  public static int[][] factor(int n, int[] primes) {
    int[][] ret = new int[9][2];
    int rp = 0;
    for (int p : primes) {
      if (p * p > n)
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
    if (n != 1) {
      ret[rp][0] = n;
      ret[rp][1] = 1;
      rp++;
    }
    return Arrays.copyOf(ret, rp);
  }

  public static int[][][] packWU(int n, int[] from, int[] to, int[] w, int sup) {
    int[][][] g = new int[n][][];
    int[] p = new int[n];
    for (int i = 0; i < sup; i++)
      p[from[i]]++;
    for (int i = 0; i < sup; i++)
      p[to[i]]++;
    for (int i = 0; i < n; i++)
      g[i] = new int[p[i]][2];
    for (int i = 0; i < sup; i++) {
      --p[from[i]];
      g[from[i]][p[from[i]]][0] = to[i];
      g[from[i]][p[from[i]]][1] = w[i];
      --p[to[i]];
      g[to[i]][p[to[i]]][0] = from[i];
      g[to[i]][p[to[i]]][1] = w[i];
    }
    return g;
  }

  public static int gcd(int a, int b) {
    while (b > 0) {
      int c = a;
      a = b;
      b = c % b;
    }
    return a;
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

    int[] isnp = new int[(n + 1) / 32 / 2 + 1];
    int sup = (n + 1) / 32 / 2 + 1;

    int[] tprimes = { 3, 5, 7, 11, 13, 17, 19, 23, 29, 31 };
    for (int tp : tprimes) {
      ret[pos++] = tp;
      int[] ptn = new int[tp];
      for (int i = (tp - 3) / 2; i < tp << 5; i += tp)
        ptn[i >> 5] |= 1 << i;
      for (int j = 0; j < sup; j += tp) {
        for (int i = 0; i < tp && i + j < sup; i++) {
          isnp[j + i] |= ptn[i];
        }
      }
    }

    // 3,5,7
    // 2x+3=n
    int[] magic = { 0, 1, 23, 2, 29, 24, 19, 3, 30, 27, 25, 11, 20, 8, 4, 13, 31, 22, 28, 18, 26, 10, 7, 12, 21, 17, 9,
        6, 16, 5, 15, 14 };
    int h = n / 2;
    for (int i = 0; i < sup; i++) {
      for (int j = ~isnp[i]; j != 0; j &= j - 1) {
        int pp = i << 5 | magic[(j & -j) * 0x076be629 >>> 27];
        int p = 2 * pp + 3;
        if (p > n)
          break;
        ret[pos++] = p;
        if ((long) p * p > n)
          continue;
        for (int q = (p * p - 3) / 2; q <= h; q += p)
          isnp[q >> 5] |= 1 << q;
      }
    }

    return Arrays.copyOf(ret, pos);
  }

  static class DisjointSet {
    public int[] upper; // minus:num_element(root) plus:root(normal)
    // public int[] w;

    public DisjointSet(int n) {
      upper = new int[n];
      Arrays.fill(upper, -1);
      // w = new int[n];
    }

    public DisjointSet(DisjointSet ds) {
      this.upper = Arrays.copyOf(ds.upper, ds.upper.length);
    }

    public int root(int x) {
      return upper[x] < 0 ? x : (upper[x] = root(upper[x]));
    }

    public boolean equiv(int x, int y) {
      return root(x) == root(y);
    }

    public boolean union(int x, int y) {
      x = root(x);
      y = root(y);
      if (x != y) {
        if (upper[y] < upper[x]) {
          int d = x;
          x = y;
          y = d;
        }
        // w[x] += w[y];
        upper[x] += upper[y];
        upper[y] = x;
      }
      return x == y;
    }

    public int count() {
      int ct = 0;
      for (int u : upper) {
        if (u < 0)
          ct++;
      }
      return ct;
    }

    public int[][] toBucket() {
      int n = upper.length;
      int[][] ret = new int[n][];
      int[] rp = new int[n];
      for (int i = 0; i < n; i++) {
        if (upper[i] < 0)
          ret[i] = new int[-upper[i]];
      }
      for (int i = 0; i < n; i++) {
        int r = root(i);
        ret[r][rp[r]++] = i;
      }
      return ret;
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
