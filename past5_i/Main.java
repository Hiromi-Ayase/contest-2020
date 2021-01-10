import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int m = ni();
    int k = ni();
    int[] h = na(n);
    int[] c = na(k);

    int[] from = new int[m];
    int[] to = new int[m];
    for (int i = 0; i < m; i++) {
      int a = ni() - 1;
      int b = ni() - 1;

      if (h[a] < h[b]) {
        from[i] = a;
        to[i] = b;
      } else {
        from[i] = b;
        to[i] = a;
      }
    }
    int[][] g = packD(n, from, to);
    int[] d = dijk(g, c);

    for (int i = 0; i < n; i++) {
      if (d[i] >= Integer.MAX_VALUE / 10) {
        out.println(-1);
      } else {
        out.println(d[i]);
      }
    }
  }

  public static int[] dijk(int[][] g, int[] from) {
    int n = g.length;
    final int[] td = new int[n];
    Arrays.fill(td, Integer.MAX_VALUE);
    TreeSet<Integer> q = new TreeSet<Integer>(new Comparator<Integer>() {
      public int compare(Integer a, Integer b) {
        if (td[a] - td[b] != 0)
          return td[a] - td[b];
        return a - b;
      }
    });

    for (int s : from) {
      s--;
      td[s] = 0;
      q.add(s);
    }

    while (q.size() > 0) {
      int cur = q.pollFirst();

      for (int i = 0; i < g[cur].length; i++) {
        int next = g[cur][i];
        int nd = td[cur] + 1;
        if (nd < td[next]) {
          q.remove(next);
          td[next] = nd;
          q.add(next);
        }
      }
    }

    return td;
  }

  public static int[][] packD(int n, int[] from, int[] to) {
    return packD(n, from, to, from.length);
  }

  public static int[][] packD(int n, int[] from, int[] to, int sup) {
    int[][] g = new int[n][];
    int[] p = new int[n];
    for (int i = 0; i < sup; i++)
      p[from[i]]++;
    for (int i = 0; i < n; i++)
      g[i] = new int[p[i]];
    for (int i = 0; i < sup; i++) {
      g[from[i]][--p[from[i]]] = to[i];
    }
    return g;
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
