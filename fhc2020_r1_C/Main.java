
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeSet;

class DisjointSet {
  public int[] upper; // minus:num_element(root) plus:root(normal)
  // public int[] w;

  public DisjointSet(int n) {
    upper = new int[n];
    Arrays.fill(upper, -1);
    // w = new int[n];
  }

  public DisjointSet(DisjointSet ds) {
    this.upper = Arrays.copyOf(ds.upper, ds.upper.length);
  }

  public int root(int x) {
    return upper[x] < 0 ? x : (upper[x] = root(upper[x]));
  }

  public boolean equiv(int x, int y) {
    return root(x) == root(y);
  }

  public boolean union(int x, int y) {
    x = root(x);
    y = root(y);
    if (x != y) {
      if (upper[y] < upper[x]) {
        int d = x;
        x = y;
        y = d;
      }
      // w[x] += w[y];
      upper[x] += upper[y];
      upper[y] = x;
    }
    return x == y;
  }

  public int count() {
    int ct = 0;
    for (int u : upper) {
      if (u < 0)
        ct++;
    }
    return ct;
  }

  public int[][] toBucket() {
    int n = upper.length;
    int[][] ret = new int[n][];
    int[] rp = new int[n];
    for (int i = 0; i < n; i++) {
      if (upper[i] < 0)
        ret[i] = new int[-upper[i]];
    }
    for (int i = 0; i < n; i++) {
      int r = root(i);
      ret[r][rp[r]++] = i;
    }
    return ret;
  }
}

public class Main {
  private static final String INPUT_ID = "/home/ubuntu/workspaces/contests/fhc2020_r1_C/quarantine";
  // 0:Sample 1:Validation 2:Submit
  private static final int STATE = 2;

  public static String solve() {
    int n = ni();
    int k = ni();
    char[] s = ns();
    int[] e = new int[n - 1];
    inputArray(e, k);

    int[] par = new int[n];
    par[0] = -1;
    for (int i = 1; i < n; i++) {
      par[i] = e[i - 1] - 1;
    }
    int[][] g = parentToG(par);
    DisjointSet ds = new DisjointSet(n);

    dfs(0, -1, g, s, ds);
    int[][] nodeCluster = new int[n][3];
    int[] cluster = new int[n];
    Map<Integer, Integer> map = new HashMap<>();
    int clusCount = 0;
    for (int i = 0; i < n; i++) {
      int v = ds.root(i);
      if (!map.containsKey(v)) {
        map.put(v, clusCount);
        clusCount++;
      }
      if (s[i] == '*') {
        nodeCluster[v][0]++;
        nodeCluster[v][1] = map.get(v);
        nodeCluster[v][2] = v;
      } else {
        nodeCluster[v] = null;
      }
      cluster[i] = map.get(v);
    }

    int[][] clusG = cluster(g, cluster);

    List<int[]> list = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      if (nodeCluster[i] == null || i != ds.root(i))
        continue;
      list.add(nodeCluster[i]);
    }

