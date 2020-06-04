import java.util.Arrays;
import java.util.TreeSet;

public class Main {

    private static void solve() {
        int n = ni();
        int m = ni();
        int[] d = na(n);
        int[] from = new int[m];
        int[] to = new int[m];
        int[] w = new int[m];
        for (int i = 0; i < m; i++) {
            from[i] = ni() - 1;
            to[i] = ni() - 1;
            w[i] = i;
        }
        int[][][] g = packWU(n, from, to, w);
        int[] ret = new int[m];
        char[] eo = new char[n];
        Arrays.fill(ret, -1);

        TreeSet<Integer> q = new TreeSet<>((o1, o2) -> d[o1] == d[o2] ? o1 - o2 : d[o1] - d[o2]);
        for (int i = 0; i < n; i++) {
            q.add(i);
        }

        while (q.size() > 0) {
            int u = q.first();
            eo[u] = 'W';
            if (!dfs(u, g, ret, eo, true, q, d)) {
                System.out.println(-1);
                return;
            }
        }

        out.println(eo);
        for (int v : ret) {
            out.println(v < 0 ? (int) 1e9 : v);
        }
    }

    private static boolean dfs(int cur, int[][][] g, int[] ret, char[] eo, boolean root, TreeSet<Integer> q, int[] d) {
        q.remove(cur);

        int min = Integer.MAX_VALUE;
        for (int[] v : g[cur]) {
            int nex = v[0];
            int w = v[1];
            if (!q.contains(nex))
                continue;

            if (d[cur] <= d[nex]) {
                ret[w] = d[nex];
                eo[nex] = eo[cur] == 'W' ? 'B' : 'W';
                min = Math.min(min, d[nex]);

                if (!dfs(nex, g, ret, eo, false, q, d)) {
                    return false;
                }
            }
        }
        return root && min == d[cur] || !root;
    }

    public static int[][][] packWU(int n, int[] from, int[] to, int[] w) {
        return packWU(n, from, to, w, from.length);
    }

    public static int[][][] packWU(int n, int[] from, int[] to, int[] w, int sup) {
        int[][][] g = new int[n][][];
        int[] p = new int[n];
        for (int i = 0; i < sup; i++)
            p[from[i]]++;
        for (int i = 0; i < sup; i++)
            p[to[i]]++;
        for (int i = 0; i < n; i++)
            g[i] = new int[p[i]][2];
        for (int i = 0; i < sup; i++) {
            --p[from[i]];
            g[from[i]][p[from[i]]][0] = to[i];
            g[from[i]][p[from[i]]][1] = w[i];
            --p[to[i]];
            g[to[i]][p[to[i]]][0] = from[i];
            g[to[i]][p[to[i]]][1] = w[i];
        }
        return g;
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
