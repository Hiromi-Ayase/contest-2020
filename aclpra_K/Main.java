import java.util.*;
import java.util.function.BiConsumer;

interface Monoid {
  public Monoid clone();

  public void to(Monoid target);

  public void prod(Monoid other);

  public void prodTo(Monoid other);
}

interface Operator extends Monoid {
  public Operator clone();

  public void apply(Monoid target);
}

class LazySegTree {
  final int MAX;

  final int N;
  final int Log;

  final Monoid[] Dat;
  final Operator[] Laz;

  final Operator id;
  final Monoid e;

  @SuppressWarnings("unchecked")
  public LazySegTree(final int n, final Monoid e, final Operator id) {
    this.MAX = n;
    int k = 1;
    while (k < n)
      k <<= 1;
    this.N = k;
    this.Log = Integer.numberOfTrailingZeros(N);
    this.id = id;
    this.e = e;

    this.Dat = new Monoid[N << 1];
    this.Laz = new Operator[N];
    for (int i = 0; i < this.Dat.length; i++) {
      this.Dat[i] = e.clone();
    }
    for (int i = 0; i < this.Laz.length; i++) {
      this.Laz[i] = id.clone();
    }
  }

  public LazySegTree(final Monoid[] dat, final Monoid e, final Operator id) {
    this(dat.length, e, id);
    build(dat);
  }

  private void build(final Monoid[] dat) {
    final int l = dat.length;
    System.arraycopy(dat, 0, Dat, N, l);
    for (int i = N - 1; i > 0; i--) {
      Dat[i << 1 | 0].to(Dat[i]);
      Dat[i].prod(Dat[i << 1 | 1]);
    }
  }

  private void push(final int k) {
    final int lk = k << 1 | 0, rk = k << 1 | 1;
    Laz[k].apply(Dat[lk]);
    Laz[k].apply(Dat[rk]);
    if (lk < N)
      Laz[k].prodTo(Laz[lk]);
    if (rk < N)
      Laz[k].prodTo(Laz[rk]);
    id.to(Laz[k]);
  }

  private void pushTo(final int k) {
    for (int i = Log; i > 0; i--)
      push(k >> i);
  }

  private void pushTo(final int lk, final int rk) {
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
      Dat[k << 1 | 0].to(Dat[k]);
      Dat[k].prod(Dat[k << 1 | 1]);
      k >>= 1;
    }
  }

  private void updateFrom(final int lk, final int rk) {
    for (int i = 1; i <= Log; i++) {
      if (((lk >> i) << i) != lk) {
        final int lki = lk >> i;
        Dat[lki << 1 | 0].to(Dat[lki]);
        Dat[lki].prod(Dat[lki << 1 | 1]);
      }
      if (((rk >> i) << i) != rk) {
        final int rki = (rk - 1) >> i;
        Dat[rki << 1 | 0].to(Dat[rki]);
        Dat[rki].prod(Dat[rki << 1 | 1]);
      }
    }
  }

  public void set(int p, final Monoid x) {
    exclusiveRangeCheck(p);
    p += N;
    pushTo(p);
    Dat[p] = x;
    updateFrom(p);
  }

  public Monoid get(int p) {
    exclusiveRangeCheck(p);
    p += N;
    pushTo(p);
    return Dat[p];
  }

  public void prod(int l, int r, final Monoid ret) {
    if (l > r) {
      throw new IllegalArgumentException(String.format("Invalid range: [%d, %d)", l, r));
    }
    inclusiveRangeCheck(l);
    inclusiveRangeCheck(r);
    if (l == r)
      return;
    l += N;
    r += N;
    pushTo(l, r);

    while (l < r) {
      if ((l & 1) == 1)
        ret.prod(Dat[l++]);
      if ((r & 1) == 1)
        Dat[--r].prodTo(ret);
      l >>= 1;
      r >>= 1;
    }
  }

  public Monoid allProd() {
    return Dat[1];
  }

  public void apply(int p, final Operator f) {
    exclusiveRangeCheck(p);
    p += N;
    pushTo(p);
    f.apply(Dat[p]);
    updateFrom(p);
  }

  public void apply(int l, int r, final Operator f) {
    if (l > r) {
      throw new IllegalArgumentException(String.format("Invalid range: [%d, %d)", l, r));
    }
    inclusiveRangeCheck(l);
    inclusiveRangeCheck(r);
    if (l == r)
      return;
    l += N;
    r += N;
    pushTo(l, r);
    for (int l2 = l, r2 = r; l2 < r2;) {
      if ((l2 & 1) == 1) {
        f.apply(Dat[l2]);
        if (l2 < N)
          Laz[l2].prod(f);
        l2++;
      }
      if ((r2 & 1) == 1) {
        r2--;
        f.apply(Dat[r2]);
        if (r2 < N)
          Laz[r2].prod(f);
      }
      l2 >>= 1;
      r2 >>= 1;
    }
    updateFrom(l, r);
  }

  private void exclusiveRangeCheck(final int p) {
    if (p < 0 || p >= MAX) {
      throw new IndexOutOfBoundsException(String.format("Index %d is not in [%d, %d).", p, 0, MAX));
    }
  }

  private void inclusiveRangeCheck(final int p) {
    if (p < 0 || p > MAX) {
      throw new IndexOutOfBoundsException(String.format("Index %d is not in [%d, %d].", p, 0, MAX));
    }
  }
}

