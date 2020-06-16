import java.util.Arrays;
import java.util.PriorityQueue;

public class Main {

  private static void solve() {
    int k = ni();
    int n = ni();
    int[][] p = ntable(n, 2);

    long ng = 0;
    long ok = Long.MAX_VALUE / n / 10;
    while (ok - ng > 1) {
      long v = (ok + ng) / 2;

      long x = 0;
      for (int i = 0; i < n; i++) {
        if (v >= p[i][0]) {
          x += (v - p[i][0]) / p[i][1] + 1;
        }
      }
      if (x >= k) {
        ok = v;
      } else {
        ng = v;
      }
    }

    long ret = 0;
    long x = 0;
    long[] b = new long[n];
    for (int i = 0; i < n; i++) {
      if (ng >= p[i][0]) {
        long y = (ng - p[i][0]) / p[i][1] + 1;

        b[i] = p[i][0] + y * p[i][1];

        long s = p[i][0] + (p[i][0] + (y - 1) * p[i][1]);
        long t = y;
        if (s % 2 == 0) {
          s /= 2;
        } else {
          t /= 2;
        }
        ret += s * t;
        x += y;
      } else {
        b[i] = p[i][0];
      }
    }

    PriorityQueue<Integer> q = new PriorityQueue<>((o1, o2) -> Long.signum(b[o1] - b[o2]));
    for (int i = 0; i < n; i++) {
      q.add(i);
    }
    while (x < k) {
      int v = q.poll();
      x++;
      ret += b[v];
      b[v] += p[v][1];
      q.add(v);
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
