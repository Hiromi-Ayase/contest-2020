import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class Main {

  private static void solve() {
    int n = ni();
    int q = ni();

    Map<Integer, TreeSet<Integer>> map = new HashMap<>();
    int[] rate = new int[n];
    Comparator<Integer> cmp = (o1, o2) -> rate[o1] != rate[o2] ? rate[o1] - rate[o2] : o1 - o2;
    int[] ytoe = new int[n];
    for (int i = 0; i < n; i++) {
      int a = ni();
      int b = ni();
      rate[i] = a;
      ytoe[i] = b;

      map.putIfAbsent(b, new TreeSet<>(cmp));
      map.get(b).add(i);
    }

    TreeSet<TreeSet<Integer>> queue = new TreeSet<>((o1, o2) -> {
      return rate[o1.last()] == rate[o2.last()] ? o1.last() - o2.last() : rate[o1.last()] - rate[o2.last()];
    });

    for (TreeSet<Integer> cur : map.values()) {
      queue.add(cur);
    }

    for (int i = 0; i < q; i++) {
      int c = ni() - 1;
      int d = ni();
      int e = ytoe[c];
      ytoe[c] = d;

      TreeSet<Integer> cur = map.get(e);
      TreeSet<Integer> nex = map.get(d);
      if (nex == null) {
        map.putIfAbsent(d, new TreeSet<>(cmp));
        nex = map.get(d);
      } else {
        queue.remove(nex);
      }
      queue.remove(cur);

      cur.remove(c);
      nex.add(c);

      queue.add(nex);
      if (cur.size() > 0) {
        queue.add(cur);
      } else {
        map.remove(e);
      }
      out.println(rate[queue.first().last()]);
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
