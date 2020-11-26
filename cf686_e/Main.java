import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int t = ni();
    for (int i = 0; i < t; i++) {
      int n = ni();
      int[] from = new int[n];
      int[] to = new int[n];
      for (int j = 0; j < n; j++) {
        from[j] = ni() - 1;
        to[j] = ni() - 1;
      }
      int[][] g = packU(n, from, to);

      long ret = solve(g);
      out.println(ret);
    }
  }

  private static long solve(int[][] g) {
    int[][] sp = split(g);
    int[] par = sp[0];
    int[] next = sp[1];

    int[] cycle = cycle(g, next);
    Set<Integer> set = new HashSet<>();
    for (int v : cycle) {
      set.add(v);
    }

    int n = g.length;
    int[] color = new int[n];
    boolean[] ved = new boolean[n];
    for (int i = 0; i < cycle.length; i++) {
      dfs(cycle[i], i, color, ved, g, set);
    }
    Map<Integer, Long> cnt = new HashMap<>();
    for (int v : color) {
      cnt.putIfAbsent(v, 0L);
      cnt.compute(v, (key, val) -> val + 1);
    }
    long ret = 0;
    for (var e : cnt.entrySet()) {
      long v = e.getValue();
      long u = n - v;
      ret += v * (v - 1);
      ret += u * v * 2;
    }
    return ret / 2;
  }

  private static void dfs(int v, int c, int[] ret, boolean[] ved, int[][] g, Set<Integer> set) {
    ret[v] = c;
    ved[v] = true;
    for (int nex : g[v]) {
      if (ved[nex] || set.contains(nex))
        continue;

      dfs(nex, c, ret, ved, g, set);
    }
  }

  private static int[][] packU(int n, int[] from, int[] to) {
    int[][] g = new int[n][];
    int[] p = new int[n];
    for (int f : from)
      p[f]++;
    for (int t : to)
      p[t]++;
    for (int i = 0; i < n; i++)
      g[i] = new int[p[i]];
    for (int i = 0; i < from.length; i++) {
      g[from[i]][--p[from[i]]] = to[i];
      g[to[i]][--p[to[i]]] = from[i];
    }
    return g;
  }

  public static int[][] split(int[][] g) {
    int n = g.length;
    int[] deg = new int[n];
    int[] par = new int[n];
    Arrays.fill(par, -1);
    for (int i = 0; i < n; i++) {
      deg[i] = g[i].length;
    }
    Queue<Integer> q = new ArrayDeque<Integer>();
    for (int i = 0; i < n; i++) {
      if (deg[i] == 1) {
        q.add(i);
      }
    }
    while (!q.isEmpty()) {
      int cur = q.poll();
      deg[cur] = -9999999;
      for (int e : g[cur]) {
        deg[e]--;
        if (deg[e] >= 0) {
          par[cur] = e;
        }
        if (deg[e] >= 0 && deg[e] <= 1) {
          q.add(e);
        }
      }
    }
    boolean[] ved = new boolean[n];
    int[] next = new int[n];
    Arrays.fill(next, -1);
    for (int i = 0; i < n; i++) {
      if (!ved[i] && deg[i] == 2) {
        ved[i] = true;
        int cur = i;
        outer: while (true) {
          for (int e : g[cur]) {
            if (deg[e] == 2 && !ved[e]) {
              next[cur] = e;
              ved[e] = true;
              cur = e;
              continue outer;
            }
          }
          next[cur] = i;
          break;
        }
      }
    }

    return new int[][] { par, next };
  }

  public static int[] makeOrder(int[][] g, int[] par) {
    int n = g.length;
    int[] ord = new int[n];
    int p = 0;
    boolean[] ved = new boolean[n];
    for (int i = 0; i < n; i++) {
      if (par[i] == -1) {
        ord[p++] = i;
        ved[i] = true;
      }
    }
    for (int r = 0; r < n; r++) {
      for (int e : g[ord[r]]) {
        if (!ved[e]) {
          ved[e] = true;
          ord[p++] = e;
        }
      }
    }
    return ord;
  }

  public static int[] makePW(int[][][] wg, int[] par) {
    int n = wg.length;
    int[] pw = new int[n];
    Arrays.fill(pw, -1);
    for (int i = 0; i < n; i++) {
      for (int[] e : wg[i]) {
        if (par[i] == e[0]) {
          pw[i] = e[1];
        }
      }
    }
    return pw;
  }

  // cycle部分を取り出す
  // TODO check
  public static int[] cycle(int[][] g, int[] next) {
    int n = g.length;
    int[] which = new int[n];
    int p = 0;
    for (int i = 0; i < n; i++) {
      if (next[i] != -1) {
        int u = i;
        while (true) {
          which[p++] = u;
          u = next[u];
          if (u == i)
            break;
        }
        break;
      }
    }
    return Arrays.copyOf(which, p);
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
