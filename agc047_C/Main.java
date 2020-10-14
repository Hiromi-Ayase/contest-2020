import java.util.*;

class FFT2 {
  public static long[] convolute(int[] a, int[] b) {
    int m = Integer.highestOneBit(Math.max(Math.max(a.length, b.length) - 1, 1)) << 2;
    double[][] fa = fft(a, m, false);
    double[][] fb = a == b ? fa : fft(b, m, false);
    for (int i = 0; i < m; i++) {
      double nfa0 = fa[0][i] * fb[0][i] - fa[1][i] * fb[1][i];
      double nfa1 = fa[0][i] * fb[1][i] + fa[1][i] * fb[0][i];
      fa[0][i] = nfa0;
      fa[1][i] = nfa1;
    }
    fft(fa[0], fa[1], true);
    long[] ret = new long[m];
    for (int i = 0; i < m; i++) {
      ret[i] = Math.round(fa[0][i]);
    }
    return ret;
  }

  public static double[][] fft(int[] srcRe, int n, boolean inverse) {
    int m = srcRe.length;
    double[] dstRe = new double[n];
    double[] dstIm = new double[n];
    for (int i = 0; i < m; i++) {
      dstRe[i] = srcRe[i];
    }

    int h = Integer.numberOfTrailingZeros(n);
    for (int i = 0; i < n; i++) {
      int rev = Integer.reverse(i) >>> 32 - h;
      if (i < rev) {
        double d = dstRe[i];
        dstRe[i] = dstRe[rev];
        dstRe[rev] = d;
      }
    }

    for (int s = 2; s <= n; s <<= 1) {
      int nt = s >>> 1;
      double theta = inverse ? -2 * Math.PI / s : 2 * Math.PI / s;
      double wRe = Math.cos(theta);
      double wIm = Math.sin(theta);
      for (int j = 0; j < n; j += s) {
        double wr = 1, wi = 0;
        for (int t = j; t < j + nt; t++) {
          int jp = t + nt;
          double re = dstRe[jp] * wr - dstIm[jp] * wi;
          double im = dstRe[jp] * wi + dstIm[jp] * wr;
          dstRe[jp] = dstRe[t] - re;
          dstIm[jp] = dstIm[t] - im;
          dstRe[t] += re;
          dstIm[t] += im;
          double nwre = wr * wRe - wi * wIm;
          double nwim = wr * wIm + wi * wRe;
          wr = nwre;
          wi = nwim;
        }
      }
    }

    if (inverse) {
      for (int i = 0; i < n; i++) {
        dstRe[i] /= n;
        dstIm[i] /= n;
      }
    }

    return new double[][] { dstRe, dstIm };
  }

  public static void fft(double[] re, double[] im, boolean inverse) {
    int n = re.length;
    int h = Integer.numberOfTrailingZeros(n);
    for (int i = 0; i < n; i++) {
      int rev = Integer.reverse(i) >>> 32 - h;
      if (i < rev) {
        double d = re[i];
        re[i] = re[rev];
        re[rev] = d;
        d = im[i];
        im[i] = im[rev];
        im[rev] = d;
      }
    }

    for (int s = 2; s <= n; s <<= 1) {
      int nt = s >>> 1;
      double theta = inverse ? -2 * Math.PI / s : 2 * Math.PI / s;
      double wRe = Math.cos(theta);
      double wIm = Math.sin(theta);
      for (int j = 0; j < n; j += s) {
        double wr = 1, wi = 0;
        for (int t = j; t < j + nt; t++) {
          int jp = t + nt;
          double lre = re[jp] * wr - im[jp] * wi;
          double lim = re[jp] * wi + im[jp] * wr;
          re[jp] = re[t] - lre;
          im[jp] = im[t] - lim;
          re[t] += lre;
          im[t] += lim;
          double nwre = wr * wRe - wi * wIm;
          double nwim = wr * wIm + wi * wRe;
          wr = nwre;
          wi = nwim;
        }
      }
    }

    if (inverse) {
      for (int i = 0; i < n; i++) {
        re[i] /= n;
        im[i] /= n;
      }
    }
  }

