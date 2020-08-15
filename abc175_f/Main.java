import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class Main {

  private static void solve() {
    int n = ni();
    String[] s = new String[n];
    long[] c = new long[n];

    for (int i = 0; i < n; i++) {
      s[i] = next();
      c[i] = ni();
    }

    System.out.println(dijk(s, c));
  }

  public static long dijk(String[] s, long[] c) {
    int n = s.length;
    Map<String, Long> d = new HashMap<>();

    TreeSet<String> q = new TreeSet<>((o1, o2) -> {
      long d1 = d.getOrDefault(o1, Long.MAX_VALUE);
      long d2 = d.getOrDefault(o2, Long.MAX_VALUE);

      if (d1 == d2) {
        return o1.compareTo(o2);
      } else {
        return Long.compare(d1, d2);
      }
    });

    d.put("", 0L);
    q.add("");

    while (q.size() > 0) {
      String cur = q.pollFirst();
      if (cur.length() > 100)
        continue;
      if (isPalindrome(cur)) {
        return d.get(cur);
      }

      for (int i = 0; i < n; i++) {
        String next = cur + s[i];
        long nd = d.get(cur) + c[i];
        if (nd < d.getOrDefault(next, Long.MAX_VALUE)) {
          q.remove(next);
          d.put(next, nd);
          q.add(next);
        }
      }
    }

    return -1;
  }

  private static boolean isPalindrome(String s) {
    if (s.length() == 0)
      return false;
    for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
      if (s.charAt(i) != s.charAt(j)) {
        return false;
      }
    }
    return true;
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
