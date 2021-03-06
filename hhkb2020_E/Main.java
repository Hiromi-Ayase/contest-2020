import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int h = ni();
    int w = ni();
    int emp = 0;
    char[][] map = new char[h + 2][w + 2];
    for (int i = 1; i <= h; i++) {
      char[] line = ns();
      for (int j = 1; j <= w; j++) {
        map[i][j] = line[j - 1];
        if (map[i][j] == '.') {
          emp++;
        }
      }
    }

    int[][][] cnt = new int[h + 2][w + 2][4];
    for (int i = 1; i <= h; i++) {
      int v = 0;
      for (int j = 1; j <= w; j++) {
        if (map[i][j] == '.') {
          v++;
        } else {
          v = 0;
        }
        cnt[i][j][0] = v;
      }
      v = 0;
      for (int j = w; j >= 1; j--) {
        if (map[i][j] == '.') {
          v++;
        } else {
          v = 0;
        }
        cnt[i][j][1] = v;
      }
    }
    for (int j = 1; j <= w; j++) {
      int v = 0;
      for (int i = 1; i <= h; i++) {
        if (map[i][j] == '.') {
          v++;
        } else {
          v = 0;
        }
        cnt[i][j][2] = v;
      }
      v = 0;
      for (int i = h; i >= 1; i--) {
        if (map[i][j] == '.') {
          v++;
        } else {
          v = 0;
        }
        cnt[i][j][3] = v;
      }
    }
    long ret = 0;

    int mod = (int) 1e9 + 7;
    long[] p2 = new long[h * w + 1];
    p2[0] = 1;
    for (int i = 1; i < p2.length; i++) {
      p2[i] = p2[i - 1] * 2 % mod;
    }
    for (int i = 1; i <= h; i++) {
      for (int j = 1; j <= w; j++) {
        if (map[i][j] != '.')
          continue;
        int v = 0;
        for (int k = 0; k < 4; k++) {
          v += cnt[i][j][k];
        }
        v -= 3;

        ret += p2[emp - v] * (p2[v] - 1) % mod;
        ret %= mod;
      }
    }
    System.out.println(ret);
  }

  public static long pow(long a, long n, long mod) {
    // a %= mod;
    long ret = 1; // 1%mod if mod=1,n=0
    int x = 63 - Long.numberOfLeadingZeros(n);
    for (; x >= 0; x--) {
      ret = ret * ret % mod;
      if (n << ~x < 0)
        ret = ret * a % mod;
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
