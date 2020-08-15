import java.util.Arrays;

public class Main {

  private static void solve() {
    int n = ni();

    int[][] p = new int[n][3];

    for (int i = 0; i < n; i++) {
      p[i][0] = ni();
      p[i][1] = ni();
      p[i][2] = ns()[0];
    }

    int ret = Integer.MAX_VALUE;

    int m = 200001;
    {
      Arrays.sort(p, (o1, o2) -> o1[0] - o2[0]);
      int[] hit = new int[m];
      Arrays.fill(hit, -1);
      for (int i = 0; i < n; i++) {
        char u = (char) p[i][2];
        int x = p[i][0];
        int y = p[i][1];

        if (u == 'R') {
          hit[y] = x;
        } else if (u == 'L') {
          if (hit[y] >= 0) {
            ret = Math.min(ret, (x - hit[y]) * 5);
          }
        }
      }
    }
    {
      Arrays.sort(p, (o1, o2) -> o1[1] - o2[1]);
      int[] hit = new int[m];
      Arrays.fill(hit, -1);
      for (int i = 0; i < n; i++) {
        char u = (char) p[i][2];
        int x = p[i][0];
        int y = p[i][1];

        if (u == 'U') {
          hit[x] = y;
        } else if (u == 'D') {
          if (hit[x] >= 0) {
            ret = Math.min(ret, (y - hit[x]) * 5);
          }
        }
      }
    }
    {
      Arrays.sort(p, (o1, o2) -> o1[0] - o2[0]);
      int[] hit1 = new int[m + m];
      int[] hit2 = new int[m + m];
      Arrays.fill(hit1, -1);
      Arrays.fill(hit2, -1);
      for (int i = 0; i < n; i++) {
        char u = (char) p[i][2];
        int x = p[i][0];
        int y = p[i][1];

        if (u == 'R') {
          hit1[x + y] = x;
          hit2[x - y + m] = x;
        } else if (u == 'U') {
          if (hit1[x + y] >= 0) {
            ret = Math.min(ret, (x - hit1[x + y]) * 10);
          }
        } else if (u == 'D') {
          if (hit2[x - y + m] >= 0) {
            ret = Math.min(ret, (x - hit2[x - y + m]) * 10);
          }
        }
      }
    }
    {
      Arrays.sort(p, (o1, o2) -> o2[0] - o1[0]);
      int[] hit1 = new int[m + m];
      int[] hit2 = new int[m + m];
      Arrays.fill(hit1, -1);
      Arrays.fill(hit2, -1);
      for (int i = 0; i < n; i++) {
        char u = (char) p[i][2];
        int x = p[i][0];
        int y = p[i][1];

        if (u == 'L') {
          hit1[x + y] = x;
          hit2[x - y + m] = x;
        } else if (u == 'D') {
          if (hit1[x + y] >= 0) {
            ret = Math.min(ret, Math.abs(x - hit1[x + y]) * 10);
          }
        } else if (u == 'U') {
          if (hit2[x - y + m] >= 0) {
            ret = Math.min(ret, Math.abs(x - hit2[x - y + m]) * 10);
          }
        }
      }
    }
    System.out.println(ret == Integer.MAX_VALUE ? "SAFE" : "" + ret);

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
