import java.util.*;

class SegTree<S> {
  final int MAX;

  final int N;
  final java.util.function.BinaryOperator<S> op;
  final S E;

  final S[] data;

  @SuppressWarnings("unchecked")
  public SegTree(int n, java.util.function.BinaryOperator<S> op, S e) {
    this.MAX = n;
    int k = 1;
    while (k < n)
      k <<= 1;
    this.N = k;
    this.E = e;
    this.op = op;
    this.data = (S[]) new Object[N << 1];
    java.util.Arrays.fill(data, E);
  }

  public SegTree(S[] dat, java.util.function.BinaryOperator<S> op, S e) {
    this(dat.length, op, e);
    build(dat);
  }

  private void build(S[] dat) {
    int l = dat.length;
    System.arraycopy(dat, 0, data, N, l);
    for (int i = N - 1; i > 0; i--) {
      data[i] = op.apply(data[i << 1 | 0], data[i << 1 | 1]);
    }
  }

  public void set(int p, S x) {
    exclusiveRangeCheck(p);
    data[p += N] = x;
    p >>= 1;
    while (p > 0) {
      data[p] = op.apply(data[p << 1 | 0], data[p << 1 | 1]);
      p >>= 1;
    }
  }

  public S get(int p) {
    exclusiveRangeCheck(p);
    return data[p + N];
  }

  public S prod(int l, int r) {
    if (l > r) {
      throw new IllegalArgumentException(String.format("Invalid range: [%d, %d)", l, r));
    }
    inclusiveRangeCheck(l);
    inclusiveRangeCheck(r);
    S sumLeft = E;
    S sumRight = E;
    l += N;
    r += N;
    while (l < r) {
      if ((l & 1) == 1)
        sumLeft = op.apply(sumLeft, data[l++]);
      if ((r & 1) == 1)
        sumRight = op.apply(data[--r], sumRight);
      l >>= 1;
      r >>= 1;
    }
    return op.apply(sumLeft, sumRight);
  }

  public S allProd() {
    return data[1];
  }

  public int maxRight(int l, java.util.function.Predicate<S> f) {
    inclusiveRangeCheck(l);
    if (!f.test(E)) {
      throw new IllegalArgumentException("Identity element must satisfy the condition.");
    }
    if (l == MAX)
      return MAX;
    l += N;
    S sum = E;
    do {
      l >>= Integer.numberOfTrailingZeros(l);
      if (!f.test(op.apply(sum, data[l]))) {
        while (l < N) {
          l = l << 1;
          if (f.test(op.apply(sum, data[l]))) {
            sum = op.apply(sum, data[l]);
            l++;
          }
        }
        return l - N;
      }
      sum = op.apply(sum, data[l]);
      l++;
    } while ((l & -l) != l);
    return MAX;
  }

  public int minLeft(int r, java.util.function.Predicate<S> f) {
    inclusiveRangeCheck(r);
    if (!f.test(E)) {
      throw new IllegalArgumentException("Identity element must satisfy the condition.");
    }
    if (r == 0)
      return 0;
    r += N;
    S sum = E;
    do {
      r--;
      while (r > 1 && (r & 1) == 1)
        r >>= 1;
      if (!f.test(op.apply(data[r], sum))) {
        while (r < N) {
          r = r << 1 | 1;
          if (f.test(op.apply(data[r], sum))) {
            sum = op.apply(data[r], sum);
            r--;
          }
        }
        return r + 1 - N;
      }
      sum = op.apply(data[r], sum);
    } while ((r & -r) != r);
    return 0;
  }

  private void exclusiveRangeCheck(int p) {
    if (p < 0 || p >= MAX) {
      throw new IndexOutOfBoundsException(String.format("Index %d out of bounds for the range [%d, %d).", p, 0, MAX));
    }
  }

  private void inclusiveRangeCheck(int p) {
    if (p < 0 || p > MAX) {
      throw new IndexOutOfBoundsException(String.format("Index %d out of bounds for the range [%d, %d].", p, 0, MAX));
    }
  }

  // **************** DEBUG **************** //

  private int indent = 6;

  public void setIndent(int newIndent) {
    this.indent = newIndent;
  }

  @Override
  public String toString() {
    return toSimpleString();
  }

  public String toDetailedString() {
    return toDetailedString(1, 0);
  }

  private String toDetailedString(int k, int sp) {
    if (k >= N)
      return indent(sp) + data[k];
    String s = "";
    s += toDetailedString(k << 1 | 1, sp + indent);
    s += "\n";
    s += indent(sp) + data[k];
    s += "\n";
    s += toDetailedString(k << 1 | 0, sp + indent);
    return s;
  }

  private static String indent(int n) {
    StringBuilder sb = new StringBuilder();
    while (n-- > 0)
      sb.append(' ');
    return sb.toString();
  }

  public String toSimpleString() {
    StringBuilder sb = new StringBuilder();
    sb.append('[');
    for (int i = 0; i < N; i++) {
      sb.append(data[i + N]);
      if (i < N - 1)
        sb.append(',').append(' ');
    }
    sb.append(']');
    return sb.toString();
  }
}

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int k = ni();
    int[] a = na(n);

    int max = 350000;
    SegTree<Integer> st = new SegTree<>(max + 1, (o1, o2) -> Math.max(o1, o2), 0);

    for (int v : a) {
      st.set(v, st.prod(Math.max(0, v - k), Math.min(max, v + k + 1)) + 1);
    }
    System.out.println(st.prod(0, max));
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
