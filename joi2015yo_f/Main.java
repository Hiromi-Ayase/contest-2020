import java.util.*;

class SegmentTreeRMQL {
  public int M, H, N;
  public long[] st;

  public SegmentTreeRMQL(int n) {
    N = n;
    M = Integer.highestOneBit(Math.max(N - 1, 1)) << 2;
    H = M >>> 1;
    st = new long[M];
    Arrays.fill(st, 0, M, Long.MAX_VALUE);
  }

  public SegmentTreeRMQL(long[] a) {
    N = a.length;
    M = Integer.highestOneBit(Math.max(N - 1, 1)) << 2;
    H = M >>> 1;
    st = new long[M];
    for (int i = 0; i < N; i++) {
      st[H + i] = a[i];
    }
    Arrays.fill(st, H + N, M, Long.MAX_VALUE);
    for (int i = H - 1; i >= 1; i--)
      propagate(i);
  }

  public void update(int pos, long x) {
    st[H + pos] = x;
    for (int i = (H + pos) >>> 1; i >= 1; i >>>= 1)
      propagate(i);
  }

  private void propagate(int i) {
    st[i] = Math.min(st[2 * i], st[2 * i + 1]);
  }

  public long minx(int l, int r) {
    long min = Long.MAX_VALUE;
    if (l >= r)
      return min;
    while (l != 0) {
      int f = l & -l;
      if (l + f > r)
        break;
      long v = st[(H + l) / f];
      if (v < min)
        min = v;
      l += f;
    }

    while (l < r) {
      int f = r & -r;
      long v = st[(H + r) / f - 1];
      if (v < min)
        min = v;
      r -= f;
    }
    return min;
  }

  public long min(int l, int r) {
    return l >= r ? 0 : min(l, r, 0, H, 1);
  }

  private long min(int l, int r, int cl, int cr, int cur) {
    if (l <= cl && cr <= r) {
      return st[cur];
    } else {
      int mid = cl + cr >>> 1;
      long ret = Long.MAX_VALUE;
      if (cl < r && l < mid) {
        ret = Math.min(ret, min(l, r, cl, mid, 2 * cur));
      }
      if (mid < r && l < cr) {
        ret = Math.min(ret, min(l, r, mid, cr, 2 * cur + 1));
      }
      return ret;
    }
  }
}

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    long d = nl();
    long[][] p = new long[n][2];
    for (int i = 0; i < n; i++) {
      p[i][0] = nl();
      p[i][1] = nl();
    }

    int n1 = n / 2;
    int n2 = (n + 1) / 2;

    long[][] p1 = f(n1, 0, p);
    long[][] p2 = f(n2, n1, p);
    radixSort(p1);
    radixSort(p2);

    long ret = 0;
    int r = 0;
    int l = 0;

    long[] a = Arrays.stream(p2).mapToLong(v -> -v[1]).toArray();
    SegmentTreeRMQL st = new SegmentTreeRMQL(a);
    // |a+b| <=d
    // -b-d <= a <= -b+d
    int to = p2.length;
    for (int i = p1.length - 1; i >= 0; i--) {
      long[] u = p1[i];
      while (r < to && u[0] + p2[r][0] <= d) {
        r++;
      }
      while (l < to && u[0] + p2[l][0] < -d) {
        l++;
      }
      ret = Math.max(ret, u[1] - st.min(l, r));
    }
    System.out.println(ret);
  }

  private static long[][] f(int n, int offset, long[][] p) {
    int m = 1;
    for (int i = 0; i < n; i++) {
      m *= 3;
    }
    long[][] ret = new long[m][2];
    int ptr = 0;
    int to = 1;

    ret[1][0] = p[offset][0];
    ret[1][1] = p[offset][1];
    ret[2][0] = -p[offset][0];
    ret[2][1] = -p[offset][1];

    for (int i = 3; i < m; i++) {
      if (i >= to * 3) {
        to *= 3;
        ptr++;
      }

      int x = i / to;
      ret[i][0] += ret[i - x * to][0];
      ret[i][1] += ret[i - x * to][1];

      if (x == 1) {
        ret[i][0] += p[offset + ptr][0];
        ret[i][1] += p[offset + ptr][1];
      } else if (x == 2) {
        ret[i][0] -= p[offset + ptr][0];
        ret[i][1] -= p[offset + ptr][1];
      }
    }
    return ret;
  }

  public static long[][] radixSort(long[][] f) {
    return radixSort(f, f.length);
  }

  public static long[][] radixSort(long[][] f, int n) {
    long[][] to = new long[n][];
    {
      int[] b = new int[65537];
      for (int i = 0; i < n; i++)
        b[1 + (int) (f[i][0] & 0xffff)]++;
      for (int i = 1; i <= 65536; i++)
        b[i] += b[i - 1];
      for (int i = 0; i < n; i++)
        to[b[(int) (f[i][0] & 0xffff)]++] = f[i];
      long[][] d = f;
      f = to;
      to = d;
    }
    {
      int[] b = new int[65537];
      for (int i = 0; i < n; i++)
        b[1 + (int) (f[i][0] >>> 16 & 0xffff)]++;
      for (int i = 1; i <= 65536; i++)
        b[i] += b[i - 1];
      for (int i = 0; i < n; i++)
        to[b[(int) (f[i][0] >>> 16 & 0xffff)]++] = f[i];
      long[][] d = f;
      f = to;
      to = d;
    }
    {
      int[] b = new int[65537];
      for (int i = 0; i < n; i++)
        b[1 + (int) (f[i][0] >>> 32 & 0xffff)]++;
      for (int i = 1; i <= 65536; i++)
        b[i] += b[i - 1];
      for (int i = 0; i < n; i++)
        to[b[(int) (f[i][0] >>> 32 & 0xffff)]++] = f[i];
      long[][] d = f;
      f = to;
      to = d;
    }
    {
      int[] b = new int[65537];
      for (int i = 0; i < n; i++)
        b[1 + (int) (f[i][0] >>> 48 & 0xffff)]++;
      for (int i = 1; i <= 65536; i++)
        b[i] += b[i - 1];
      for (int i = 0; i < n; i++)
        to[b[(int) (f[i][0] >>> 48 & 0xffff)]++] = f[i];
      long[][] d = f;
      f = to;
      to = d;
    }
    return f;
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
