import java.util.*;

class TwoSAT {
  private final int n;
  private final InternalSCC scc;
  private final boolean[] answer;

  private boolean hasCalledSatisfiable = false;
  private boolean existsAnswer = false;

  public TwoSAT(int n) {
    this.n = n;
    this.scc = new InternalSCC(2 * n);
    this.answer = new boolean[n];
  }

  public void addClause(int x, boolean f, int y, boolean g) {
    scc.addEdge(x << 1 | (f ? 0 : 1), y << 1 | (g ? 1 : 0));
    scc.addEdge(y << 1 | (g ? 0 : 1), x << 1 | (f ? 1 : 0));
  }

  public void addImplication(int x, boolean f, int y, boolean g) {
    addClause(x, !f, y, g);
  }

  public void addNand(int x, boolean f, int y, boolean g) {
    addClause(x, !f, y, !g);
  }

  public boolean satisfiable() {
    hasCalledSatisfiable = true;
    int[] ids = scc.ids();
    for (int i = 0; i < n; i++) {
      if (ids[i << 1 | 0] == ids[i << 1 | 1])
        return existsAnswer = false;
      answer[i] = ids[i << 1 | 0] < ids[i << 1 | 1];
    }
    return existsAnswer = true;
  }

  public boolean[] answer() {
    if (!hasCalledSatisfiable) {
      throw new UnsupportedOperationException("Call TwoSAT#satisfiable at least once before TwoSAT#answer.");
    }
    if (existsAnswer)
      return answer;
    return null;
  }

  private static final class EdgeList {
    long[] a;
    int ptr = 0;

    EdgeList(int cap) {
      a = new long[cap];
    }

    void add(int upper, int lower) {
      if (ptr == a.length)
        grow();
      a[ptr++] = (long) upper << 32 | lower;
    }

    void grow() {
      long[] b = new long[a.length << 1];
      System.arraycopy(a, 0, b, 0, a.length);
      a = b;
    }
  }

  private static final class InternalSCC {
    final int n;
    int m;
    final EdgeList unorderedEdges;
    final int[] start;

    InternalSCC(int n) {
      this.n = n;
      this.unorderedEdges = new EdgeList(n);
      this.start = new int[n + 1];
    }

    void addEdge(int from, int to) {
      unorderedEdges.add(from, to);
      start[from + 1]++;
      this.m++;
    }

    static final long mask = 0xffff_ffffl;

    int[] ids() {
      for (int i = 1; i <= n; i++) {
        start[i] += start[i - 1];
      }
      int[] orderedEdges = new int[m];
      int[] count = new int[n + 1];
      System.arraycopy(start, 0, count, 0, n + 1);
      for (int i = 0; i < m; i++) {
        long e = unorderedEdges.a[i];
        orderedEdges[count[(int) (e >>> 32)]++] = (int) (e & mask);
      }
      int nowOrd = 0;
      int groupNum = 0;
      int k = 0;
      int[] par = new int[n];
      int[] vis = new int[n];
      int[] low = new int[n];
      int[] ord = new int[n];
      java.util.Arrays.fill(ord, -1);
      int[] ids = new int[n];
      long[] stack = new long[n];
      int ptr = 0;

      for (int i = 0; i < n; i++) {
        if (ord[i] >= 0)
          continue;
        par[i] = -1;
        stack[ptr++] = i;
        while (ptr > 0) {
          long p = stack[--ptr];
          int u = (int) (p & mask);
          int j = (int) (p >>> 32);
          if (j == 0) {
            low[u] = ord[u] = nowOrd++;
            vis[k++] = u;
          }
          if (start[u] + j < count[u]) {
            int to = orderedEdges[start[u] + j];
            stack[ptr++] += 1l << 32;
            if (ord[to] == -1) {
              stack[ptr++] = to;
              par[to] = u;
            } else {
              low[u] = Math.min(low[u], ord[to]);
            }
          } else {
            while (j-- > 0) {
              int to = orderedEdges[start[u] + j];
              if (par[to] == u)
                low[u] = Math.min(low[u], low[to]);
            }
            if (low[u] == ord[u]) {
              while (true) {
                int v = vis[--k];
                ord[v] = n;
                ids[v] = groupNum;
                if (v == u)
                  break;
              }
              groupNum++;
            }
          }
        }
      }
      for (int i = 0; i < n; i++) {
        ids[i] = groupNum - 1 - ids[i];
      }
      return ids;
    }
  }
}

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int d = ni();
    int[][] p = ntable(n, 2);
    TwoSAT ts = new TwoSAT(n);
    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        int x1 = p[i][0];
        int y1 = p[i][1];
        int x2 = p[j][0];
        int y2 = p[j][1];

        if (Math.abs(x1 - x2) < d) {
          ts.addNand(i, true, j, true);
        }
        if (Math.abs(x1 - y2) < d) {
          ts.addNand(i, true, j, false);
        }
        if (Math.abs(y1 - x2) < d) {
          ts.addNand(i, false, j, true);
        }
        if (Math.abs(y1 - y2) < d) {
          ts.addNand(i, false, j, false);
        }
      }
    }
    if (ts.satisfiable()) {
      System.out.println("Yes");
      boolean[] b = ts.answer();
      for (int i = 0; i < n; i++) {
        System.out.println(p[i][b[i] ? 0 : 1]);
      }
    } else {
      System.out.println("No");
    }
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
