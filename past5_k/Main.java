import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int target = 0;
    for (int i = 0; i < 4; i++) {
      char[] line = ns();
      for (int j = 0; j < 4; j++) {
        int h = i * 4 + j;

        char c = line[j];
        if (c == '#') {
          target |= 1 << h;
        }
      }
    }

    double[] memo = new double[1 << 16];
    Arrays.fill(memo, -1);
    memo[0] = 0;
    double ret = dfs(target, memo);
    System.out.printf("%.12f\n", ret);
  }

  private static int[][] dir = { { 0, 0 }, { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

  private static double dfs(int s, double[] memo) {
    if (memo[s] >= 0)
      return memo[s];

    int n = 16;

    double ret = Integer.MAX_VALUE;
    for (int p = 0; p < n; p++) {
      int rev = 0;

      double sum = 0;

      int y = p / 4;
      int x = p % 4;
      for (int[] d : dir) {
        int ny = y + d[0];
        int nx = x + d[1];
        int np = ny * 4 + nx;
        if (ny < 0 || ny >= 4 || nx < 0 || nx >= 4 || ((s >> np) & 1) == 0) {
          sum += 1.0 / 5;
          rev++;
        } else {
          sum += (dfs(s ^ (1 << np), memo) + 1) / 5;
        }
      }

      if (rev == 5)
        continue;

      sum *= 5.0 / (5 - rev);
      ret = Math.min(ret, sum);
    }
    memo[s] = ret;
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
