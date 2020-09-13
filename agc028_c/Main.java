import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();

    int[][] p = new int[n * 2][3];
    int amin = Integer.MAX_VALUE;
    int bmin = Integer.MAX_VALUE;
    int amax = 0;
    int bmax = 0;
    for (int i = 0; i < n; i++) {
      int a = ni();
      int b = ni();
      p[i * 2 + 0] = new int[] { a, i, 0 };
      p[i * 2 + 1] = new int[] { b, i, 1 };

      amax = Math.max(amax, a);
      amin = Math.min(amin, a);
      bmax = Math.max(bmax, b);
      bmin = Math.min(bmin, b);
    }
    Arrays.sort(p, (o1, o2) -> o1[0] - o2[0]);

    long ret = 0;
    for (int i = 0; i < n; i++) {
      ret += p[i][0];
    }
    if (amax <= bmin || bmax <= amin) {
      System.out.println(ret);
      return;
    } else {
      Set<Integer> half = new HashSet<>();
      for (int i = 0; i < n; i++) {
        half.add(p[i][1]);
      }
      if (half.size() < n) {
        System.out.println(ret);
        return;
      }
      int ptr = n - 1;
      int cnt = 0;
      while (ptr >= 0 && p[ptr][0] == p[n - 1][0]) {
        ptr--;
        cnt++;
      }

      if (cnt >= 2 || p[n][1] != p[n - 1][1]) {
        ret += p[n][0] - p[n - 1][0];
        System.out.println(ret);
      } else {
        int x = p[n - 2][1] != p[n + 0][1] ? p[n + 0][0] - p[n - 2][0] : Integer.MAX_VALUE;
        int y = p[n - 1][1] != p[n + 1][1] ? p[n + 1][0] - p[n - 1][0] : Integer.MAX_VALUE;
        ret += Math.min(x, y);
        System.out.println(ret);
      }
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
