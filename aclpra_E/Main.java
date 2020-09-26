import java.util.*;

/**
 * @verified - https://atcoder.jp/contests/practice2/tasks/practice2_e -
 *           http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=GRL_6_B
 */
class MinCostFlow {
  public class WeightedCapEdge {
    private final int from, to;
    private long cap;
    private long cost;
    private final int rev;

    WeightedCapEdge(int from, int to, long cap, long cost, int rev) {
      this.from = from;
      this.to = to;
      this.cap = cap;
      this.cost = cost;
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

    public long getCost() {
      return cost;
    }

    public long getFlow() {
      return g[to][rev].cap;
    }
  }

  private static final long INF = Long.MAX_VALUE;

  private final int n;
  private int m;
  private final java.util.ArrayList<WeightedCapEdge> edges;
  private final int[] count;
  private final WeightedCapEdge[][] g;
  private final long[] potential;

  private final long[] dist;
  private final WeightedCapEdge[] prev;

  public MinCostFlow(int n) {
    this.n = n;
    this.edges = new java.util.ArrayList<>();
    this.count = new int[n];
    this.g = new WeightedCapEdge[n][];
    this.potential = new long[n];
    this.dist = new long[n];
    this.prev = new WeightedCapEdge[n];
  }

  public int addEdge(int from, int to, long cap, long cost) {
    rangeCheck(from, 0, n);
    rangeCheck(to, 0, n);
    nonNegativeCheck(cap, "Capacity");
    nonNegativeCheck(cost, "Cost");
    WeightedCapEdge e = new WeightedCapEdge(from, to, cap, cost, count[to]);
    count[from]++;
    count[to]++;
    edges.add(e);
    return m++;
  }

  private void buildGraph() {
    for (int i = 0; i < n; i++) {
      g[i] = new WeightedCapEdge[count[i]];
    }
    int[] idx = new int[n];
    for (WeightedCapEdge e : edges) {
      g[e.to][idx[e.to]++] = new WeightedCapEdge(e.to, e.from, 0, -e.cost, idx[e.from]);
      g[e.from][idx[e.from]++] = e;
    }
  }

  private long addFlow;
  private long addCost;

  public long[] minCostMaxFlow(int s, int t) {
    return minCostFlow(s, t, INF);
  }

  public long[] minCostFlow(int s, int t, long flowLimit) {
    rangeCheck(s, 0, n);
    rangeCheck(t, 0, n);
    if (s == t) {
      throw new IllegalArgumentException(String.format("s = t = %d", s));
    }
    nonNegativeCheck(flowLimit, "Flow");
    buildGraph();
    long flow = 0;
    long cost = 0;
    while (true) {
      dijkstra(s, t, flowLimit - flow);
      if (addFlow == 0)
        break;
      flow += addFlow;
      cost += addFlow * addCost;
    }
    return new long[] { flow, cost };
  }

  public java.util.ArrayList<long[]> minCostSlope(int s, int t) {
    return minCostSlope(s, t, INF);
  }

  public java.util.ArrayList<long[]> minCostSlope(int s, int t, long flowLimit) {
    rangeCheck(s, 0, n);
    rangeCheck(t, 0, n);
    if (s == t) {
      throw new IllegalArgumentException(String.format("s = t = %d", s));
    }
    nonNegativeCheck(flowLimit, "Flow");
    buildGraph();
    java.util.ArrayList<long[]> slope = new java.util.ArrayList<>();
    long prevCost = -1;
    long flow = 0;
    long cost = 0;
    while (true) {
      slope.add(new long[] { flow, cost });
      dijkstra(s, t, flowLimit - flow);
      if (addFlow == 0)
        return slope;
      flow += addFlow;
      cost += addFlow * addCost;
      if (addCost == prevCost) {
        slope.remove(slope.size() - 1);
      }
      prevCost = addCost;
    }
  }

  private void dijkstra(int s, int t, long maxFlow) {
    final class State implements Comparable<State> {
      final int v;
      final long d;

      State(int v, long d) {
        this.v = v;
        this.d = d;
      }

      public int compareTo(State s) {
        return d == s.d ? v - s.v : d > s.d ? 1 : -1;
      }
    }
    java.util.Arrays.fill(dist, INF);
    dist[s] = 0;
    java.util.PriorityQueue<State> pq = new java.util.PriorityQueue<>();
    pq.add(new State(s, 0l));
    while (pq.size() > 0) {
      State st = pq.poll();
      int u = st.v;
      if (st.d != dist[u])
        continue;
      for (WeightedCapEdge e : g[u]) {
        if (e.cap <= 0)
          continue;
        int v = e.to;
        long nextCost = dist[u] + e.cost + potential[u] - potential[v];
        if (nextCost < dist[v]) {
          dist[v] = nextCost;
          prev[v] = e;
          pq.add(new State(v, dist[v]));
        }
      }
    }
    if (dist[t] == INF) {
      addFlow = 0;
      addCost = INF;
      return;
    }
    for (int i = 0; i < n; i++) {
      potential[i] += dist[i];
    }
    addCost = 0;
    addFlow = maxFlow;
    for (int v = t; v != s;) {
      WeightedCapEdge e = prev[v];
      addCost += e.cost;
      addFlow = java.lang.Math.min(addFlow, e.cap);
      v = e.from;
    }
    for (int v = t; v != s;) {
      WeightedCapEdge e = prev[v];
      e.cap -= addFlow;
      g[v][e.rev].cap += addFlow;
      v = e.from;
    }
  }

  public void clearFlow() {
    java.util.Arrays.fill(potential, 0);
    for (WeightedCapEdge e : edges) {
      long flow = e.getFlow();
      e.cap += flow;
      g[e.to][e.rev].cap -= flow;
    }
  }

  public WeightedCapEdge getEdge(int i) {
    rangeCheck(i, 0, m);
    return edges.get(i);
  }

  public java.util.ArrayList<WeightedCapEdge> getEdges() {
    return edges;
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
    int k = ni();
    int[][] a = ntable(n, n);

    int nodes = n * n + n * 2 + 2;

    int voff = n * n;
    int hoff = n * n + n;
    int sink = nodes - 1;
    int source = nodes - 2;

    MinCostFlow mcf = new MinCostFlow(nodes);
    long potential = Integer.MAX_VALUE;
    long infCap = 6000;

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        int v = i * n + j;
        mcf.addEdge(voff + i, v, 1, potential - a[i][j]);
        mcf.addEdge(v, hoff + j, 1, 0);
      }
    }
    for (int i = 0; i < n; i++) {
      mcf.addEdge(source, voff + i, k, 0);
      mcf.addEdge(hoff + i, sink, k, 0);
    }
    List<long[]> slope = mcf.minCostSlope(source, sink);
    long max = 0;
    long flow = -1;
    for (long[] v : slope) {
      long now = potential * v[0] - v[1];
      if (now > max) {
        max = now;
        flow = v[0];
      }
    }

    mcf.clearFlow();
    mcf.minCostFlow(source, sink, flow);
    System.out.println(max);
    char[] line = new char[n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        line[j] = mcf.getEdge((i * n + j) * 2).getFlow() > 0 ? 'X' : '.';
      }
      System.out.println(line);
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
