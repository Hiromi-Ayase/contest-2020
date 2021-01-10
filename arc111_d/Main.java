import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int m = ni();

    int[] from = new int[m];
    int[] to = new int[m];
    int[] e = new int[m];
    for (int i = 0; i < m; i++) {
      from[i] = ni() - 1;
      to[i] = ni() - 1;
      e[i] = i;
    }
    int[] c = na(n);
    int[][][] g = packWU(n, from, to, e);
    int[] ret = new int[m];
    Arrays.fill(ret, -1);
    Set<Integer> ved = new HashSet<>();
    for (int i = 0; i < n; i++) {
      ved.clear();
      dfsSCC(i, ret, g, c, ved);

      if (ved.size() > 0) {
        for (int cur : ved) {
          for (int[] v : g[cur]) {
            int nex = v[0];
            int ee = v[1];

            if (c[nex] < c[cur]) {
              ret[ee] = cur;
            } else if (c[nex] > c[cur]) {
              ret[ee] = nex;
            }
          }
        }
      }
    }
    for (int i = 0; i < n; i++) {
      dfsNonSCC(i, ret, g, c);
    }

    for (int i = 0; i < m; i++) {
      if (ret[i] == from[i]) {
        System.out.println("->");
      } else {
        System.out.println("<-");
      }
    }

  }

  private static int dfsNonSCC(int cur, int[] ret, int[][][] g, int[] c) {
    int sum = 1;
    for (int[] v : g[cur]) {
      int nex = v[0];
      int e = v[1];

      if (c[cur] <= c[nex])
        continue;

      if (ret[e] >= 0) {
        sum += c[nex];
        continue;
      }

      ret[e] = cur;
      sum += dfsNonSCC(nex, ret, g, c);
      ;
    }
    return sum;
  }

  private static void dfsSCC(int cur, int[] ret, int[][][] g, int[] c, Set<Integer> ved) {
    for (int[] v : g[cur]) {
      int nex = v[0];
      int e = v[1];

      if (ret[e] >= 0 || c[cur] != c[nex])
        continue;

      ret[e] = cur;
      ved.add(nex);
      dfsSCC(nex, ret, g, c, ved);
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
              // U.tr(cur, nex, incstack[nex], low[nex], stack);
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
