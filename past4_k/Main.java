import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int k = ni();
    int[][] a = new int[k][];
    int mod = (int) 1e9;

    int d = 21;

    long[][] cnt = new long[k][d];
    long[] c = new long[k];
    for (int i = 0; i < k; i++) {
      int n = ni();
      a[i] = na(n);
      for (int v : a[i]) {
        for (int j = v + 1; j < d; j++) {
          c[i] += cnt[i][j];
          c[i] %= mod;
        }
        cnt[i][v]++;
      }
    }

    int q = ni();
    int[] b = na(q);

    long[] totalCnt = new long[d];
    long ret = 0;
    for (int i : b) {
      i--;
      ret += c[i];
      ret %= mod;
      for (int v = 1; v < d; v++) {
        for (int u = v + 1; u < d; u++) {
          ret += cnt[i][v] * totalCnt[u];
          ret %= mod;
        }
      }
      for (int v = 1; v < d; v++) {
        totalCnt[v] += cnt[i][v];
        totalCnt[v] %= mod;
      }
    }
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
