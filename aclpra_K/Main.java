import java.util.*;

class SegmentTreeMatrix {
  public int M, H, N;
  public int[][][] node;
  public static int mod = 998244353;
  public static long BIG = 8L * mod * mod;
  public static int S = 2;

  public SegmentTreeMatrix(int[] h) {
    N = h.length;
    M = Integer.highestOneBit(Math.max(N - 1, 1)) << 2;
    H = M >>> 1;

    node = new int[M][][];
    for (int i = 0; i < N; i++) {
      node[H + i] = new int[S][S];
      // TODO

    }

    for (int i = H - 1; i >= 1; i--)
      propagate(i);
  }

  private void propagate(int cur) {
    node[cur] = prop2(node[2 * cur], node[2 * cur + 1], node[cur]);
  }

  private int[][] prop2(int[][] L, int[][] R, int[][] C) {
    if (L != null && R != null) {
      C = mul(R, L, C, mod);
      return C;
    } else if (L != null) {
      return prop1(L, C);
    } else if (R != null) {
      return prop1(R, C);
    } else {
      return null;
    }
  }

  private int[][] prop1(int[][] L, int[][] C) {
    if (C == null) {
      // C = L; // read only
      C = new int[S][];
      for (int i = 0; i < S; i++) {
        C[i] = Arrays.copyOf(L[i], S);
      }
    } else {
      for (int i = 0; i < S; i++) {
        C[i] = Arrays.copyOf(L[i], S);
      }
    }
    return C;
  }

  public void update(int pos) {
    // TODO
    for (int i = H + pos >>> 1; i >= 1; i >>>= 1)
      propagate(i);
  }

  public int[] apply(int l, int r, int[] v) {
    return apply(l, r, 0, H, 1, v);
  }

  protected int[] apply(int l, int r, int cl, int cr, int cur, int[] v) {
    if (l <= cl && cr <= r) {
      return mul(node[cur], v, mod);
    } else {
      int mid = cl + cr >>> 1;
      if (cl < r && l < mid) {
        v = apply(l, r, cl, mid, 2 * cur, v);
      }
      if (mid < r && l < cr) {
        v = apply(l, r, mid, cr, 2 * cur + 1, v);
      }
      return v;
    }
  }

  public static int[] mul(int[][] A, int[] v, int mod) {
    int m = A.length;
    int n = v.length;
    int[] w = new int[m];
    for (int i = 0; i < m; i++) {
      long sum = 0;
      for (int k = 0; k < n; k++) {
        sum += (long) A[i][k] * v[k];
        if (sum >= BIG)
          sum -= BIG;
      }
      w[i] = (int) (sum % mod);
    }
    return w;
  }

  public static int[][] mul(int[][] A, int[][] B, int[][] C, int mod) {
    assert A[0].length == B.length;
    int m = A.length;
    int n = A[0].length;
    int o = B[0].length;
    if (C == null)
      C = new int[m][o];
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < o; j++) {
        long sum = 0;
        for (int k = 0; k < n; k++) {
          sum += (long) A[i][k] * B[k][j];
          if (sum >= BIG)
            sum -= BIG;
        }
        sum %= mod;
        C[i][j] = (int) sum;
      }
    }
    return C;
  }
}

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int q = ni();

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
