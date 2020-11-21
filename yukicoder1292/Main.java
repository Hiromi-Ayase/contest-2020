import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    char[] s = ns();
    Set<Long> set = new HashSet<>();
    set.add(0L);

    long x = 0;
    long y = 0;

    for (char d : s) {
      long mx = (x % 3 + 3) % 3;
      long mx2 = (x % 6 + 6) % 6;
      long my = (y % 2 + 2) % 2;

      if (d == 'a') {
        if (mx == 0) {
          x++;
        } else if (mx == 1) {
          x--;
        } else if (mx2 == 2 && my == 0 || mx2 == 5 && my == 1) {
          y++;
        } else {
          y--;
        }
      } else if (d == 'b') {
        if (mx == 0) {
          x--;
        } else if (mx == 2) {
          x++;
        } else if (mx2 == 1 && my == 0 || mx2 == 4 && my == 1) {
          y--;
        } else {
          y++;
        }
      } else if (d == 'c') {
        if (mx == 1) {
          x++;
        } else if (mx == 2) {
          x--;
        } else if (mx2 == 0 && my == 0 || mx2 == 3 && my == 1) {
          y++;
        } else {
          y--;
        }
      }

      long h = x * 100000000000L + y;
      set.add(h);
    }
    System.out.println(set.size());
  }

  public static double[] symm(double ax, double ay, double bx, double by, double ux, double uy) {
    double eps = 1E-10;
    double aa = by - ay;
    double bb = -(bx - ax);
    double cc = bx - ax;
    double dd = by - ay;
    double xx = (uy - 2 * ay) * (bx - ax) - (ux - 2 * ax) * (by - ay);
    double yy = ux * (bx - ax) + uy * (by - ay);
    double det = aa * dd - bb * cc;
    if (Math.abs(det) < eps)
      return null;
    double t = (dd * xx - bb * yy) / det;
    double u = (-cc * xx + aa * yy) / det;
    return new double[] { t, u };
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
