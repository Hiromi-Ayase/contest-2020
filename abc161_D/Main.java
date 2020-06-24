import java.util.Arrays;

public class Main {

  private static void solve() {
    int k = ni();

    int[] a = new int[30];
    a[0] = 0;
    int d = 1;

    for (int i = 0; i < k; i++) {
      for (int j = 0; j <= d; j++) {
        if (j == d - 1 && a[j] == 9) {
          for (int s = 0; s < d; s++) {
            a[s] = 0;
          }
          a[d] = 1;
          d++;
          break;
        } else if (j == d - 1 || a[j] < 9 && Math.abs((a[j] + 1) - a[j + 1]) <= 1) {
          a[j]++;
          for (int s = j - 1; s >= 0; s--) {
            a[s] = Math.max(0, a[s + 1] - 1);
          }
          break;
        }
      }

      // System.err.println(i + " " + Arrays.toString(a));
    }

    for (int i = 0, j = d - 1; i < j; i++, j--) {
      int tmp = a[i];
      a[i] = a[j];
      a[j] = tmp;
    }

    for (int i = 0; i < d; i++) {
      out.print(a[i]);
    }
    out.println();
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
