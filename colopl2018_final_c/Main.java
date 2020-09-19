import java.util.*;

class EnvelopeLinear {
  public static final long INF = Integer.MIN_VALUE;

  public long[] xs;
  public long[] intercepts, slopes;
  public int p;

  public EnvelopeLinear(int n) {
    xs = new long[n];
    intercepts = new long[n];
    slopes = new long[n];
    p = 0;
  }

  public void clear() {
    p = 0;
  }

  public void add(long slope, long intercept) {
    assert p == 0 || slopes[p - 1] >= slope;
    while (p > 0) {
      int i = p - 1;
      if (intercept + xs[i] * slope <= intercepts[i] + xs[i] * slopes[i]) { // x=xs[i]
        p--;
        continue;
      }
      if (slope == slopes[i]) {
        if (intercept >= intercepts[i]) {
          return;
        } else {
          p--;
          continue;
        }
      }
      // y=sx+i vs y=Sx+I
      // sx+i=Sx+I
      // x=(i-I)/(S-s)
      long num = intercept - intercepts[i];
      long den = slopes[i] - slope;
      long nx = num < 0 ? num / den : (num + den - 1) / den;
      xs[p] = nx;
      intercepts[p] = intercept;
      slopes[p] = slope;
      p++;
      return;
    }

    xs[p] = INF;
    intercepts[p] = intercept;
    slopes[p] = slope;
    p++;
  }

  public int argmin(int x) {
    if (p <= 0)
      return -1;
    int ind = Arrays.binarySearch(xs, 0, p, x);
    if (ind < 0)
      ind = -ind - 2;
    return ind;
  }

  public long min(long x) {
    if (p <= 0)
      return Long.MIN_VALUE / 5;
    int ind = Arrays.binarySearch(xs, 0, p, x);
    if (ind < 0)
      ind = -ind - 2;
    return slopes[ind] * x + intercepts[ind];
  }
}

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    long[] a = nal(n);
    EnvelopeLinear eh = new EnvelopeLinear(n);

    for (int j = 1; j <= n; j++) {
      eh.add(-2 * j, a[j - 1] + (long) j * j);
    }

    for (int i = 1; i <= n; i++) {
      out.println(eh.min(i) + (long) i * i);
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