    long count;
    long max;
    if (list.size() == 1) {
      max = list.get(0)[0] * (list.get(0)[0] - 1) / 2;
      int v = list.get(0)[2];
      long[] tmp1 = new long[1];
      long[] tmp2 = new long[1];
      dfs2(0, -1, g, tmp1, ds, v);
      dfs3(v, -1, g, tmp2, ds, v, max);
      count = tmp1[0] + tmp2[0];

    } else if (list.size() == 0) {
      max = 0;
      long[] tmp = new long[1];
      dfs2(0, -1, g, tmp, ds, -1);
      count = tmp[0];

    } else {
      Collections.sort(list, (o1, o2) -> o2[0] - o1[0]);
      List<int[]> top = new ArrayList<>();

      int topNum = list.get(0)[0];
      int secNum = list.get(1)[0];

      long sum = 0;
      for (int[] v : list) {
        if (v[0] == topNum) {
          top.add(v);
        }
        sum += (long) v[0] * (v[0] - 1) / 2;
      }

      int[][] p3 = parents3(clusG);
      int[] cpar = p3[0];
      int[][] spar = logstepParents(cpar);
      int[] dep = p3[2];

      if (topNum > secNum) {
        count = 0;
        int topRoot = top.get(0)[1];

        for (int[] v : list) {
          if (v[0] == secNum) {
            count += (long) topNum * v[0] * d(topRoot, v[1], spar, dep);
          }
        }

        long cs = topNum + secNum;
        max = sum - ((long) topNum * (topNum - 1) / 2) - ((long) secNum * (secNum - 1) / 2) + cs * (cs - 1) / 2;
      } else {

        long cs = topNum + topNum;
        max = sum - ((long) topNum * (topNum - 1) / 2) * 2 + cs * (cs - 1) / 2;
        count = 0;

        for (int i = 0; i < top.size(); i++) {
          for (int j = i + 1; j < top.size(); j++) {
            int[] v = top.get(i);
            int[] u = top.get(j);
            count += (long) topNum * topNum * d(u[1], v[1], spar, dep);
          }
        }
      }
    }

