import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int[] a = na(n);
    int[] b = na(n);
    int[] p = na(n);

    // {no, 体重, 今持ってるの}
    int[][] c = new int[n][3];

    for (int i = 0; i < n; i++) {
      p[i]--;

      c[i][0] = i;
      c[i][1] = a[i];
      c[i][2] = p[i];

      if (a[i] <= b[p[i]] && p[i] != i) {
        System.out.println(-1);
        return;
      }
    }

    Arrays.sort(c, (o1, o2) -> o1[1] - o2[1]);
    int[] inv = new int[n];

    StringBuilder sb = new StringBuilder();
    int cnt = 0;
    for (int i = 0; i < n; i++) {
      inv[c[i][2]] = i;
    }

    // swap
    for (int i = 0; i < n; i++) {
      int me = c[i][0];
      int mine = c[i][2];
      if (me == mine)
        continue;

      cnt++;
      int yours = c[inv[me]][2];
      int you = c[inv[me]][0];
      sb.append((me + 1) + " " + (you + 1) + "\n");
      c[i][2] = yours;
      c[inv[me]][2] = mine;

      int tmp = inv[yours];
      inv[yours] = inv[mine];
      inv[mine] = tmp;
    }

    System.out.println(cnt);
    System.out.println(sb);
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
