import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int t = ni();
    for (int i = 0; i < t; i++) {
      int n = ni();
      int[][] size = new int[n][3];
      for (int j = 0; j < n; j++) {
        size[j][0] = ni();
        size[j][1] = ni();
        size[j][2] = j;
      }
      var ret = solve(n, size);
      out.println(ret);
    }
  }

  private static String solve(int n, int[][] size) {
    int[] ret = new int[n];
    Arrays.fill(ret, -2);

    {
      Arrays.sort(size, (o1, o2) -> o1[0] - o2[0]);
      int wMin = Integer.MAX_VALUE;
      int wMinArg = -2;
      int ptr = 0;
      for (int[] v : size) {
        int h = v[0];

        while (ptr < n && size[ptr][0] < h) {
          if (wMin > size[ptr][1]) {
            wMinArg = size[ptr][2];
            wMin = size[ptr][1];
          }
          ptr++;
        }

        int w = v[1];
        if (wMin < w) {
          ret[v[2]] = wMinArg;
        }

      }
    }

    {
      int[][] size2 = Arrays.copyOf(size, n);
      Arrays.sort(size2, (o1, o2) -> o1[1] - o2[1]);
      int wMin = Integer.MAX_VALUE;
      int wMinArg = -1;
      int ptr = 0;
      for (int[] v : size2) {
        int h = v[0];
        int w = v[1];

        while (ptr < n && size[ptr][0] < w) {
          if (wMin > size[ptr][1]) {
            wMinArg = size[ptr][2];
            wMin = size[ptr][1];
          }
          ptr++;
        }

        if (wMin < h) {
          ret[v[2]] = wMinArg;
        }

      }
    }

    StringBuilder sb = new StringBuilder();
    for (int v : ret) {
      sb.append(v + 1 + " ");
    }
    return sb.substring(0, sb.length() - 1);
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
