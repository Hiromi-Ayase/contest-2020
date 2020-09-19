import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int m = ni();

    int[] from = new int[m];
    int[] to = new int[m];
    for (int i = 0; i < m; i++) {
      from[i] = ni();
      to[i] = ni();
    }
    int[][] g = packD(n, from, to);
    int[] clus = decomposeToSCC(g);
    int[][] h = condense(g, clus);

    int[] top = sortTopologically(h);

    int[][] c = new int[top.length][2];
    for (int i = 0; i < top.length; i++) {
      c[i][0] = i;
      c[i][1] = top[i];
    }
    Arrays.sort(c, (o1, o2) -> o1[1] - o2[1]);

    int[][] ret = new int[n][3];
    for (int i = 0; i < n; i++) {
      ret[i][0] = i;
      ret[i][1] = clus[i];
      ret[i][2] = c[clus[i]][0];
    }
    Arrays.sort(ret, (o1, o2) -> o1[2] - o2[2]);

    int ptr = 0;
    out.println(h.length);
    for (int v : top) {
      int s = ptr;
      while (ptr < n && ret[ptr][1] == v)
        ptr++;

      out.print(ptr - s + " ");
      for (int i = s; i < ptr; i++) {
        out.print(ret[i][0] + " ");
      }
      out.println();
    }
  }

  public static int[] decomposeToSCC(int[][] g) {
    int n = g.length;
    int[] stack = new int[n + 1];
    int[] ind = new int[n + 1];
    int[] ord = new int[n];
    Arrays.fill(ord, -1);
    int[] low = new int[n];
    Arrays.fill(low, -1);
    int sp = 0;
    int id = 0; // preorder
    int[] clus = new int[n];
    int cid = 0;
    int[] cstack = new int[n + 1];
    int csp = 0;
    boolean[] incstack = new boolean[n];
    for (int i = 0; i < n; i++) {
      if (ord[i] == -1) {
        ind[sp] = 0;
        cstack[csp++] = i;
        stack[sp++] = i;
        incstack[i] = true;
        while (sp > 0) {
          int cur = stack[sp - 1];
          if (ind[sp - 1] == 0) {
            ord[cur] = low[cur] = id++;
          }
          if (ind[sp - 1] < g[cur].length) {
            int nex = g[cur][ind[sp - 1]];
            if (ord[nex] == -1) {
              ind[sp - 1]++;
              ind[sp] = 0;
              incstack[nex] = true;
              cstack[csp++] = nex;
              stack[sp++] = nex;
            } else {
              // shortcut
              if (incstack[nex])
                low[cur] = Math.min(low[cur], low[nex]);
              ind[sp - 1]++;
            }
          } else {
            if (ord[cur] == low[cur]) {
              while (csp > 0) {
                incstack[cstack[csp - 1]] = false;
                clus[cstack[--csp]] = cid;
                if (cstack[csp] == cur)
                  break;
              }
              cid++;
            }
            if (--sp >= 1)
              low[stack[sp - 1]] = Math.min(low[stack[sp - 1]], low[stack[sp]]);
          }
        }
      }
    }
    return clus;
  }

  public static int[] sortTopologically(int[][] g) {
    int n = g.length;
    int[] ec = new int[n];
    for (int i = 0; i < n; i++) {
      for (int to : g[i])
        ec[to]++;
    }
    int[] ret = new int[n];
    int q = 0;

    // sources
    for (int i = 0; i < n; i++) {
      if (ec[i] == 0)
        ret[q++] = i;
    }

    for (int p = 0; p < q; p++) {
      for (int to : g[ret[p]]) {
        if (--ec[to] == 0)
          ret[q++] = to;
      }
    }
    // loop
    for (int i = 0; i < n; i++) {
      if (ec[i] > 0)
        return null;
    }
    return ret;
  }

  public static int[][] condense(int[][] g, int[] clus) {
    int n = g.length;
    int m = 0;
    for (int i = 0; i < n; i++)
      m = Math.max(m, clus[i]);
    m++;

    int[] cp = new int[m];
    for (int i = 0; i < n; i++) {
      cp[clus[i]] += g[i].length;
    }
    int[][] c = new int[m][];
    for (int i = 0; i < m; i++) {
      c[i] = new int[cp[i]];
    }

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < g[i].length; j++) {
        c[clus[i]][--cp[clus[i]]] = clus[g[i][j]];
      }
    }

    for (int i = 0; i < m; i++) {
      Arrays.sort(c[i]);
      int jp = 0;
      for (int j = 0; j < c[i].length; j++) {
        if ((j == 0 || c[i][j] != c[i][j - 1]) && c[i][j] != i) {
          c[i][jp++] = c[i][j];
        }
      }
      c[i] = Arrays.copyOf(c[i], jp);
    }
    return c;
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
