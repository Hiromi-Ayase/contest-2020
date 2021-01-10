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

    int n = h * w;
    int[][] dp = new int[1 << n][n];
    for (int[] v : dp)
      Arrays.fill(v, -1);

    int len = 0;
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        if (s[i][j] == '#') {
          len++;
          int x = i * w + j;
          dp[1 << x][x] = 100;
        }

      }
    }

    int[][] dir = { { -1, 0 }, { 1, 0 }, { 0, 1 }, { 0, -1 } };
    for (int i = 1; i < (1 << n); i++) {
      for (int j = 0; j < n; j++) {
        if (dp[i][j] < 0)
          continue;
        int y = j / w;
        int x = j % w;

        for (int[] d : dir) {
          int ny = y + d[0];
          int nx = x + d[1];

          if (ny < 0 || ny >= h || nx < 0 || nx >= w)
            continue;

          int nj = ny * w + nx;
          if (((i >> nj) & 1) == 1 || s[ny][nx] != '#')
            continue;

          dp[i | (1 << nj)][nj] = j;
        }
      }
    }

    for (int i = 1; i < (1 << n); i++) {
      for (int j = 0; j < n; j++) {
        if (Integer.bitCount(i) == len && dp[i][j] > 0) {
          System.out.println(len);

          int now = j;
          int hist = i;
          while (now != 100) {
            int nh = now / w;
            int nw = now % w;
            System.out.println((nh + 1) + " " + (nw + 1));
            int nex = dp[hist][now];
            hist = hist ^ (1 << now);
            now = nex;
          }
          return;
        }
      }
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
