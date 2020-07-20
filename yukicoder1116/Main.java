import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

  private static int mod = 998244353;
  private static long inv2 = invl(2, mod);
  private static int[][] fif;
  private static long[] p2;
  private static int[] from;
  private static int[] to;
  private static int n;
  private static int m;
  private static int k;

  private static void solve() {
    n = ni();
    m = ni();
    from = new int[m];
    to = new int[m];

    for (int i = 0; i < m; i++) {
      from[i] = ni() - 1;
      to[i] = ni() - 1;
    }

    fif = enumFIF(n * 2, mod);
    p2 = new long[n + 1];
    p2[0] = 1;
    for (int i = 1; i <= n; i++) {
      p2[i] = p2[i - 1] * 2 % mod;
    }

    k = m * 2;
    int[][][][] cnt = new int[k + 1][m + 1][2][2];
    for (int i = 0; i < 1 << m; i++) {
      int[] v = f(i);
      if (v == null)
        continue;
      cnt[v[0]][v[1]][v[2]][v[3]]++;
    }

    long ret = 0;
    for (int i = 0; i <= k; i++) {
      for (int j = 0; j <= m; j++) {
        for (int s = 0; s < 2; s++) {
          for (int t = 0; t < 2; t++) {
            if (cnt[i][j][s][t] == 0)
              continue;
            long now = g(i, j, t == 1) * cnt[i][j][s][t] % mod;
            long x = s == 0 ? 1 : -1;

            ret += mod + x * now;
            ret %= mod;
          }
        }
      }
    }

    System.out.println(ret);
  }

  private static long g(int v, int u, boolean loop) {
    if (loop) {
      return u == 1 ? 1 : 0;
    }

    long ret = 0;
    int nlast = n - v;
    for (int i = Math.max(0, 3 - v); i <= nlast; i++) {
      long now = CX(nlast, i, mod, fif);
      now = now * p2[u] % mod;
      now = now * fif[0][i + u - 1] % mod;
      now = now * inv2 % mod;
      ret += now;
      ret %= mod;
    }
    if (ret < 0) {
      throw new RuntimeException();
    }
    return ret;
  }

  private static int[] f(int s) {
    Map<Integer, Integer> map = new HashMap<>();
    int cnt = 0;
    int[] deg = new int[k];
    DisjointSet uf = new DisjointSet(k);

    boolean loop = false;
    for (int i = 0; i < m; i++) {
      if ((s >> i & 1) == 1) {
        int a = from[i];
        int b = to[i];
        if (!map.containsKey(a)) {
          map.put(a, cnt++);
        }
        if (!map.containsKey(b)) {
          map.put(b, cnt++);
        }
        a = map.get(a);
        b = map.get(b);

        if (deg[a] == 2 || deg[b] == 2 || loop && uf.equiv(a, b)) {
          return null;
        }
        if (!loop && uf.equiv(a, b)) {
          loop = true;
        }
        deg[a]++;
        deg[b]++;

        uf.union(a, b);
      }
    }
    int v = 0;
    for (int i = 0; i < cnt; i++) {
      if (uf.upper[i] < 0) {
        v++;
      }
    }
    return new int[] { cnt, v, Integer.bitCount(s) % 2, loop ? 1 : 0 };
  }

  private static long invl(long a, long mod) {
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

  public static class DisjointSet {
    public int[] upper; // minus:num_element(root) plus:root(normal)
    // public int[] w;

    public DisjointSet(int n) {
      upper = new int[n];
      Arrays.fill(upper, -1);
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
        upper[x] += upper[y];
        upper[y] = x;
      }
      return x == y;
    }
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
