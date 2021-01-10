import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int h = ni();
    int w = ni();
    char[][] s = new char[h][w];
    for (int i = 0; i < h; i++) {
      s[i] = ns();
    }
    char[][] t = new char[h][w];
    for (int i = 0; i < h; i++) {
      t[i] = ns();
    }

    int m = Math.max(h, w);

    for (int k = 0; k < 4; k++) {
      for (int dx = -m; dx <= m; dx++) {
        for (int dy = -m; dy <= m; dy++) {
          if (check(t, s, dx, dy)) {
            System.out.println("Yes");
            return;
          }
        }
      }
      t = rotate(t);
    }
    System.out.println("No");
  }

  private static boolean check(char[][] t, char[][] s, int dx, int dy) {
    int h = s.length;
    int w = s[0].length;
    for (int i = 0; i < t.length; i++) {
      for (int j = 0; j < t[0].length; j++) {
        if (t[i][j] == '#') {
          int ni = i + dy;
          int nj = j + dx;

          if (ni < 0 || ni >= h || nj < 0 || nj >= w)
            return false;
        }
      }
    }

    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        int ni = i - dy;
        int nj = j - dx;
        if (ni < 0 || ni >= t.length || nj < 0 || nj >= t[0].length)
          continue;

        if (t[ni][nj] == '#' && s[i][j] == '#') {
          return false;
        }
      }
    }
    return true;
  }

  private static char[][] rotate(char[][] t) {
    int h = t.length;
    int w = t[0].length;
    char[][] ret = new char[w][h];
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        ret[j][i] = t[i][w - j - 1];
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
