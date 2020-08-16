
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class Main {
  private static final String INPUT_ID = "/home/ubuntu/workspaces/contests/fhc2020_r1_A1/perimetric_chapter_1";
  // 0:Sample 1:Validation 2:Submit
  private static final int STATE = 2;

  public static String solve() {
    int n = ni();
    int k = ni();
    int w = ni();
    int[] l = new int[n];
    int[] h = new int[n];
    inputArray(l, k);
    inputArray(h, k);
    int mod = (int) 1e9 + 7;

    long ret = 1;
    long sum = 0;

    int startX = -1;
    long[] a = new long[n * w + 1];
    long[] b = new long[n * w + 1];

    for (int i = 0; i < n; i++) {
      if (i > 0 && l[i - 1] + w < l[i]) {
        int r = l[i - 1] + w - startX;
        long p = a[r] + b[r] + r * 2L;
        sum += p;
        sum %= mod;
        for (int j = r; j >= 0; j--) {
          a[j] = b[j] = 0;
        }
        startX = -1;
      }

      if (startX < 0) {
        startX = l[i];
      }

      for (int j = 0; j < w; j++) {
        int idx = l[i] - startX + j + 1;
        a[idx] = Math.max(a[idx], h[i]);
        b[idx] = b[idx - 1] + Math.abs(a[idx] - a[idx - 1]);
      }

      int r = l[i] + w - startX;
      long p = a[r] + b[r] + r * 2L + sum;
      p %= mod;
      ret = ret * p % mod;
    }

    return "" + ret;
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
