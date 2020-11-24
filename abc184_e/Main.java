import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int h = ni();
    int w = ni();
    int INF = 3000;
    char[][] map = new char[h + 2][w + 2];
    Map<Character, Set<Integer>> tele = new HashMap<>();
    for (int i = 0; i < 26; i++) {
      char c = (char) ('a' + i);
      tele.put(c, new HashSet<>());
    }

    int[] s = null;
    int[] g = null;
    for (int i = 1; i <= h; i++) {
      char[] line = ns();
      for (int j = 1; j <= w; j++) {
        char c = line[j - 1];
        map[i][j] = line[j - 1];
        if (c == 'S') {
          s = new int[] { i, j };
        } else if (c == 'G') {
          g = new int[] { i, j };
        } else if ('a' <= c && c <= 'z') {
          tele.get(c).add(i * INF + j);
        }
      }
    }
    Deque<Integer> q = new ArrayDeque<>();
    int[][] dir = { { 1, 0 }, { 0, 1 }, { 0, -1 }, { -1, 0 } };
    int[] td = new int[INF * INF];
    Arrays.fill(td, Integer.MAX_VALUE);
    q.add(s[0] * INF + s[1]);
    td[s[0] * INF + s[1]] = 0;
    while (q.size() > 0) {
      int cur = q.pollFirst();
      int cy = cur / INF;
      int cx = cur % INF;
      char c = map[cy][cx];

      for (int[] dd : dir) {
        int ny = dd[0] + cy;
        int nx = dd[1] + cx;
        char nc = map[cy][cx];
        if (nc == '#' || nc == 0)
          continue;

        int next = ny * INF + nx;
        int nd = td[cur] + 1;
        if (nd < td[next]) {
          td[next] = nd;
          q.addLast(next);
        }
      }

      if ('a' <= c && c <= 'z') {
        for (int nex : tele.get(c)) {
          int ny = nex / INF;
          int nx = nex % INF;
          int next = ny * INF + nx;

          int nd = td[cur] + 1;
          if (nd < td[next]) {
            td[next] = nd;
            q.addLast(next);
          }
        }
        tele.get(c).clear();
      }
    }
    int ret = td[g[0] * INF + g[1]];
    System.out.println(ret >= Integer.MAX_VALUE / 2 ? -1 : ret);
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
