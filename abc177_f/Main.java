import java.util.*;

class SegmentTreeRMQ {
  public int M, H, N;
  public int[] st;

  public SegmentTreeRMQ(int n) {
    N = n;
    M = Integer.highestOneBit(Math.max(N - 1, 1)) << 2;
    H = M >>> 1;
    st = new int[M];
    Arrays.fill(st, 0, M, Integer.MAX_VALUE);
  }

  public SegmentTreeRMQ(int[] a) {
    N = a.length;
    M = Integer.highestOneBit(Math.max(N - 1, 1)) << 2;
    H = M >>> 1;
    st = new int[M];
    for (int i = 0; i < N; i++) {
      st[H + i] = a[i];
    }
    Arrays.fill(st, H + N, M, Integer.MAX_VALUE);
    for (int i = H - 1; i >= 1; i--)
      propagate(i);
  }

  public void update(int pos, int x) {
    st[H + pos] = x;
    for (int i = (H + pos) >>> 1; i >= 1; i >>>= 1)
      propagate(i);
  }

  private void propagate(int i) {
    st[i] = Math.min(st[2 * i], st[2 * i + 1]);
  }

  public int minx(int l, int r) {
    int min = Integer.MAX_VALUE;
    if (l >= r)
      return min;
    while (l != 0) {
      int f = l & -l;
      if (l + f > r)
        break;
      int v = st[(H + l) / f];
      if (v < min)
        min = v;
      l += f;
    }

    while (l < r) {
      int f = r & -r;
      int v = st[(H + r) / f - 1];
      if (v < min)
        min = v;
      r -= f;
    }
    return min;
  }

  public int min(int l, int r) {
    return l >= r ? 0 : min(l, r, 0, H, 1);
  }

  private int min(int l, int r, int cl, int cr, int cur) {
    if (l <= cl && cr <= r) {
      return st[cur];
    } else {
      int mid = cl + cr >>> 1;
      int ret = Integer.MAX_VALUE;
      if (cl < r && l < mid) {
        ret = Math.min(ret, min(l, r, cl, mid, 2 * cur));
      }
      if (mid < r && l < cr) {
        ret = Math.min(ret, min(l, r, mid, cr, 2 * cur + 1));
      }
      return ret;
    }
  }

  public int firstle(int l, int v) {
    int cur = H + l;
    while (true) {
      if (st[cur] <= v) {
        if (cur < H) {
          cur = 2 * cur;
        } else {
          return cur - H;
        }
      } else {
        cur++;
        if ((cur & cur - 1) == 0)
          return -1;
        if ((cur & 1) == 0)
          cur >>>= 1;
      }
    }
  }

  public int lastle(int l, int v) {
    int cur = H + l;
    while (true) {
      if (st[cur] <= v) {
        if (cur < H) {
          cur = 2 * cur + 1;
        } else {
          return cur - H;
        }
      } else {
        if ((cur & cur - 1) == 0)
          return -1;
        cur--;
        if ((cur & 1) == 1)
          cur >>>= 1;
      }
    }
  }
}

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int h = ni();
    int w = ni();
    int[][] p = ntable(h, 2);

    TreeSet<Integer> set = new TreeSet<>();
    for (int i = 0; i < w; i++) {
      set.add(i);
    }
    int down = 0;

    SegmentTreeRMQ st = new SegmentTreeRMQ(new int[w]);

    for (int[] q : p) {
      int l = q[0] - 1;
      int r = q[1] - 1;
      down++;

      if (r == w - 1) {
        while (true) {
          Integer v = set.ceiling(l);
          if (v == null)
            break;
          set.remove(v);
          st.update(v, Integer.MAX_VALUE / 2);
        }
      } else {
        Integer v = set.floor(r);
        int minMove = Integer.MAX_VALUE / 2;
        if (v != null && l <= v) {
          while (true) {
            Integer u = set.ceiling(l);
            if (u == null || u > v)
              break;
            set.remove(u);
            int move = st.st[st.H + u];
            st.update(u, Integer.MAX_VALUE / 2);

            minMove = Math.min(move + Math.abs(r + 1 - u), minMove);
          }

          int nv = r + 1;
          if (!set.contains(nv) || minMove < st.st[st.H]) {
            st.update(nv, minMove);
          }
          set.add(nv);
        }
      }

      if (set.isEmpty()) {
        System.out.println(-1);
      } else {
        System.out.println(down + st.min(0, w));
      }
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