@SuppressWarnings("unused")
public class Main {

  private static int mod = 998244353;

  static class Vec implements Monoid {
    private final int[] a;

    public Vec(final int[] a) {
      this.a = a;
    }

    @Override
    public void to(final Monoid target) {
      final Vec o = (Vec) target;
      o.a[0] = a[0];
      o.a[1] = a[1];
    }

    @Override
    public void prod(final Monoid other) {
      final Vec o = (Vec) other;
      a[0] = (a[0] + o.a[0]) % mod;
      a[1] = a[1] + o.a[1];
    }

    @Override
    public void prodTo(final Monoid other) {
      final Vec o = (Vec) other;
      o.a[0] = (a[0] + o.a[0]) % mod;
      o.a[1] = a[1] + o.a[1];
    }

    public Monoid clone() {
      return new Vec(Arrays.copyOf(a, a.length));
    }
  }

  static class Mat implements Operator {
    private final int N;
    private final int[][] a;
    private final int[][] b;

    public Mat(final int[][] a) {
      this.a = a;
      this.N = a.length;
      b = new int[N][N];
    }

    @Override
    public void to(final Monoid target) {
      final Mat o = (Mat) target;
      for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
          o.a[i][j] = a[i][j];
        }
      }
    }

    @Override
    public void prod(final Monoid other) {
      final Mat o = (Mat) other;
      for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
          long sum = 0;
          for (int k = 0; k < N; k++) {
            sum += (long) a[i][k] * o.a[k][j] % mod;
            sum %= mod;
          }
          b[i][j] = (int) sum;
        }
      }
      for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
          a[i][j] = b[i][j];
        }
      }
    }

    @Override
    public void prodTo(final Monoid other) {
      final Mat o = (Mat) other;
      for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
          long sum = 0;
          for (int k = 0; k < N; k++) {
            sum += (long) a[i][k] * o.a[k][j] % mod;
            sum %= mod;
          }
          b[i][j] = (int) sum;
        }
      }
      for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
          o.a[i][j] = b[i][j];
        }
      }
    }

    @Override
    public void apply(final Monoid target) {
      final Vec v = (Vec) target;
      for (int i = 0; i < N; i++) {
        long sum = 0;
        for (int k = 0; k < N; k++) {
          sum += (long) a[i][k] * v.a[k] % mod;
          sum %= mod;
        }
        v.a[i] = (int) sum;
      }
    }

    public Operator clone() {
      int[][] a = new int[N][N];
      for (int i = 0; i < N; i++)
        a[i] = Arrays.copyOf(this.a[i], N);
      return new Mat(a);
    }

  }

  private static void solve() {
    final int n = ni();
    final int q = ni();
    final Vec[] a = new Vec[n];
    for (int i = 0; i < n; i++) {
      a[i] = new Vec(new int[] { ni(), 1 });
    }

    final Vec e = new Vec(new int[2]);
    final Mat id = new Mat(new int[][] { { 1, 0 }, { 0, 1 } });
    final LazySegTree st = new LazySegTree(a, e, id);

    final Mat f = new Mat(new int[][] { { 1, 0 }, { 0, 1 } });
    final Vec ret = new Vec(new int[2]);
    for (int i = 0; i < q; i++) {
      final int t = ni();
      if (t == 0) {
        final int l = ni();
        final int r = ni();
        f.a[0][0] = ni();
        f.a[0][1] = ni();
        st.apply(l, r, f);
      } else {
        final int l = ni();
        final int r = ni();

        e.to(ret);
        st.prod(l, r, ret);

        out.println(ret.a[0]);
      }
    }
  }

  public static void main(final String[] args) {
    new Thread(null, new Runnable() {
      @Override
      public void run() {
        final long start = System.currentTimeMillis();
        final String debug = args.length > 0 ? args[0] : null;
        if (debug != null) {
          try {
            is = java.nio.file.Files.newInputStream(java.nio.file.Paths.get(debug));
          } catch (final Exception e) {
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
      } catch (final Exception e) {
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

  private static int[] na(final int n) {
    final int[] a = new int[n];
    for (int i = 0; i < n; i++)
      a[i] = ni();
    return a;
  }

  private static char[] ns() {
    return next().toCharArray();
  }

  private static long[] nal(final int n) {
    final long[] a = new long[n];
    for (int i = 0; i < n; i++)
      a[i] = nl();
    return a;
  }

  private static int[][] ntable(final int n, final int m) {
    final int[][] table = new int[n][m];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        table[i][j] = ni();
      }
    }
    return table;
  }

  private static int[][] nlist(final int n, final int m) {
    final int[][] table = new int[m][n];
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

  private static void tr(final Object... o) {
    if (is != System.in)
      System.out.println(java.util.Arrays.deepToString(o));
  }
}
