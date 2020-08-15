
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

public class Main {
  private static final String INPUT_ID = "running_on_fumes_chapter_2";
  private static final int STATE = 0;

  public static String solve() {
    int n = ni();
    int m = ni();
    int a = ni() - 1;
    int b = ni() - 1;
    int[] par = new int[n];
    long[] c = new long[n];
    for (int i = 0; i < n; i++) {
      par[i] = ni() - 1;
      c[i] = nl();
    }
    int[][] g = parentToChildren(par);
    return "";
  }

  private static void dfs(int cur, int depth, long[] cost, int[][] g, long[] c) {
    for (int nex : g[cur]) {
      dfs(nex, depth + 1, cost, g, c);
    }
  }

  public static int[][] parentToChildren(int[] par) {
    int n = par.length;
    int[] ct = new int[n];
    for (int i = 0; i < n; i++) {
      if (par[i] >= 0) {
        ct[par[i]]++;
      }
    }
    int[][] g = new int[n][];
    for (int i = 0; i < n; i++) {
      g[i] = new int[ct[i]];
    }
    for (int i = 0; i < n; i++) {
      if (par[i] >= 0) {
        g[par[i]][--ct[par[i]]] = i;
      }
    }

    return g;
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
