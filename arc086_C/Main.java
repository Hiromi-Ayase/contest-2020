import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Main {
  private static int mod = 1000000007;

  private static void solve() {
    int n = ni() + 1;
    int[] par = new int[n];
    par[0] = -1;
    for (int i = 1; i < n; i++) {
      par[i] = ni();
    }
    int[][] g = parentToChildren(par);

    long[] p2 = new long[n + 1];
    p2[0] = 1;
    for (int i = 0; i < n; i++) {
      p2[i + 1] = p2[i] * 2 % mod;
    }
    int[][] p3 = parents3(parentToG(par), 0);
    int[] dep = p3[2];
    int maxDep = Arrays.stream(dep).max().getAsInt();

    int[] ds = new int[maxDep + 1];
    for (int i = 0; i < n; i++)
      ds[dep[i]]++;

    Deque<long[]> q = dfs(0, 0, g);

    long ans = 0;
    int d = 0;
    for (long[] u : q) {
      ans += u[1] * p2[n - ds[d]] % mod;
      ans %= mod;
      d++;
    }
    System.out.println(ans);
  }

  static long[] tmp = new long[3];

  private static Deque<long[]> dfs(int i, int d, int[][] g) {
    if (g[i].length == 0) {
      Deque<long[]> ret = new ArrayDeque<>();
      ret.add(new long[] { 1, 1, 0 });
      return ret;
    }

    List<Deque<long[]>> list = new ArrayList<>();
    int max = 0;
    Deque<long[]> ret = null;

    for (int nex : g[i]) {
      Deque<long[]> q = dfs(nex, d + 1, g);
      list.add(q);
      if (q.size() > max) {
        ret = q;
        max = q.size();
      }
    }

    int maxDep = 0;
    for (Deque<long[]> q : list) {
      if (q == ret)
        continue;

      Iterator<long[]> it = ret.iterator();
      maxDep = Math.max(maxDep, q.size());
      for (long[] v : q) {
        long[] u = it.next();

        tmp[0] = v[0] * u[0] % mod;
        tmp[1] = (u[1] * v[0] % mod + u[0] * v[1] % mod) % mod;
        tmp[2] = ((v[0] + v[1] + v[2]) % mod * u[2] % mod + v[1] * u[1] % mod + v[2] * u[0] % mod) % mod;

        u[0] = tmp[0];
        u[1] = tmp[1];
        u[2] = tmp[2];
      }
    }
    int ptr = 0;
    for (long[] u : ret) {
      if (ptr == maxDep)
        break;
      u[0] = (u[0] + u[2]) % mod;
      u[2] = 0;
      ptr++;
    }

    ret.addFirst(new long[] { 1, 1, 0 });
    return ret;
  }

  public static int[][] parentToChildren(int[] par) {
    int n = par.length;
    int[] ct = new int[n];
    for (int i = 0; i < n; i++) {
      if (par[i] >= 0) {
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
      }
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

  public static long invl(long a, long mod) {
    long b = mod;
    long p = 1, q = 0;
    while (b > 0) {
      long c = a / b;
      long d;
      d = a;
      a = b;
      b = d % b;
      d = p;
      p = q;
      q = d - c * q;
    }
    return p < 0 ? p + mod : p;
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
    }, "", 94000000).start();
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
