import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    char[] s = ns();
    int[] a = new int[n];
    for (int i = 0; i < n; i++) {
      a[i] = i;
    }

    while (true) {
      char[] t = new char[n];
      char[] revT = new char[n];
      for (int i = 0; i < n; i++) {
        t[i] = s[a[i]];
        revT[n - i - 1] = s[a[i]];
      }

      if (!Arrays.equals(s, t) && !Arrays.equals(s, revT)) {
        System.out.println(t);
        return;
      }

      if (!next(a))
        break;
    }
    System.out.println("None");
  }

  public static boolean next(int[] a) {
    int n = a.length;
    int i;
    for (i = n - 2; i >= 0 && a[i] >= a[i + 1]; i--)
      ;
    if (i == -1)
      return false;
    int j;
    for (j = i + 1; j < n && a[i] < a[j]; j++)
      ;
    int d = a[i];
    a[i] = a[j - 1];
    a[j - 1] = d;
    for (int p = i + 1, q = n - 1; p < q; p++, q--) {
      d = a[p];
      a[p] = a[q];
      a[q] = d;
    }
    return true;
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
