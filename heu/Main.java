import java.util.Arrays;
import java.util.Random;

public class Main {

  private static int n = 26;
  private static int[][] s;
  private static int[] c;
  private static int d;

  private static void solve() {
    d = ni();
    c = na(n);
    s = ntable(d, n);

    long start = System.currentTimeMillis();
    long lap = start;
    XorShift rand = new XorShift();

    int[] state = new int[d];
    int[] bestState = new int[d];
    int bestScore = score(bestState);
    int currentScore = score(state);
    long step = 0;
    long timeLimit = 1800;

    long START_TEMP = 2500;
    long END_TEMP = 1;
    for (step = 0;; step++) {
      long time = lap - start;
      if (step % 1000 == 0) {
        lap = System.currentTimeMillis();

        if (lap - start >= timeLimit) {
          break;
        }
      }

      long temp = START_TEMP + (END_TEMP - START_TEMP) * time / timeLimit;

      boolean swap = rand.nextDouble() < 0.5;

      int k = swap ? rand.nextInt(d - 1) : rand.nextInt(d);
      int v = rand.nextInt(n);
      int scoreDiff = swap ? scoreDiff2(state, k) : scoreDiff(state, k, v);

      double probability = Math.exp(((double) scoreDiff) / temp);
      boolean forceNext = probability > rand.nextDouble();

      if (scoreDiff > 0 || forceNext) {
        currentScore += scoreDiff;
        if (bestScore < currentScore) {
          bestState = Arrays.copyOf(state, d);
          bestScore = currentScore;
        }
        if (swap) {
          int tmp = state[k];
          state[k] = state[k + 1];
          state[k + 1] = tmp;
        } else {
          state[k] = v;
        }
      }
    }

    for (int v : bestState) {
      out.println(v + 1);
    }

    System.err.printf("Step:%d Score:%d%n", step, bestScore);
    // System.err.printf("Step:%d Score:%d State:%s%n", step, score(bestState),
    // Arrays.toString(bestState));
  }

  public static class XorShift extends Random {
    private static final long serialVersionUID = 6806629989739663134L;
    private long x = 123456789, y = 362436069, z = 521288629, w = 88675123;

    public XorShift() {
      super();
      x = System.nanoTime();
    }

    public XorShift(long seed) {
      super(seed);
      x = seed;
    }

    public synchronized void setSeed(long seed) {
      super.setSeed(seed);
      x = seed;
    }

    protected int next(int bits) {
      long t = (x ^ x << 11) & (1L << 32) - 1;
      x = y;
      y = z;
      z = w;
      w = (w ^ w >>> 19 ^ t ^ t >>> 8) & (1L << 32) - 1;
      return (int) w >>> 32 - bits;
    }
  }

  private static int scoreDiff2(int[] state, int k) {
    int u = state[k];
    int v = state[k + 1];
    if (u == v)
      return 0;

    int ret = (s[k][v] + s[k + 1][u]) - (s[k][u] + s[k + 1][v]);
    int lastU1 = 0;
    int lastV1 = 0;
    int lastU2 = 0;
    int lastV2 = 0;

    for (int i = 0; i < d; i++) {
      int p = state[i];

      if (p == u) {
        lastU1 = i + 1;
      } else if (p == v) {
        lastV1 = i + 1;
      }

      if (i == k) {
        lastV2 = i + 1;
      } else if (i == k + 1) {
        lastU2 = i + 1;
      } else {
        if (p == u) {
          lastU2 = i + 1;
        } else if (p == v) {
          lastV2 = i + 1;
        }
      }

      ret += c[u] * (lastU2 - lastU1);
      ret += c[v] * (lastV2 - lastV1);
    }
    return ret;

  }

  private static int scoreDiff(int[] state, int k, int v) {
    int u = state[k];
    if (u == v) {
      return 0;
    }

    int ret = s[k][v] - s[k][u];
    int lastU1 = 0;
    int lastV1 = 0;
    int lastU2 = 0;
    int lastV2 = 0;

    for (int i = 0; i < d; i++) {
      int p = state[i];

      if (p == u) {
        lastU1 = i + 1;
      } else if (p == v) {
        lastV1 = i + 1;
      }

      if (i != k) {
        if (p == u) {
          lastU2 = i + 1;
        } else if (p == v) {
          lastV2 = i + 1;
        }
      } else {
        lastV2 = i + 1;
      }

      ret += c[u] * (lastU2 - lastU1);
      ret += c[v] * (lastV2 - lastV1);
    }
    return ret;
  }

  private static int[] last = new int[n];

  private static int score(int[] state) {
    Arrays.fill(last, 0);
    int ret = 0;
    for (int i = 0; i < d; i++) {
      int p = state[i];
      ret += s[i][p];
      last[p] = i + 1;
      for (int j = 0; j < n; j++) {
        ret -= c[j] * (i - last[j] + 1);

      }
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
