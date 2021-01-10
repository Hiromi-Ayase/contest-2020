import java.util.*;

class MaxFlow {
  public class CapEdge {
    private final int from, to;
    private long cap;
    private final int rev;

    CapEdge(int from, int to, long cap, int rev) {
      this.from = from;
      this.to = to;
      this.cap = cap;
      this.rev = rev;
    }

    public int getFrom() {
      return from;
    }

    public int getTo() {
      return to;
    }

    public long getCap() {
      return cap;
    }

    public long getFlow() {
      return g[to][rev].cap;
    }
  }

  private static final long INF = Long.MAX_VALUE;

  private final int n;
  private int m;
  private final java.util.ArrayList<CapEdge> edges;
  private final int[] count;
  private final CapEdge[][] g;

  public MaxFlow(int n) {
    this.n = n;
    this.edges = new java.util.ArrayList<>();
    this.count = new int[n];
    this.g = new CapEdge[n][];
  }

  public int addEdge(int from, int to, long cap) {
    rangeCheck(from, 0, n);
    rangeCheck(to, 0, n);
    nonNegativeCheck(cap, "Capacity");
    CapEdge e = new CapEdge(from, to, cap, count[to]);
    count[from]++;
    count[to]++;
    edges.add(e);
    return m++;
  }

  public CapEdge getEdge(int i) {
    rangeCheck(i, 0, m);
    return edges.get(i);
  }

  public java.util.ArrayList<CapEdge> getEdges() {
    return edges;
  }

  public void changeEdge(int i, long newCap, long newFlow) {
    rangeCheck(i, 0, m);
    nonNegativeCheck(newCap, "Capacity");
    if (newFlow > newCap) {
      throw new IllegalArgumentException(String.format("Flow %d is greater than capacity %d.", newCap, newFlow));
    }
    CapEdge e = edges.get(i);
    CapEdge er = g[e.to][e.rev];
    e.cap = newCap - newFlow;
    er.cap = newFlow;
  }

  private void buildGraph() {
    for (int i = 0; i < n; i++) {
      g[i] = new CapEdge[count[i]];
    }
    int[] idx = new int[n];
    for (CapEdge e : edges) {
      g[e.to][idx[e.to]++] = new CapEdge(e.to, e.from, 0, idx[e.from]);
      g[e.from][idx[e.from]++] = e;
    }
  }

  public long maxFlow(int s, int t) {
    return flow(s, t, INF);
  }

  public long flow(int s, int t, long flowLimit) {
    rangeCheck(s, 0, n);
    rangeCheck(t, 0, n);
    buildGraph();
    long flow = 0;
    int[] level = new int[n];
    int[] que = new int[n];
    int[] iter = new int[n];
    while (true) {
      java.util.Arrays.fill(level, -1);
      dinicBFS(s, t, level, que);
      if (level[t] < 0)
        return flow;
      java.util.Arrays.fill(iter, 0);
      while (true) {
        long d = dinicDFS(t, s, flowLimit - flow, iter, level);
        if (d <= 0)
          break;
        flow += d;
      }
    }
  }

  private void dinicBFS(int s, int t, int[] level, int[] que) {
    int hd = 0, tl = 0;
    que[tl++] = s;
    level[s] = 0;
    while (tl > hd) {
      int u = que[hd++];
      for (CapEdge e : g[u]) {
        int v = e.to;
        if (e.cap <= 0 || level[v] >= 0)
          continue;
        level[v] = level[u] + 1;
        if (v == t)
          return;
        que[tl++] = v;
      }
    }
  }

  private long dinicDFS(int cur, int s, long f, int[] iter, int[] level) {
    if (cur == s)
      return f;
    long res = 0;
    while (iter[cur] < count[cur]) {
      CapEdge er = g[cur][iter[cur]++];
      int u = er.to;
      CapEdge e = g[u][er.rev];
      if (level[u] >= level[cur] || e.cap <= 0)
        continue;
      long d = dinicDFS(u, s, Math.min(f - res, e.cap), iter, level);
      if (d <= 0)
        continue;
      e.cap -= d;
      er.cap += d;
      res += d;
      if (res == f)
        break;
    }
    return res;
  }

  public long fordFulkersonMaxFlow(int s, int t) {
    return fordFulkersonFlow(s, t, INF);
  }

  public long fordFulkersonFlow(int s, int t, long flowLimit) {
    rangeCheck(s, 0, n);
    rangeCheck(t, 0, n);
    buildGraph();
    boolean[] used = new boolean[n];
    long flow = 0;
    while (true) {
      java.util.Arrays.fill(used, false);
      long f = fordFulkersonDFS(s, t, flowLimit - flow, used);
      if (f <= 0)
        return flow;
      flow += f;
    }
  }

  private long fordFulkersonDFS(int cur, int t, long f, boolean[] used) {
    if (cur == t)
      return f;
    used[cur] = true;
    for (CapEdge e : g[cur]) {
      if (used[e.to] || e.cap <= 0)
        continue;
      long d = fordFulkersonDFS(e.to, t, Math.min(f, e.cap), used);
      if (d <= 0)
        continue;
      e.cap -= d;
      g[e.to][e.rev].cap += d;
      return d;
    }
    return 0;
  }

  public boolean[] minCut(int s) {
    rangeCheck(s, 0, n);
    boolean[] reachable = new boolean[n];
    int[] stack = new int[n];
    int ptr = 0;
    stack[ptr++] = s;
    reachable[s] = true;
    while (ptr > 0) {
      int u = stack[--ptr];
      for (CapEdge e : g[u]) {
        int v = e.to;
        if (reachable[v] || e.cap <= 0)
          continue;
        reachable[v] = true;
        stack[ptr++] = v;
      }
    }
    return reachable;
  }

  private void rangeCheck(int i, int minInlusive, int maxExclusive) {
    if (i < 0 || i >= maxExclusive) {
      throw new IndexOutOfBoundsException(String.format("Index %d out of bounds for length %d", i, maxExclusive));
    }
  }

  private void nonNegativeCheck(long cap, java.lang.String attribute) {
    if (cap < 0) {
      throw new IllegalArgumentException(String.format("%s %d is negative.", attribute, cap));
    }
  }
}

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();

    int m = n + 400010;
    MaxFlow flow = new MaxFlow(m + 1);

    int ptr = 0;

    Set<Integer> v = new HashSet<>();
    for (int i = 1; i <= n; i++) {
      int a = ni() + n;
      int b = ni() + n;
      v.add(a);
      v.add(b);

      flow.addEdge(0, i, 1);
      flow.addEdge(i, a, 1);
      flow.addEdge(i, b, 1);
    }

    for (int u : v) {
      flow.addEdge(u, m, 1);
    }

    System.out.println(flow.flow(0, m, Integer.MAX_VALUE));
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
