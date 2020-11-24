import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int m = ni();
    int[] from = new int[m];
    int[] to = new int[m];
    int[] c = new int[m];
    for (int i = 0; i < m; i++) {
      from[i] = ni() - 1;
      to[i] = ni() - 1;
      c[i] = ni();
    }
    int[][][] g = packWU(n, from, to, c);

    int[] ret = new int[n];
    dfs(0, -1, g[0][0][1], new boolean[n], g, ret);

    for (int v : ret) {
      out.println(v);
    }
  }

  private static void dfs(int cur, int rootColor, int edgeColor, boolean[] ved, int[][][] g, int[] ret) {
    ved[cur] = true;

    if (rootColor != edgeColor) {
      ret[cur] = edgeColor;
    }

    Set<Integer> childColors = new HashSet<>();
    childColors.add(rootColor);
    for (int[] v : g[cur]) {
      int nex = v[0];
      int c = v[1];

      if (ved[nex])
        continue;

      childColors.add(c);
      dfs(nex, ret[cur], c, ved, g, ret);
    }

    if (rootColor == edgeColor) {
      int n = g.length;
      for (int i = 1; i <= n; i++) {
        if (!childColors.contains(i)) {
          ret[cur] = i;
          break;
        }
      }
    }
  }

  public static int[][][] packWU(int n, int[] from, int[] to, int[] w) {
    return packWU(n, from, to, w, from.length);
  }

  public static int[][][] packWU(int n, int[] from, int[] to, int[] w, int sup) {
    int[][][] g = new int[n][][];
    int[] p = new int[n];
    for (int i = 0; i < sup; i++)
      p[from[i]]++;
    for (int i = 0; i < sup; i++)
      p[to[i]]++;
    for (int i = 0; i < n; i++)
      g[i] = new int[p[i]][2];
    for (int i = 0; i < sup; i++) {
      --p[from[i]];
      g[from[i]][p[from[i]]][0] = to[i];
      g[from[i]][p[from[i]]][1] = w[i];
      --p[to[i]];
      g[to[i]][p[to[i]]][0] = from[i];
      g[to[i]][p[to[i]]][1] = w[i];
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
