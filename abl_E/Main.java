import java.util.*;

/**
 * TODO: verify {@link LazySegTree#maxRight} and {@link LazySegTree#minLeft}
 * 
 * @verified https://atcoder.jp/contests/practice2/tasks/practice2_k
 */

abstract class LazySegTreeFast {
  final int MAX;

  final int N;
  final int Log;
  final long[] E;
  final long[] Id;

  final long[][] Dat;
  final long[][] Laz;

  private final int sLen;
  private final int fLen;

  public abstract void op(long[] s1, long[] s2, long[] ret);

  public abstract void composite(long[] f1, long[] f2, long[] ret);

  public abstract void mapping(long[] f, long[] s, long[] ret);

  public LazySegTreeFast(int n, long[] e, long[] id) {
    this.MAX = n;
    int k = 1;
    while (k < n)
      k <<= 1;
    this.N = k;
    this.Log = Integer.numberOfTrailingZeros(N);

    this.E = e;
    this.Id = id;
    sLen = e.length;
    fLen = id.length;
    this.Dat = new long[N << 1][sLen];
    this.Laz = new long[N][fLen];

    for (int i = 0; i < this.Dat.length; i++) {
      for (int j = 0; j < sLen; j++) {
        this.Dat[i][j] = e[j];
      }
    }
    for (int i = 0; i < this.Laz.length; i++) {
      for (int j = 0; j < fLen; j++) {
        this.Laz[i][j] = id[j];
      }
    }
  }

  public LazySegTreeFast(long[][] dat, long[] e, long[] id) {
    this(dat.length, e, id);
    build(dat);
  }

  private void build(long[][] dat) {
    int l = dat.length;
    for (int i = 0; i < l; i++) {
      for (int j = 0; j < sLen; j++) {
        this.Dat[i + N][j] = dat[i][j];
      }
    }
    for (int i = N - 1; i > 0; i--) {
      op(Dat[i << 1 | 0], Dat[i << 1 | 1], Dat[i]);
    }
  }

  private void push(int k) {
    if (Arrays.equals(Laz[k], Id))
      return;
    int lk = k << 1 | 0, rk = k << 1 | 1;
    mapping(Laz[k], Dat[lk], Dat[lk]);
    mapping(Laz[k], Dat[rk], Dat[rk]);

    if (lk < N) {
      composite(Laz[k], Laz[lk], Laz[lk]);
    }
    if (rk < N) {
      composite(Laz[k], Laz[rk], Laz[rk]);
    }
    System.arraycopy(Id, 0, Laz[k], 0, fLen);
  }

  private void pushTo(int k) {
    for (int i = Log; i > 0; i--)
      push(k >> i);
  }

  private void pushTo(int lk, int rk) {
    for (int i = Log; i > 0; i--) {
      if (((lk >> i) << i) != lk)
        push(lk >> i);
      if (((rk >> i) << i) != rk)
        push(rk >> i);
    }
  }

  private void updateFrom(int k) {
    k >>= 1;
    while (k > 0) {
      op(Dat[k << 1 | 0], Dat[k << 1 | 1], Dat[k]);
      k >>= 1;
    }
  }

  private void updateFrom(int lk, int rk) {
    for (int i = 1; i <= Log; i++) {
      if (((lk >> i) << i) != lk) {
        int lki = lk >> i;
        op(Dat[lki << 1 | 0], Dat[lki << 1 | 1], Dat[lki]);
      }
      if (((rk >> i) << i) != rk) {
        int rki = (rk - 1) >> i;
        op(Dat[rki << 1 | 0], Dat[rki << 1 | 1], Dat[rki]);
      }
    }
  }

  public void set(int p, long[] x) {
    assert (0 <= p && p <= MAX);
    p += N;
    pushTo(p);
    Dat[p] = x;
    updateFrom(p);
  }

  public long[] get(int p) {
    assert (0 <= p && p <= MAX);
    p += N;
    pushTo(p);
    return Dat[p];
  }

  public long[] prod(int l, int r) {
    assert (l <= r);
    assert (0 <= l && l < MAX);
    assert (0 <= r && r < MAX);
    if (l == r)
      return E;
    l += N;
    r += N;
    pushTo(l, r);
    long[] sum = Arrays.copyOf(E, sLen);
    while (l < r) {
      if ((l & 1) == 1) {
        op(sum, Dat[l++], sum);
      }
      if ((r & 1) == 1) {
        op(Dat[--r], sum, sum);
      }
      l >>= 1;
      r >>= 1;
    }
    return sum;
  }

  public long[] allProd() {
    return Dat[1];
  }

