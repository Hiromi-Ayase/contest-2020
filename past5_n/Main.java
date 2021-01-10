import java.util.*;

@SuppressWarnings("unused")
public class Main {

  private static void solve() {
    int n = ni();
    int q = ni();
    int[][] p = ntable(n - 1, 2);

    List<List<int[]>> list = new ArrayList<>();
    List<List<int[]>> left = new ArrayList<>();
    List<List<int[]>> right = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      list.add(new ArrayList<>());
      left.add(new ArrayList<>());
      right.add(new ArrayList<>());
    }

    for (int i = 0; i < q; i++) {
      int a = ni();
      int b = ni() - 1;
      list.get(b).add(new int[] { i, a, b });
    }

    {
      TreeSet<int[]> set = new TreeSet<>((o1, o2) -> {
        if (o1[1] == o2[1])
          return o1[0] - o2[0];
        else
          return o1[1] - o2[1];
      });

      for (int i = 0; i < n - 1; i++) {
        int l = p[i][0];
        int r = p[i][1];

        for (int[] v : list.get(i)) {
          set.add(v);
        }

        while (set.size() > 0 && set.first()[1] < l) {
          right.get(i).add(set.pollFirst());
        }

        while (set.size() > 0 && set.last()[1] > r) {
          right.get(i).add(set.pollLast());
        }
      }
      for (int[] v : list.get(n - 1)) {
        set.add(v);
      }
      while (set.size() > 0) {
        right.get(n - 1).add(set.pollLast());
      }
    }

    {
      TreeSet<int[]> set = new TreeSet<>((o1, o2) -> {
        if (o1[1] == o2[1])
          return o1[0] - o2[0];
        else
          return o1[1] - o2[1];
      });

      for (int i = n - 1; i > 0; i--) {
        int l = p[i - 1][0];
        int r = p[i - 1][1];

        for (int[] v : list.get(i)) {
          set.add(v);
        }

        while (set.size() > 0 && set.first()[1] < l) {
          left.get(i).add(set.pollFirst());
        }

        while (set.size() > 0 && set.last()[1] > r) {
          left.get(i).add(set.pollLast());
        }
      }
      for (int[] v : list.get(0)) {
        set.add(v);
      }

      while (set.size() > 0) {
        left.get(0).add(set.pollLast());
      }
    }

    int[] ret = new int[q];
    for (int i = 0; i < n; i++) {
      for (int[] v : left.get(i)) {
        ret[v[0]] = i;
      }
      for (int[] v : right.get(i)) {
        ret[v[0]] = i - ret[v[0]] + 1;
      }
    }

    for (int i = 0; i < q; i++) {
      out.println(ret[i]);
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
