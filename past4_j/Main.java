import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int m = ni();

    int[] x = na(3);
    char[] s = ns();

    int[] from = new int[m * 2 + n * 4];
    int[] to = new int[m * 2 + n * 4];
    long[] w = new long[m * 2 + n * 4];
    int ptr = 0;

    for (int i = 0; i < m; i++) {
      from[ptr] = ni() - 1;
      to[ptr] = ni() - 1;
      w[ptr] = ni();
      ptr++;
      to[ptr] = from[ptr - 1];
      from[ptr] = to[ptr - 1];
      w[ptr] = w[ptr - 1];
      ptr++;
    }

    int xab = n + 0;
    int xac = n + 1;
    int xba = n + 2;
    int xbc = n + 3;
    int xca = n + 4;
    int xcb = n + 5;
    for (int i = 0; i < n; i++) {
      if (s[i] == 'A') {
        from[ptr] = i;
        to[ptr] = xab;
        w[ptr] = x[0];
        ptr++;
        from[ptr] = i;
        to[ptr] = xac;
        w[ptr] = x[1];
        ptr++;
        to[ptr] = i;
        from[ptr] = xba;
        w[ptr] = 0;
        ptr++;
        to[ptr] = i;
        from[ptr] = xca;
        w[ptr] = 0;
        ptr++;
      }
      if (s[i] == 'B') {
        from[ptr] = i;
        to[ptr] = xba;
        w[ptr] = x[0];
        ptr++;
        from[ptr] = i;
        to[ptr] = xbc;
        w[ptr] = x[2];
        ptr++;
        to[ptr] = i;
        from[ptr] = xab;
        w[ptr] = 0;
        ptr++;
        to[ptr] = i;
        from[ptr] = xcb;
        w[ptr] = 0;
        ptr++;
      }
      if (s[i] == 'C') {
        from[ptr] = i;
        to[ptr] = xca;
        w[ptr] = x[1];
        ptr++;
        from[ptr] = i;
        to[ptr] = xcb;
        w[ptr] = x[2];
        ptr++;
        to[ptr] = i;
        from[ptr] = xbc;
        w[ptr] = 0;
        ptr++;
        to[ptr] = i;
        from[ptr] = xac;
        w[ptr] = 0;
        ptr++;
      }
    }
    GraphW g = packWD(n + 6, from, to, w);

    long[] d = dijk(g.g, g.wg, 0);
    System.out.println(d[n - 1]);

  }

  public static long[] dijk(int[][] g, long[][] wg, int from) {
    int n = g.length;
    final long[] td = new long[n];
    Arrays.fill(td, Long.MAX_VALUE);
    TreeSet<Integer> q = new TreeSet<Integer>(new Comparator<Integer>() {
      public int compare(Integer a, Integer b) {
        if (td[a] - td[b] != 0)
          return Long.signum(td[a] - td[b]);
        return a - b;
      }
    });
    q.add(from);
    td[from] = 0;

    while (q.size() > 0) {
      int cur = q.pollFirst();

      for (int i = 0; i < g[cur].length; i++) {
        int next = g[cur][i];
        long nd = td[cur] + wg[cur][i];
        if (nd < td[next]) {
          q.remove(next);
          td[next] = nd;
          q.add(next);
        }
      }
    }

    return td;
  }

  public static GraphW packWD(int n, int[] from, int[] to, long[] w) {
    int[][] g = new int[n][];
    long[][] wg = new long[n][];
    int[] p = new int[n];
    for (int f : from)
      p[f]++;
    for (int i = 0; i < n; i++) {
      g[i] = new int[p[i]];
      wg[i] = new long[p[i]];
    }
    for (int i = 0; i < from.length; i++) {
      --p[from[i]];
      g[from[i]][p[from[i]]] = to[i];
      wg[from[i]][p[from[i]]] = w[i];
    }
    GraphW gw = new GraphW();
    gw.g = g;
    gw.wg = wg;
    return gw;
  }

  public static class GraphW {
    public int[][] g;
    public long[][] wg;
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
