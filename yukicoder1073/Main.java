
public class Main {

  private static void solve() {
    long n = nl();

    int[][] mat = { { 1, 1, 1, 1, 1, 1 }, { 1, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0, 0 },
        { 0, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 1, 0 }, };

    int[] v = { 1, 0, 0, 0, 0, 0 };

    int mod = (int) 1e9 + 7;
    int si = (int) invl(6, mod);
    for (int j = 0; j < 6; j++) {
      mat[0][j] *= si;
    }

    int[] ret = pow(mat, v, n, mod);
    System.out.println(ret[0]);
  }

  public static int[] pow(int[][] A, int[] v, long e, int mod) {
    int[][] MUL = A;
    for (int i = 0; i < v.length; i++)
      v[i] %= mod;
    for (; e > 0; e >>>= 1) {
      if ((e & 1) == 1)
        v = mul(MUL, v, mod);
      MUL = p2(MUL, mod);
    }
    return v;
  }

  public static int[] mul(int[][] A, int[] v, int mod) {
    int m = A.length;
    int n = v.length;
    int[] w = new int[m];
    for (int i = 0; i < m; i++) {
      long sum = 0;
      for (int k = 0; k < n; k++) {
        sum += (long) A[i][k] * v[k];
        sum %= mod;
      }
      w[i] = (int) sum;
    }
    return w;
  }

  // int matrix^2 (cannot ensure negative values)
  public static int[][] p2(int[][] A, int mod) {
    int n = A.length;
    int[][] C = new int[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        long sum = 0;
        for (int k = 0; k < n; k++) {
          sum += (long) A[i][k] * A[k][j];
          sum %= mod;
        }
        C[i][j] = (int) sum;
      }
    }
    return C;
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
