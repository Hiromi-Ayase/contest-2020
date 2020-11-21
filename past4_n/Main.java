import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int w = 6;
    int h = 18;
    char[][] map = new char[h][w];
    for (int i = 0; i < h; i++) {
      map[i] = ns();
    }

    // 0:0,0 1:0,1 2:0,2, 3:1,2, 4:1,3, 5:1,4
    long[][] dp = new long[18][1 << 12];

    outer: for (int i = 0; i < (1 << 12); i++) {
      for (int j = 0; j < 12; j++) {
        int y = j / 6;
        int x = j % 6;

        int b = (i >> j) & 1;
        char c = map[y][x];
        if (c == '?') {
          continue;
        } else {
          if (c - '0' != b)
            continue outer;
        }
      }
      if (check(i) && check2(i << 6)) {
        dp[0][i] = 1;
      }
    }

    for (int i = 1; i < h; i++) {
      for (int j = 0; j < (1 << 12); j++) {
        outer: for (int k = 0; k < (1 << 6); k++) {

          for (int s = 0; s < 6; s++) {
            int x = s % 6;

            int b = (k >> s) & 1;
            char c = i < h - 1 ? map[i + 1][x] : '0';
            if (c == '?') {
              continue;
            } else {
              if (c - '0' != b)
                continue outer;
            }
          }

          int x = (k << 6) | (j >> 6);
          int y = (k << 12) | j;
          if (check(x) && check2(y)) {
            dp[i][x] += dp[i - 1][j];
          }
        }
      }
    }
    long ret = 0;
    for (int i = 0; i < (1 << 12); i++) {
      if ((i >> 6) == 0) {
        ret += dp[h - 1][i];
      }
    }
    System.out.println(ret);
  }

  static int[][] map = new int[3][6];

  private static boolean check(int x) {
    for (int i = 0; i < 12; i++) {
      int b = (x >> i) & 1;
      map[i / 6][i % 6] = b;
    }
    for (int j = 0; j < 6; j++) {
      int cur = map[1][j];
      int one = cur + map[0][j];
      if (j > 0) {
        one += map[1][j - 1];
      }
      if (j < 5) {
        one += map[1][j + 1];
      }
      if (one >= 3 && cur == 0 || one <= 1 && cur == 1)
        return false;
    }
    return true;
  }

  private static boolean check2(int x) {
    for (int i = 0; i < 18; i++) {
      int b = (x >> i) & 1;
      map[i / 6][i % 6] = b;
    }
    for (int j = 0; j < 6; j++) {
      int cur = map[1][j];
      int one = cur + map[0][j] + map[2][j];
      if (j > 0) {
        one += map[1][j - 1];
      }
      if (j < 5) {
        one += map[1][j + 1];
      }
      if (one >= 3 && cur == 0 || one <= 2 && cur == 1)
        return false;
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
