
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  private static final String INPUT_ID = "/home/ubuntu/workspaces/contests/fhc2020_r1_B/dislodging_logs";
  // 0:Sample 1:Validation 2:Submit
  private static final int STATE = 2;

  public static String solve() {
    int n = ni();
    int m = ni();
    int k = ni();
    int s = ni();
    int[] p = new int[n];
    int[] q = new int[m];
    inputArray(p, k);
    inputArray(q, k);

    Arrays.sort(p);
    Arrays.sort(q);

    int ok = Integer.MAX_VALUE;
    int ng = 0;

    while (ok - ng > 1) {
      int time = (ok + ng) / 2;
      if (check(p, q, time)) {
        ok = time;
      } else {
        ng = time;
      }
    }
    return "" + ok;
  }

  // p: log driver, q:logs
  private static boolean check(int[] p, int[] q, int time) {
    int pt = 0;
    int n = p.length;
    int m = q.length;

    for (int i = 0; i < n; i++) {
      if (p[i] < q[pt]) {
        pt = upperBound(q, p[i] + time) + 1;
      } else {
        int l = Math.abs(p[i] - q[pt]);
        if (l > time) {
          return false;
        }
        pt = lowerBound(q, p[i]);

        while (pt < m && Math.abs(q[pt] - p[i]) + l * 2 <= time) {
          pt++;
        }

        while (pt < m && Math.abs(q[pt] - p[i]) * 2 + l <= time) {
          pt++;
        }

      }

      if (pt == m)
        return true;
    }
    return pt == m;
  }

  public static int upperBound(int[] a, int v) {
    int low = -1, high = a.length;
    while (high - low > 1) {
      int h = high + low >>> 1;
      if (a[h] <= v) {
        low = h;
      } else {
        high = h;
      }
    }
    return low;
  }

  public static int lowerBound(int[] a, int v) {
    int low = -1, high = a.length;
    while (high - low > 1) {
      int h = high + low >>> 1;
      if (a[h] >= v) {
        high = h;
      } else {
        low = h;
      }
    }
    return high;
  }

  private static void inputArray(int[] array, int k) {
    int n = array.length;
    for (int i = 0; i < k; i++) {
      array[i] = ni();
    }
    long a = nl();
    long b = nl();
    long c = nl();
    long d = nl();
    for (int i = k; i < n; i++) {
      array[i] = (int) ((a * array[i - 2] + b * array[i - 1] + c) % d + 1);
    }
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
