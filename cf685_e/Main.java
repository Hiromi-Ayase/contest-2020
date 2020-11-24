import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static int askXor(int i, int j) {
    out.println("XOR " + i + " " + j);
    out.flush();
    return ni();
  }

  private static int askOr(int i, int j) {
    out.println("OR " + i + " " + j);
    out.flush();
    return ni();
  }

  private static int askAnd(int i, int j) {
    out.println("AND " + i + " " + j);
    out.flush();
    return ni();
  }

  private static void ans(int[] a) {
    out.print("!");
    for (int v : a) {
      out.print(" " + v);
    }
    out.println();
    out.flush();
  }

  private static void solve() {
    int n = ni();
    int[] a = new int[n];

    int[] xor = new int[n + 1];
    int[] same = null;

    int[] xorInd = new int[n + 1];
    Arrays.fill(xorInd, -1);
    xorInd[0] = 1;
    for (int i = 2; i <= n; i++) {
      xor[i] = askXor(1, i);
      if (xorInd[xor[i]] >= 0) {
        same = new int[] { xorInd[xor[i]], i };
      }
      xorInd[xor[i]] = i;
    }

    if (same != null) {
      a[0] = askAnd(same[0], same[1]) ^ xor[same[0]];
    } else {
      int x = askAnd(1, xorInd[1]);
      int y = askAnd(1, xorInd[2]);
      int mask = ((1 << 20) - 1) ^ 3;
      a[0] = (x & mask) | (x & 2) | (y & 1);
    }

    for (int i = 1; i <= n; i++) {
      a[i - 1] = xor[i] ^ a[0];
    }
    ans(a);
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
