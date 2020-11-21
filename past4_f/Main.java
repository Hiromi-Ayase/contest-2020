import java.util.*;

@SuppressWarnings("unused")
public class Main {

  static class Node {
    String s;
    int cnt;

    Node(String s, int cnt) {
      this.s = s;
      this.cnt = cnt;
    }
  }

  private static void solve() {
    int n = ni();
    int k = ni();

    Map<String, Node> map = new HashMap<>();
    for (int i = 0; i < n; i++) {
      String s = next();
      map.putIfAbsent(s, new Node(s, 0));
      map.compute(s, (key, val) -> {
        val.cnt++;
        return val;
      });
    }
    List<Node> list = new ArrayList<>(map.values());
    Collections.sort(list, (o1, o2) -> o2.cnt - o1.cnt);

    Node ret = list.get(k - 1);
    if (k < list.size()) {
      if (list.get(k).cnt == ret.cnt) {
        System.out.println("AMBIGUOUS");
        return;
      }
    }
    if (k > 1) {
      if (list.get(k - 2).cnt == ret.cnt) {
        System.out.println("AMBIGUOUS");
        return;
      }
    }
    System.out.println(ret.s);
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
