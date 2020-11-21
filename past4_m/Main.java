import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int q = ni();
    int[] from = new int[n - 1];
    int[] to = new int[n - 1];
    int[] w = new int[n - 1];
    for (int i = 0; i < n - 1; i++) {
      from[i] = ni() - 1;
      to[i] = ni() - 1;
      w[i] = i;
    }
    int[][] g = packU(n, from, to);
    int[][] p3 = parents3(g, 0);
    int[] par = p3[0];
    int[] depth = p3[2];
    int[] ord = p3[1];

    int[][] spar = logstepParents(par);

    Map<Integer, TreeSet<Integer>> dp = new HashMap<>();
    Map<Integer, Set<Integer>> remove = new HashMap<>();
    for (int i = 0; i < n; i++) {
      dp.put(i, new TreeSet<>());
      remove.put(i, new HashSet<>());
    }

    int[][] op = ntable(q, 3);

    for (int i = 0; i < q; i++) {
      int u = op[i][0] - 1;
      int v = op[i][1] - 1;
      int c = op[i][2];

      int lca = lca2(u, v, spar, depth);

      remove.get(lca).add(i);
      dp.get(u).add(i);
      dp.get(v).add(i);
    }

    int[][][] h = packWU(n, from, to, w);
    int[] pare = new int[n];
    for (int i = 0; i < n; i++) {
      for (int[] v : h[i]) {
        if (v[0] == par[i]) {
          pare[i] = v[1];
          break;
        }
      }
    }
    Integer[] ret = new Integer[n - 1];
    Arrays.fill(ret, -1);

    for (int i = n - 1; i >= 1; i--) {
      int cur = ord[i];
      int nex = par[cur];

      TreeSet<Integer> colors1 = dp.get(cur);
      for (Integer c : remove.get(cur)) {
        colors1.remove(c);
      }

      if (colors1.size() > 0) {
        ret[pare[cur]] = colors1.last();
      }

      TreeSet<Integer> colors2 = dp.get(nex);
      if (colors1.size() > colors2.size()) {
        var tmp = colors1;
        colors1 = colors2;
        colors2 = tmp;
      }

      for (Integer v : colors1) {
        colors2.add(v);
      }
      dp.put(nex, colors2);
    }

    // dfs(0, -1, h, ret, add, remove);
    for (int v : ret) {
      out.println(v < 0 ? 0 : op[v][2]);
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

  public static int[][] parents3(int[][] g, int root) {
    int n = g.length;
    int[] par = new int[n];
    Arrays.fill(par, -1);

    int[] depth = new int[n];
    depth[0] = 0;

    int[] q = new int[n];
    q[0] = root;
    for (int p = 0, r = 1; p < r; p++) {
      int cur = q[p];
      for (int nex : g[cur]) {
        if (par[cur] != nex) {
          q[r++] = nex;
          par[nex] = cur;
          depth[nex] = depth[cur] + 1;
        }
      }
    }
    return new int[][] { par, q, depth };
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

  public static int lca2(int a, int b, int[][] spar, int[] depth) {
    if (depth[a] < depth[b]) {
      b = ancestor(b, depth[b] - depth[a], spar);
    } else if (depth[a] > depth[b]) {
      a = ancestor(a, depth[a] - depth[b], spar);
    }

    if (a == b)
      return a;
    for (int d = 31 - Integer.numberOfLeadingZeros(depth[a]); d >= 0; d--) {
      if (spar[d][a] != spar[d][b]) {
        a = spar[d][a];
        b = spar[d][b];
      }
    }
    return spar[0][a];
  }

  protected static int ancestor(int a, int m, int[][] spar) {
    for (; m > 0 && a != -1; m &= m - 1)
      a = spar[Integer.numberOfTrailingZeros(m)][a];
    return a;
  }

  // Is shal ancestor of deep?
  public static boolean isAncestor(int shal, int deep, int[][] spar, int[] dep) {
    return dep[shal] <= dep[deep] && ancestor(deep, dep[deep] - dep[shal], spar) == shal;
  }

  /**
   * 2^k個上の先祖を格納
   * 
   * @param par
   * @return
   */
  public static int[][] logstepParents(int[] par) {
    int n = par.length;
    int m = Integer.numberOfTrailingZeros(Integer.highestOneBit(n - 1)) + 1;
    int[][] pars = new int[m][n];
    pars[0] = par;
    for (int j = 1; j < m; j++) {
      for (int i = 0; i < n; i++) {
        pars[j][i] = pars[j - 1][i] == -1 ? -1 : pars[j - 1][pars[j - 1][i]];
      }
    }
    return pars;
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