    return max + " " + count;
  }

  public static int[][] cluster(int[][] g, int[] clus) {
    int sup = 0;
    for (int c : clus)
      sup = Math.max(sup, c);
    sup++;

    int E = 0;
    for (int[] e : g)
      E += e.length;

    int n = clus.length;
    int[][] bucket = new int[sup][];
    int[] bp = new int[sup];
    for (int i = 0; i < n; i++)
      bp[clus[i]]++;
    for (int i = 0; i < sup; i++)
      bucket[i] = new int[bp[i]];
    for (int i = n - 1; i >= 0; i--)
      bucket[clus[i]][--bp[clus[i]]] = i;

    int[][] gret = new int[sup][];
    int[] cs = new int[E];
    for (int i = 0; i < sup; i++) {
      int[] b = bucket[i];
      int p = 0;
      for (int from : b) {
        for (int to : g[from]) {
          if (clus[to] != i) {
            cs[p++] = clus[to];
          }
        }
      }
      Arrays.sort(cs, 0, p);

      gret[i] = new int[p];
      if (p == 0)
        continue;

      int q = 0;
      for (int e = 0; e < p; e++) {
        if (e == 0 || cs[e] != cs[e - 1]) {
          gret[i][q++] = cs[e];
        }
      }

      gret[i] = Arrays.copyOf(gret[i], q);
    }
    return gret;
  }

  public static int[][] parents3(int[][] g) {
    int n = g.length;
    int[] par = new int[n];
    Arrays.fill(par, -1);

    int[] depth = new int[n];
    int[] q = new int[n];
    int r = 0;
    for (int u = 0; u < n; u++) {
      if (par[u] == -1) {
        q[r] = u;
        depth[u] = 0;
        r++;
        for (int p = r - 1; p < r; p++) {
          int cur = q[p];
          for (int nex : g[cur]) {
            if (par[cur] != nex) {
              q[r++] = nex;
              par[nex] = cur;
              depth[nex] = depth[cur] + 1;
            }
          }
        }
      }
    }
    return new int[][] { par, q, depth };
  }

  public static long d(int x, int y, int[][] spar, int[] dep) {
    int lca = lca(x, y, spar, dep);
    return dep[x] + dep[y] - 2 * dep[lca];
  }

  public static int lca(int a, int b, int[][] spar, int[] depth) {
    if (depth[a] < depth[b]) {
      b = ancestor(b, depth[b] - depth[a], spar);
    } else if (depth[a] > depth[b]) {
      a = ancestor(a, depth[a] - depth[b], spar);
    }

    int low = -1;
    int high = depth[a];
    while (high - low > 1) {
      int x = (low + high) / 2;
      if (ancestor(a, x, spar) == ancestor(b, x, spar)) {
        high = x;
      } else {
        low = x;
      }
    }
    return ancestor(a, high, spar);
  }

  protected static int ancestor(int a, int m, int[][] spar) {
    for (; m > 0 && a != -1; m &= m - 1)
      a = spar[Integer.numberOfTrailingZeros(m)][a];
    return a;
  }

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

  private static long dfs3(int cur, int par, int[][] g, long[] ret, DisjointSet ds, int ok, long clusterSize) {
    long r = 1;
    for (int nex : g[cur]) {
      if (nex == par || !ds.equiv(nex, ok))
        continue;

      long v = dfs3(nex, cur, g, ret, ds, ok, clusterSize);
      ret[0] += v * (clusterSize - v);
      r += v;
    }
    return r;
  }

  private static long dfs2(int cur, int par, int[][] g, long[] ret, DisjointSet ds, int ng) {
    int n = g.length;
    long r = 1;
    for (int nex : g[cur]) {
      if (nex == par)
        continue;

      long v = dfs2(nex, cur, g, ret, ds, ng);
      if (ng < 0 || !ds.equiv(cur, ng) || !ds.equiv(nex, ng)) {
        ret[0] += v * (n - v);
      }
      r += v;
    }
    return r;
  }

  private static void dfs(int cur, int par, int[][] g, char[] s, DisjointSet ds) {
    for (int nex : g[cur]) {
      if (nex == par)
        continue;
      if (s[cur] == '*') {
        if (s[cur] == s[nex]) {
          ds.union(nex, cur);
        }
      }
      dfs(nex, cur, g, s, ds);
    }
  }

  public static int[][] parentToG(int[] par) {
    int n = par.length;
    int[] ct = new int[n];
    for (int i = 0; i < n; i++) {
      if (par[i] >= 0) {
        ct[i]++;
        ct[par[i]]++;
      }
    }
    int[][] g = new int[n][];
    for (int i = 0; i < n; i++) {
      g[i] = new int[ct[i]];
    }
    for (int i = 0; i < n; i++) {
      if (par[i] >= 0) {
        g[par[i]][--ct[par[i]]] = i;
        g[i][--ct[i]] = par[i];
      }
    }
    return g;
  }

  private static void inputArray(int[] array, int k) {
    int n = array.length;
    for (int i = 0; i < k; i++) {
      array[i] = ni();
    }
    long a = nl();
    long b = nl();
    long c = nl();
    for (int i = k; i < n; i++) {
      array[i] = (int) ((a * array[i - 2] + b * array[i - 1] + c) % (i + 1) + 1);
    }
  }

  public static String batch() {
    int t = ni();
    StringBuilder ret = new StringBuilder();
    for (int i = 0; i < t; i++) {
      String v = solve();
      String line = "Case #" + (i + 1) + ": " + v;
      System.out.println(line);
      ret.append(line);
      ret.append("\n");
    }
    return ret.toString();
  }

  public static void main(String[] args) throws IOException {
    String id = INPUT_ID;
    String[] suffix = { "_sample_input.txt", "_validation_input.txt", "_input.txt" };
    Path input = Paths.get(id + suffix[STATE]);
    new Thread(null, new Runnable() {
      @Override
      public void run() {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("output.txt"), StandardCharsets.UTF_8)) {
          reader = Files.newBufferedReader(input, StandardCharsets.UTF_8);
          String ret = batch();
          bw.write(ret);

          if (STATE == 0) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = Files.newBufferedReader(Paths.get(id + "_sample_output.txt"),
                StandardCharsets.UTF_8)) {
              String line;
              while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
              }
            }
            System.out.println();
            System.out.println("Sample output:");
            System.out.println(sb);
            System.out.println(sb.toString().equals(ret) ? "OK!" : "NG!");
          }
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }, "", 640000000).start();
  }

  private static StringTokenizer tokenizer = null;
  private static BufferedReader reader;

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
}
