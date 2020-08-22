import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class Main {

  private static void solve() {
    int n = ni();
    int m = ni();
    int p = ni();
    int s = ni() - 1;
    int g = ni() - 1;

    int[] from = new int[m];
    int[] to = new int[m];
    for (int i = 0; i < m; i++) {
      from[i] = ni() - 1;
      to[i] = ni() - 1;
    }
    int[][] h = packU(n, from, to);
    int[][] ds = dijk(h, s);
    int[][] dg = dijk(h, g);

    List<Integer> ret = new ArrayList<>();
    outer: for (int i = 0; i < n; i++) {
      for (int j = 0; j < 2; j++) {
        for (int k = 0; k < 2; k++) {
          int d1 = ds[i][j];
          int d2 = dg[i][k];

          if (d1 + d2 <= p && (p - d1 - d2) % 2 == 0) {
            ret.add(i + 1);
            continue outer;
          }
        }
      }
    }

    if (ret.size() == 0) {
      out.println(-1);
    } else {
      out.println(ret.size());
      for (int v : ret) {
        out.println(v);
      }
    }
  }

  public static int[][] dijk(int[][] g, int from) {
    int n = g.length;
    final int[][] td = new int[n][2];
    for (int[] v : td)
      Arrays.fill(v, Integer.MAX_VALUE);
    TreeSet<int[]> q = new TreeSet<>((a, b) -> {
      if (td[a[0]][a[1]] - td[b[0]][b[1]] != 0)
        return td[a[0]][a[1]] - td[b[0]][b[1]];
      return a[0] == b[0] ? a[1] - b[1] : a[0] - b[0];
    });
    q.add(new int[] { from, 0 });
    td[from][0] = 0;

    while (q.size() > 0) {
      int[] v = q.pollFirst();
      int cur = v[0];
      int cnt = v[1];

      for (int nex : g[cur]) {
        int[] u = new int[] { nex, cnt };
        int nd = td[cur][cnt] + 1;
        if (nd < td[nex][cnt]) {
          q.remove(u);
          td[nex][cnt] = nd;
          q.add(u);
        }

        if (cnt == 0) {
          int[] u2 = new int[] { nex, cnt + 1 };
          int nd2 = td[cur][cnt] + 1;
          if (nd2 < td[nex][cnt + 1] && nd2 > td[nex][cnt]) {
            q.remove(u2);
            td[nex][cnt + 1] = nd;
            q.add(u2);
          }
        }
      }
    }

    return td;
  }

  public static int[][] packU(int n, int[] from, int[] to) {
    return packU(n, from, to, from.length);
  }

  public static int[][] packU(int n, int[] from, int[] to, int sup) {
    int[][] g = new int[n][];
    int[] p = new int[n];
    for (int i = 0; i < sup; i++)
      p[from[i]]++;
    for (int i = 0; i < sup; i++)
      p[to[i]]++;
    for (int i = 0; i < n; i++)
      g[i] = new int[p[i]];
    for (int i = 0; i < sup; i++) {
      g[from[i]][--p[from[i]]] = to[i];
      g[to[i]][--p[to[i]]] = from[i];
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
