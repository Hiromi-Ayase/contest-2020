import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int m = ni();
    int[] from = new int[m];
    int[] to = new int[m];
    int[] deg = new int[n];
    for (int i = 0; i < m; i++) {
      from[i] = ni() - 1;
      to[i] = ni() - 1;
      deg[from[i]]++;
      deg[to[i]]++;
    }

    int max = 1000;

    int[] from2 = new int[m * 2];
    int[] to2 = new int[m * 2];
    int ptr = 0;
    for (int i = 0; i < m; i++) {
      if (deg[to[i]] > max) {
        from2[ptr] = from[i];
        to2[ptr] = to[i];
        ptr++;
      }
      if (deg[from[i]] > max) {
        to2[ptr] = from[i];
        from2[ptr] = to[i];
        ptr++;
      }
    }
    from2 = Arrays.copyOf(from2, ptr);
    to2 = Arrays.copyOf(to2, ptr);
    int[][] g = packU(n, from, to);
    int[][] lg = packD(n, from2, to2);

    long inf = 100000000;

    Map<Integer, Integer> mail = new HashMap<>();
    Map<Long, Integer> done = new HashMap<>();
    for (int i = 0; i < n; i++) {
      if (deg[i] > max) {
        mail.put(i, 0);
      }
      for (int nex : lg[i]) {
        long h = nex * inf + i;
        done.put(h, 0);
      }
    }

    int[] a = new int[n];
    int q = ni();
    for (int i = 0; i < q; i++) {
      int t = ni();
      int x = ni() - 1;
      if (t == 1) {
        if (mail.containsKey(x)) {
          mail.compute(x, (key, val) -> val + 1);
        } else {
          for (int nex : g[x]) {
            a[nex]++;
          }
        }
      } else {
        int ret = a[x];
        for (int nex : lg[x]) {
          int largeMail = mail.get(nex);
          long h = nex * inf + x;
          int largeRead = done.get(h);
          ret += largeMail - largeRead;
          done.put(h, largeMail);
        }
        a[x] = 0;
        out.println(ret);
      }
    }
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
