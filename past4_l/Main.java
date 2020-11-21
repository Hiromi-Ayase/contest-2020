import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int q = ni();

    long[] h = nal(n);

    Map<Long, Integer> cnt0 = new HashMap<>();
    Map<Long, Integer> cnt1 = new HashMap<>();

    for (int i = 0; i < n - 1; i += 2) {
      long d = h[i] - h[i + 1];
      cnt0.putIfAbsent(d, 0);
      cnt0.compute(d, (key, val) -> val + 1);
    }

    for (int i = 1; i < n - 1; i += 2) {
      long d = h[i + 1] - h[i];
      cnt1.putIfAbsent(d, 0);
      cnt1.compute(d, (key, val) -> val + 1);
    }

    long diff = 0;
    for (int i = 0; i < q; i++) {
      int t = ni();
      if (t == 1) {
        diff += ni();
      } else if (t == 2) {
        diff -= ni();
      } else {
        int u = ni() - 1;
        long v = ni();

        if (u % 2 == 0) {
          if (u >= 1) {
            long d = h[u] - h[u - 1];
            cnt1.compute(d, (key, val) -> val - 1);
            long nd = (h[u] + v) - h[u - 1];
            cnt1.putIfAbsent(nd, 0);
            cnt1.compute(nd, (key, val) -> val + 1);
          }
          if (u < n - 1) {
            long d = h[u] - h[u + 1];
            cnt0.compute(d, (key, val) -> val - 1);
            long nd = (h[u] + v) - h[u + 1];
            cnt0.putIfAbsent(nd, 0);
            cnt0.compute(nd, (key, val) -> val + 1);
          }
        } else {
          if (u < n - 1) {
            long d = h[u + 1] - h[u];
            cnt1.compute(d, (key, val) -> val - 1);
            long nd = h[u + 1] - (h[u] + v);
            cnt1.putIfAbsent(nd, 0);
            cnt1.compute(nd, (key, val) -> val + 1);
          }
          if (u >= 1) {
            long d = h[u - 1] - h[u];
            cnt0.compute(d, (key, val) -> val - 1);
            long nd = h[u - 1] - (h[u] + v);
            cnt0.putIfAbsent(nd, 0);
            cnt0.compute(nd, (key, val) -> val + 1);
          }
        }
        h[u] += v;
      }
      long ret = cnt0.getOrDefault(-diff, 0) + cnt1.getOrDefault(-diff, 0);
      System.out.println(ret);
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
