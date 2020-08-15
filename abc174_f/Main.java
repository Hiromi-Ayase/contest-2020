import java.util.Arrays;

public class Main {

  private static void solve() {
    int n = ni();
    int q = ni();
    int[] a = na(n);
    int[][] qs = new int[q][3];
    for (int i = 0; i < q; i++) {
      qs[i][0] = i;
      qs[i][1] = ni() - 1;
      qs[i][2] = ni() - 1;
    }
    for (int i = 0; i < n; i++) {
      a[i]--;
    }

    Arrays.sort(qs, (o1, o2) -> o1[2] - o2[2]);
    int[] last = new int[n];
    Arrays.fill(last, -1);

    int[] ft = new int[n + 1];
    int[] ret = new int[q];

    int rp = 0;
    for (int i = 0; i < q; i++) {
      int l = qs[i][1];
      int r = qs[i][2];

      while (rp <= r) {
        if (last[a[rp]] >= 0) {
          addFenwick(ft, last[a[rp]], -1);
        }
        last[a[rp]] = rp;
        addFenwick(ft, last[a[rp]], 1);
        rp++;
      }
      ret[qs[i][0]] = sumFenwick(ft, r) - sumFenwick(ft, l - 1);
    }

    for (int v : ret) {
      out.println(v);
    }
  }

  public static int sumFenwick(int[] ft, int i) {
    int sum = 0;
    for (i++; i > 0; i -= i & -i)
      sum += ft[i];
    return sum;
  }

  public static void addFenwick(int[] ft, int i, int v) {
    if (v == 0 || i < 0)
      return;
    int n = ft.length;
    for (i++; i < n; i += i & -i)
      ft[i] += v;
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
