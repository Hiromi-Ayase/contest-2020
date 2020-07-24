import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Main {

  private static void dfs2(int now, int[][] trie, int[][] cnt, int[][] cnt2, Map<Integer, List<Query>> map, int n) {
    int str = hit[now];
    if (map.containsKey(str)) {
      for (Query query : map.get(str)) {
        int ret = 0;
        char[] p = query.p;
        for (int i = 0; i < 26; i++) {
          for (int j = i + 1; j < 26; j++) {
            ret += cnt2[p[i] - 'a'][p[j] - 'a'];
          }
        }
        for (int i = 0; i < 26; i++) {
          ret += cnt[now][i];
        }
        query.ret = n - ret;
      }
    }

    for (int i = 0; i < 26; i++) {
      int nex = trie[now][i];
      if (nex < 0)
        continue;

      for (int j = 0; j < 26; j++) {
        cnt2[i][j] += cnt[now][j];
      }
      dfs2(trie[now][i], trie, cnt, cnt2, map, n);
      for (int j = 0; j < 26; j++) {
        cnt2[i][j] -= cnt[now][j];
      }
    }
  }

  private static int dfs(int now, int[][] trie, int[][] cnt) {
    int total = hit[now] >= 0 ? 1 : 0;

    for (int i = 0; i < 26; i++) {
      if (trie[now][i] < 0)
        continue;

      cnt[now][i] = dfs(trie[now][i], trie, cnt);
      total += cnt[now][i];
    }
    return total;
  }

  private static class Query {
    char[] p;
    int index;
    int ret;
  }

  private static int[] hit;

  public static int[][] buildAlphabetTrie(char[][] q) {
    int m = 1;
    for (int i = 0; i < q.length; i++)
      m += q[i].length;
    int[][] trie = new int[m][26];
    hit = new int[m];
    Arrays.fill(hit, -1);
    for (int i = 0; i < m; i++)
      Arrays.fill(trie[i], -1);

    int tp = 1;
    for (int j = 0; j < q.length; j++) {
      int cur = 0;
      for (int i = 0; i < q[j].length; i++) {
        int ind = q[j][i] - 'a';
        if (trie[cur][ind] == -1)
          trie[cur][ind] = tp++;
        cur = trie[cur][ind];
      }
      hit[cur] = j;
    }
    return Arrays.copyOf(trie, tp);
  }

  private static void solve() {
    int n = ni();
    char[][] s = new char[n][];
    for (int i = 0; i < n; i++) {
      s[i] = ns();
    }
    int[][] trie = buildAlphabetTrie(s);

    int q = ni();
    Map<Integer, List<Query>> map = new HashMap<>();

    for (int i = 0; i < q; i++) {

      int k = ni() - 1;
      char[] p = ns();

      Query query = new Query();
      query.p = p;
      query.index = i;

      map.putIfAbsent(k, new ArrayList<>());
      map.get(k).add(query);
    }

    int[][] cnt = new int[trie.length][26];
    dfs(0, trie, cnt);
    dfs2(0, trie, cnt, new int[26][26], map, n);

    int[] ret = new int[q];
    for (List<Query> queries : map.values()) {
      for (Query query : queries)
        ret[query.index] = query.ret;
    }

    for (int v : ret) {
      out.println(v);
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
