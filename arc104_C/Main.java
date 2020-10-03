import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int[][] p = ntable(n, 2);
    int[] in = new int[n * 2];
    int[] out = new int[n * 2];
    Arrays.fill(in, -1);
    Arrays.fill(out, -1);
    int wild = 0;
    for (int i = 0; i < n; i++) {
      if (p[i][0] >= 0) {
        int s = --p[i][0];
        if (in[s] >= 0 || out[s] >= 0) {
          System.out.println("No");
          return;
        }
        in[s] = i;
      }
      if (p[i][1] >= 0) {
        int s = --p[i][1];
        if (in[s] >= 0 || out[s] >= 0) {
          System.out.println("No");
          return;
        }
        out[s] = i;
      }

      if (p[i][0] < 0 && p[i][1] < 0) {
        wild++;
      }
    }

    boolean ret = dfs(0, in, out, p, wild);
    System.out.println(ret ? "Yes" : "No");
  }

  private static boolean dfs(int a, int[] in, int[] out, int[][] p, int wild) {
    int n = p.length;
    if (a == 2 * n)
      return true;

    for (int b = a + 1; b < 2 * n; b++) {
      int ret = validate(a, b, in, out, p);
      if (ret < 0 || wild < ret) {
        continue;
      }

      int na = b + (b - a);
      boolean v = dfs(na, in, out, p, wild - ret);
      if (v) {
        return true;
      }
    }
    return false;
  }

  private static int validate(int a, int b, int[] in, int[] out, int[][] p) {
    int ret = 0;
    for (int from = a; from < b; from++) {
      int to = from + (b - a);
      if (to >= in.length) {
        return -1;
      }
      int s = in[from];
      int t = out[to];

      if (s >= 0 && t >= 0 && s != t || s >= 0 && p[s][1] >= 0 && p[s][1] != to
          || t >= 0 && p[t][0] >= 0 && p[t][0] != from) {
        return -1;
      }
      if (s < 0 && t < 0) {
        ret++;
      }
    }
    return ret;
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
