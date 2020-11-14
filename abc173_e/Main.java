import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int k = ni();

    long[][] a = new long[n][2];
    for (int i = 0; i < n; i++) {
      a[i][0] = nl();
      a[i][1] = Math.abs(a[i][0]);
    }

    Arrays.sort(a, (o1, o2) -> Long.signum(o2[1] - o1[1]));

    int fcnt = 0;
    long ret = 1;
    int mod = (int) 1e9 + 7;

    int lastPosi = -1;
    int lastNega = -1;
    for (int i = 0; i < k; i++) {
      if (a[i][0] < 0) {
        fcnt++;
      }
      if (a[i][0] < 0) {
        lastNega = i;
      }
      if (a[i][0] > 0) {
        lastPosi = i;
      }

      ret *= a[i][1];
      ret %= mod;
    }

    if (fcnt % 2 == 0) {
      System.out.println(ret);
    } else if (ret == 0) {
      System.out.println(0);
    } else {
      int nextNega = -1;
      int nextPosi = -1;
      for (int i = k; i < n; i++) {
        if (nextNega < 0 && a[i][0] < 0) {
          nextNega = i;
        }
        if (nextPosi < 0 && a[i][0] > 0) {
          nextPosi = i;
        }
      }

      if (lastPosi >= 0 && nextNega >= 0 && lastNega >= 0 && nextPosi >= 0) {
        if (a[lastPosi][1] * a[nextPosi][1] < a[lastNega][1] * a[nextNega][1]) {
          ret = ret * invl(a[lastPosi][1], mod) % mod * a[nextNega][1] % mod;
        } else {
          ret = ret * invl(a[lastNega][1], mod) % mod * a[nextPosi][1] % mod;
        }
      } else if (lastPosi >= 0 && nextNega >= 0) {
        ret = ret * invl(a[lastPosi][1], mod) % mod * a[nextNega][1] % mod;
      } else if (lastNega >= 0 && nextPosi >= 0) {
        ret = ret * invl(a[lastNega][1], mod) % mod * a[nextPosi][1] % mod;
      } else {
        ret = 1;
        for (int i = 0; i < k; i++) {
          ret *= a[n - i - 1][1];
          ret %= mod;
        }
        ret = (mod - ret) % mod;
      }
      System.out.println(ret);
    }
  }

  public static long invl(long a, long mod) {
    long b = mod;
    long p = 1, q = 0;
    while (b > 0) {
      long c = a / b;
      long d;
      d = a;
      a = b;
      b = d % b;
      d = p;
      p = q;
      q = d - c * q;
    }
    return p < 0 ? p + mod : p;
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