  /**
   * 2D FFT
   * 
   * @param a
   * @param b
   * @return
   * @notverified JAG13SpringF
   * @verified COCI2016R3F
   */
  public static double[][][] convolute(int[][] a, int[][] b) {
    int mh = Integer.highestOneBit(Math.max(1, Math.max(a.length, b.length) - 1)) << 2;
    int mw = Integer.highestOneBit(Math.max(1, Math.max(a[0].length, b[0].length) - 1)) << 2;
    int n = a.length;
    double[][] drow = new double[2][mw];
    double[][][] fa = new double[mh][][];
    for (int i = 0; i < n; i++) {
      fa[i] = fft(a[i], mw, false);
    }
    for (int i = n; i < mh; i++)
      fa[i] = drow;

    double[][][] tfa = new double[mw][][];
    for (int i = 0; i < mw; i++) {
      tfa[i] = new double[2][mh];
      for (int j = 0; j < mh; j++) {
        tfa[i][0][j] = fa[j][0][i];
        tfa[i][1][j] = fa[j][1][i];
      }
      fft(tfa[i][0], tfa[i][1], false);
    }

    double[][][] fb = new double[mh][][];
    for (int i = 0; i < n; i++) {
      fb[i] = fft(b[i], mw, false);
    }
    for (int i = n; i < mh; i++)
      fb[i] = drow;

    double[][][] tfb = new double[mw][][];
    for (int i = 0; i < mw; i++) {
      tfb[i] = new double[2][mh];
      for (int j = 0; j < mh; j++) {
        tfb[i][0][j] = fb[j][0][i];
        tfb[i][1][j] = fb[j][1][i];
      }
      fft(tfb[i][0], tfb[i][1], false);
    }

    for (int j = 0; j < mw; j++) {
      for (int i = 0; i < mh; i++) {
        double nfa0 = tfa[j][0][i] * tfb[j][0][i] - tfa[j][1][i] * tfb[j][1][i];
        double nfa1 = tfa[j][0][i] * tfb[j][1][i] + tfa[j][1][i] * tfb[j][0][i];
        tfa[j][0][i] = nfa0;
        tfa[j][1][i] = nfa1;
      }
      fft(tfa[j][0], tfa[j][1], true);
    }

    double[][][] r = new double[mh][2][mw];
    for (int j = 0; j < mh; j++) {
      for (int k = 0; k < mw; k++) {
        r[j][0][k] = tfa[k][0][j];
        r[j][1][k] = tfa[k][1][j];
      }
      fft(r[j][0], r[j][1], true);
    }

    return r;
  }
}

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int p = 200003;

    int root = primitiveRoot(p);

    int[] inv = new int[p - 1];
    int[] map = new int[p];
    inv[0] = 1;
    map[0] = -1;
    for (int i = 1; i <= p - 2; i++) {
      inv[i] = (int) ((long) inv[i - 1] * root % p);
      map[inv[i]] = i;
    }

    int[] b = new int[p - 1];
    long sq = 0;
    for (int i = 0; i < n; i++) {
      int v = ni();
      if (v == 0)
        continue;
      b[map[v]]++;

      sq += (long) v * v % p;
    }

    long ret = 0;
    long[] c = FFT2.convolute(b, b);

    long f = 1;
    for (int i = 0; i < c.length; i++) {
      ret += f * c[i];
      f = f * root % p;
    }

    ret = (ret - sq) / 2;

    System.out.println(ret);
  }

  public static long pow(long a, long n, long mod) {
    long ret = 1; // 1%mod if mod=1,n=0
    int x = 63 - Long.numberOfLeadingZeros(n);
    for (; x >= 0; x--) {
      ret = ret * ret % mod;
      if (n << ~x < 0)
        ret = ret * a % mod;
    }
    return ret;
  }

  private static int primitiveRoot(int m) {
    if (m == 2)
      return 1;
    if (m == 167772161)
      return 3;
    if (m == 469762049)
      return 3;
    if (m == 754974721)
      return 11;
    if (m == 998244353)
      return 3;

    int[] divs = new int[20];
    divs[0] = 2;
    int cnt = 1;
    int x = (m - 1) / 2;
    while (x % 2 == 0)
      x /= 2;
    for (int i = 3; (long) (i) * i <= x; i += 2) {
      if (x % i == 0) {
        divs[cnt++] = i;
        while (x % i == 0) {
          x /= i;
        }
      }
    }
    if (x > 1) {
      divs[cnt++] = x;
    }
    for (int g = 2;; g++) {
      boolean ok = true;
      for (int i = 0; i < cnt; i++) {
        if (pow(g, (m - 1) / divs[i], m) == 1) {
          ok = false;
          break;
        }
      }
      if (ok)
        return g;
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
