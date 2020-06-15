import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

public class Main {

  private static void solve() {
    int h = ni();
    int w = ni();
    int k = ni();

    int sy = ni() - 1;
    int sx = ni() - 1;
    int gy = ni() - 1;
    int gx = ni() - 1;

    char[][] map = new char[h][];
    for (int i = 0; i < h; i++) {
      map[i] = ns();
    }

    long ret = dijk(map, sy, sx, gy, gx, h, w, k);
    System.out.println(ret >= Integer.MAX_VALUE ? -1 : ret);
  }

  public static long dijk(char[][] map, int sy, int sx, int gy, int gx, int h, int w, int k) {
    int inf = 1000000;
    int n = h * w + inf * 4;
    final long[] td = new long[n];
    Arrays.fill(td, Long.MAX_VALUE / 2);
    TreeSet<Integer> q = new TreeSet<Integer>(new Comparator<Integer>() {
      public int compare(Integer a, Integer b) {
        if (td[a] - td[b] != 0)
          return Long.signum(td[a] - td[b]);
        return a - b;
      }
    });

    for (int d = 0; d < 4; d++) {
      int from = sy * w + sx + d * inf;
      q.add(from);
      td[from] = 0;
    }

    int[][] dir = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
    while (q.size() > 0) {
      int cur = q.pollFirst();
      int cd = cur / inf;

      int y = cur % inf / w;
      int x = cur % inf % w;

      for (int nexd = 0; nexd < 4; nexd++) {
        if (nexd == cd)
          continue;

        int next = y * w + x + nexd * inf;
        long nd = (td[cur] + k - 1) / k * k;

        if (nd < td[next]) {
          q.remove(next);
          td[next] = nd;
          q.add(next);
        }
      }

      {
        int ny = y + dir[cd][0];
        int nx = x + dir[cd][1];
        if (ny < 0 || nx < 0 || ny >= h || nx >= w || map[ny][nx] == '@') {
          continue;
        }
        int next = ny * w + nx + cd * inf;
        long nd = td[cur] + 1;

        if (nd < td[next]) {
          q.remove(next);
          td[next] = nd;
          q.add(next);
        }
      }
    }

    long ret = Long.MAX_VALUE;
    for (int d = 0; d < 4; d++) {
      int v = d * inf + gy * w + gx;
      ret = Math.min(ret, (td[v] + k - 1) / k);
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