  public void apply(int p, long[] f) {
    assert (0 <= p && p <= MAX);
    p += N;
    pushTo(p);
    mapping(f, Dat[p], Dat[p]);
    updateFrom(p);
  }

  public void apply(int l, int r, long[] f) {
    assert (l <= r);
    assert (0 <= l && l < MAX);
    assert (0 <= r && r < MAX);
    if (l == r)
      return;
    l += N;
    r += N;
    pushTo(l, r);
    for (int l2 = l, r2 = r; l2 < r2;) {
      if ((l2 & 1) == 1) {
        mapping(f, Dat[l2], Dat[l2]);

        if (l2 < N) {
          composite(f, Laz[l2], Laz[l2]);
        }
        l2++;
      }
      if ((r2 & 1) == 1) {
        r2--;

        mapping(f, Dat[r2], Dat[r2]);

        if (r2 < N) {
          composite(f, Laz[r2], Laz[r2]);
        }
      }
      l2 >>= 1;
      r2 >>= 1;
    }
    updateFrom(l, r);
  }

  // Not verified.
  public int maxRight(int l, java.util.function.Predicate<long[]> g) {
    assert (0 <= l && l < MAX);
    if (!g.test(E)) {
      throw new IllegalArgumentException("Identity element must satisfy the condition.");
    }
    if (l == MAX)
      return MAX;
    l += N;
    pushTo(l);
    long[] sRet = new long[sLen];
    long[] sum = Arrays.copyOf(E, sLen);
    do {
      l >>= Integer.numberOfTrailingZeros(l);
      op(sum, Dat[l], sRet);
      if (!g.test(sRet)) {
        while (l < N) {
          push(l);
          l = l << 1;
          op(sum, Dat[l], sRet);
          if (g.test(sRet)) {
            System.arraycopy(sRet, 0, sum, 0, sLen);
            l++;
          }
        }
        return l - N;
      }
      op(sum, Dat[l], sRet);
      System.arraycopy(sRet, 0, sum, 0, sLen);
      l++;
    } while ((l & -l) != l);
    return MAX;
  }

  // Not verified.
  public int minLeft(int r, java.util.function.Predicate<long[]> g) {
    assert (0 <= r && r < MAX);
    if (!g.test(E)) {
      throw new IllegalArgumentException("Identity element must satisfy the condition.");
    }
    if (r == 0)
      return 0;
    r += N;
    pushTo(r - 1);
    long[] sRet = new long[sLen];
    long[] sum = Arrays.copyOf(E, sLen);
    do {
      r--;
      while (r > 1 && (r & 1) == 1)
        r >>= 1;
      op(sum, Dat[r], sRet);
      if (!g.test(sRet)) {
        while (r < N) {
          push(r);
          r = r << 1 | 1;
          op(sum, Dat[r], sRet);
          if (g.test(sRet)) {
            System.arraycopy(sRet, 0, sum, 0, sLen);
            r--;
          }
        }
        return r + 1 - N;
      }
      op(sum, Dat[r], sRet);
      System.arraycopy(sRet, 0, sum, 0, sLen);
    } while ((r & -r) != r);
    return 0;
  }
}

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int q = ni();

    long[] e = { 0, 0 };
    int mod = 998244353;
    long[][] dat = new long[n][2];
    long[] ten = new long[(n << 2) + 1];
    long[] one = new long[(n << 2) + 1];
    ten[0] = 1;
    one[0] = 0;
    for (int i = 1; i < ten.length; i++) {
      ten[i] = ten[i - 1] * 10 % mod;
      one[i] = (one[i - 1] * 10 + 1) % mod;
    }
    for (int i = 0; i < n; i++) {
      dat[i][0] = 1;
      dat[i][1] = 1;
    }

    long[] id = { 0 };
    LazySegTreeFast st = new LazySegTreeFast(dat, e, id) {
      @Override
      public void op(long[] s1, long[] s2, long[] ret) {
        long a = (s1[0] * ten[(int) s2[1]] + s2[0]) % mod;
        long b = s1[1] + s2[1];

        ret[0] = a;
        ret[1] = b;
      }

      @Override
      public void composite(long[] f1, long[] f2, long[] ret) {
        long x = f1[0] == 0 ? f2[0] : f1[0];
        ret[0] = x;
      }

      @Override
      public void mapping(long[] f, long[] s, long[] ret) {
        long x = f[0] == 0 ? s[0] : f[0] * one[(int) s[1]] % mod;
        ret[0] = x;
        ret[1] = s[1];
      }
    };

    for (int i = 0; i < q; i++) {
      int l = ni() - 1;
      int r = ni();
      int d = ni();
      st.apply(l, r, new long[] { d });
      out.println(st.prod(0, n)[0]);
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
