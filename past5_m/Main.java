import java.util.*;

@SuppressWarnings("unused")
class SegmentTreeRSQ {
  public int M, H, N;
  public long[] st;
  public long[] plus;

  public SegmentTreeRSQ(int n) {
    N = n;
    M = Integer.highestOneBit(Math.max(N - 1, 1)) << 2;
    H = M >>> 1;
    st = new long[M];
    plus = new long[H];
  }

  public SegmentTreeRSQ(int[] a) {
    N = a.length;
    M = Integer.highestOneBit(Math.max(N - 1, 1)) << 2;
    H = M >>> 1;
    st = new long[M];
    plus = new long[H];
    for (int i = 0; i < N; i++) {
      st[H + i] = a[i];
    }
    for (int i = (M >> 1) - 1; i >= 1; i--) {
      propagate(i);
    }
  }

  public void add(int pos, int v) {
    for (int i = H + pos; i >= 1; i >>>= 1) {
      st[i] += v;
    }
  }

  private void propagate(int i) {
    int count = H / Integer.highestOneBit(i);
    st[i] = st[2 * i] + st[2 * i + 1] + plus[i] * count;
  }

  public void add(int l, int r, int v) {
    if (l < r)
      add(l, r, v, 0, H, 1);
  }

  private void add(int l, int r, int v, int cl, int cr, int cur) {
    if (cur >= H) {
      st[cur] += v;
    } else if (l <= cl && cr <= r) {
      plus[cur] += v;
      propagate(cur);
    } else {
      int mid = cl + cr >>> 1;
      if (cl < r && l < mid) {
        add(l, r, v, cl, mid, 2 * cur);
      }
      if (mid < r && l < cr) {
        add(l, r, v, mid, cr, 2 * cur + 1);
      }
      propagate(cur);
    }
  }

  private long gsum;

  public long sum(int l, int r) {
    gsum = 0;
    sum(l, r, 0, H, 1);
    return gsum;
  }

  private void sum(int l, int r, int cl, int cr, int cur) {
    if (l <= cl && cr <= r) {
      gsum += st[cur];
    } else {
      int mid = cl + cr >>> 1;
      if (cl < r && l < mid) {
        sum(l, r, cl, mid, 2 * cur);
      }
      if (mid < r && l < cr) {
        sum(l, r, mid, cr, 2 * cur + 1);
      }
      gsum += plus[cur] * (Math.min(r, cr) - Math.max(l, cl));
    }
  }
}

public class Main {

  private static void solve() {
    int n = ni();
    long l = nl();
    long[] a = nal(n);

    long ok = 1;
    long ng = l + 1;

    while (ng - ok > 1) {
      long k = (ng + ok) / 2;

      int lp = 0;
      int rp = 0;
      SegmentTreeRSQ st = new SegmentTreeRSQ(n + 1);
      st.add(0, 1);

      long nowL = 0;
      long nowR = 0;
      for (int i = 0; i < n; i++) {
        nowL += a[i];
        nowR += a[i];

        while (nowL > l) {
          nowL -= a[lp];
          lp++;
        }
        while (nowR >= k) {
          nowR -= a[rp];
          rp++;
        }
        if (st.sum(lp, rp) > 0) {
          st.add(i + 1, 1);
        }
      }

      if (st.sum(n, n + 1) > 0) {
        ok = k;
      } else {
        ng = k;
      }
    }
    System.out.println(ok);
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
