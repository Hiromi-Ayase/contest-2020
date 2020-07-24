import java.util.Arrays;
import java.util.TreeSet;

public class Main {

  private static void solve() {
    int h = ni();
    int w = ni();
    char[][] map = new char[h][w];

    int[] s = null;
    int[] g = null;
    for (int i = 0; i < h; i++) {
      map[i] = ns();

      for (int j = 0; j < w; j++) {
        if (map[i][j] == 's') {
          s = new int[] { i, j };
        } else if (map[i][j] == 'g') {
          g = new int[] { i, j };
        }
      }
    }

    long[][] d = dijkl(map, s);
    if (d[g[0]][g[1]] <= 2) {
      System.out.println("YES");
    } else {
      System.out.println("NO");
    }
  }

  public static long[][] dijkl(char[][] map, int[] from) {
    int h = map.length;
    int w = map[0].length;
    long[][] td = new long[h][w];

    for (long[] v : td)
      Arrays.fill(v, Long.MAX_VALUE / 2);
    TreeSet<int[]> q = new TreeSet<>((o1, o2) -> {
      int d = Long.signum(td[o1[0]][o1[1]] - td[o2[0]][o2[1]]);
      if (d != 0) {
        return d;
      } else {
        return o1[0] == o2[0] ? o1[1] - o2[1] : o1[0] - o2[0];
      }
    });
    q.add(from);
    td[from[0]][from[1]] = 0;

    int[][] dir = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
    while (q.size() > 0) {
      int[] cur = q.pollFirst();
      for (int[] d : dir) {
        int ny = d[0] + cur[0];
        int nx = d[1] + cur[1];
        if (nx < 0 || ny < 0 || nx >= w || ny >= h) {
          continue;
        }

        long nd = td[cur[0]][cur[1]] + (map[ny][nx] == '#' ? 1 : 0);
        if (nd < td[ny][nx]) {
          int[] next = { ny, nx };
          q.remove(next);
          td[ny][nx] = nd;
          q.add(next);
        }
      }
    }

    return td;
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
