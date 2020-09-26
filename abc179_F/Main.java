import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int q = ni();

    int ymin = n - 1;
    int xmin = n - 1;

    long[] yhist = new long[n];
    long[] xhist = new long[n];
    Arrays.fill(yhist, -1);
    Arrays.fill(xhist, -1);

    long ret = 0; // ひっくり返した数
    for (int i = 0; i < q; i++) {
      int t = ni();
      if (t == 1) {
        int x = ni() - 1;
        // put(1, x)

        long v;
        if (x < xmin) {
          xmin = x;
          v = ymin - 1;

          for (int p = x; p < n - 1 && xhist[p] < 0; p++) {
            xhist[p] = v;
          }
        } else {
          v = xhist[x];
        }
        ret += v;

      } else {
        int y = ni() - 1;
        long v;
        // put(y, 1)
        if (y < ymin) {
          ymin = y;
          v = xmin - 1;
          for (int p = y; p < n - 1 && yhist[p] < 0; p++) {
            yhist[p] = v;
          }
        } else {
          v = yhist[y];
        }
        ret += v;
      }
    }
    ret = (long) (n - 2) * (n - 2) - ret;
    System.out.println(ret);
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
