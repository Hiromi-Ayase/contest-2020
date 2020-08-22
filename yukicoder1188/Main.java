import java.util.Arrays;

public class Main {

  private static void solve() {
    int n = ni();
    int[] a = na(n);
    int[] b = new int[n];
    int[] inva = new int[n];
    int[] invb = new int[n];
    for (int i = 0; i < n; i++) {
      b[i] = -a[i];
      inva[n - i - 1] = a[i];
      invb[n - i - 1] = b[i];
    }

    int[] al1 = lis(a);
    int[] bl1 = lis(b);
    int[] al2 = lis(inva);
    int[] bl2 = lis(invb);

    for (int i = 0, j = n - 1; i < j; i++, j--) {
      {
        int tmp = al2[i];
        al2[i] = al2[j];
        al2[j] = tmp;
      }
      {
        int tmp = bl2[i];
        bl2[i] = bl2[j];
        bl2[j] = tmp;
      }
    }

    int ret = 0;
    for (int i = 0; i < n; i++) {
      ret = Math.max(ret, Math.min(bl1[i], bl2[i]) - 1);
      ret = Math.max(ret, Math.min(al1[i], al2[i]) - 1);
    }
    System.out.println(ret);
  }

  public static int[] lis(int[] in) {
    int n = in.length;
    int[] ret = new int[n];
    int now = 0;
    int[] h = new int[n + 1];
    Arrays.fill(h, Integer.MIN_VALUE / 2);
    for (int i = 0; i < n; i++) {
      int ind = Arrays.binarySearch(h, 0, now + 1, in[i]);
      if (ind < 0) {
        ind = -ind - 2;
        h[ind + 1] = in[i];
        if (ind >= now)
          now++;
      }
      ret[i] = now;
    }
    return ret;
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
