
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

class SegmentTreeRMQL {
  public int M, H, N;
  public long[] st;

  public SegmentTreeRMQL(int n) {
    N = n;
    M = Integer.highestOneBit(Math.max(N - 1, 1)) << 2;
    H = M >>> 1;
    st = new long[M];
    Arrays.fill(st, 0, M, Long.MAX_VALUE);
  }

  public SegmentTreeRMQL(long[] a) {
    N = a.length;
    M = Integer.highestOneBit(Math.max(N - 1, 1)) << 2;
    H = M >>> 1;
    st = new long[M];
    for (int i = 0; i < N; i++) {
      st[H + i] = a[i];
    }
    Arrays.fill(st, H + N, M, Long.MAX_VALUE);
    for (int i = H - 1; i >= 1; i--)
      propagate(i);
  }

  public void update(int pos, long x) {
    st[H + pos] = x;
    for (int i = (H + pos) >>> 1; i >= 1; i >>>= 1)
      propagate(i);
  }

  private void propagate(int i) {
    st[i] = Math.min(st[2 * i], st[2 * i + 1]);
  }

  public long minx(int l, int r) {
    long min = Long.MAX_VALUE;
    if (l >= r)
      return min;
    while (l != 0) {
      int f = l & -l;
      if (l + f > r)
        break;
      long v = st[(H + l) / f];
      if (v < min)
        min = v;
      l += f;
    }

    while (l < r) {
      int f = r & -r;
      long v = st[(H + r) / f - 1];
      if (v < min)
        min = v;
      r -= f;
    }
    return min;
  }

  public long min(int l, int r) {
    return l >= r ? 0 : min(l, r, 0, H, 1);
  }

  private long min(int l, int r, int cl, int cr, int cur) {
    if (l <= cl && cr <= r) {
      return st[cur];
    } else {
      int mid = cl + cr >>> 1;
      long ret = Long.MAX_VALUE;
      if (cl < r && l < mid) {
        ret = Math.min(ret, min(l, r, cl, mid, 2 * cur));
      }
      if (mid < r && l < cr) {
        ret = Math.min(ret, min(l, r, mid, cr, 2 * cur + 1));
      }
      return ret;
    }
  }
}

public class Main1 {
  private static final String INPUT_ID = "running_on_fumes_chapter_1";
  private static final int STATE = 2;

  public static String solve() {
    int n = ni();
    int m = ni();
    long[] c = nal(n);
    SegmentTreeRMQL st = new SegmentTreeRMQL(n + 1);
    st.update(0, 0);

    for (int i = 1; i < n - 1; i++) {
      if (c[i] > 0) {
        long min = st.min(Math.max(0, i - m), i);
        if (min >= Long.MAX_VALUE / 2)
          continue;
        st.update(i, c[i] + min);
      }
    }
    long ret = st.min(Math.max(0, n - 1 - m), n);
    return (ret >= Long.MAX_VALUE / 2 ? -1 : ret) + "";
  }

  public static String batch() {
    int t = ni();
    StringBuilder ret = new StringBuilder();
    for (int i = 0; i < t; i++) {
      String v = solve();
      String line = "Case #" + (i + 1) + ": " + v;
      System.out.println(line);
      ret.append(line);
      ret.append("\n");
    }
    return ret.toString();
  }

  public static void main(String[] args) throws IOException {
    String id = INPUT_ID;
    String[] suffix = { "_sample_input.txt", "_validation_input.txt", "_input.txt" };
    Path input = Paths.get(id + suffix[STATE]);
    new Thread(null, new Runnable() {
      @Override
      public void run() {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("output.txt"), StandardCharsets.UTF_8)) {
          reader = Files.newBufferedReader(input, StandardCharsets.UTF_8);
          String ret = batch();
          bw.write(ret);

          if (STATE == 0) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = Files.newBufferedReader(Paths.get(id + "_sample_output.txt"),
                StandardCharsets.UTF_8)) {
              String line;
              while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
              }
            }
            System.out.println();
            System.out.println("Sample output:");
            System.out.println(sb);
            System.out.println(sb.toString().equals(ret) ? "OK!" : "NG!");
          }
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }, "", 64000000).start();
  }

  private static StringTokenizer tokenizer = null;
  private static BufferedReader reader;

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
}
