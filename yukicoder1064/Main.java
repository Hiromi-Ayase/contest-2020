import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {

    private static java.io.InputStream is = System.in;
    private static java.io.PrintWriter out = new java.io.PrintWriter(System.out);
    private static java.util.StringTokenizer tokenizer = null;
    private static java.io.BufferedReader reader;
    static {
        reader = new java.io.BufferedReader(new java.io.InputStreamReader(is), 32768);
    }

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

    private static int ni() {
        return Integer.parseInt(next());
    }

    public static void main(String[] args) {
        int n = ni();
        int m = ni();
        int x = ni() - 1;
        int y = ni() - 1;

        int[][] p = new int[n][];
        for (int i = 0; i < n; i++) {
            p[i] = new int[] { ni(), ni() };
        }

        int[] from = new int[m];
        int[] to = new int[m];
        int[] w = new int[m];
        for (int i = 0; i < m; i++) {
            from[i] = ni() - 1;
            to[i] = ni() - 1;

            int dx = p[from[i]][0] - p[to[i]][0];
            int dy = p[from[i]][1] - p[to[i]][1];
            w[i] = dx * dx + dy * dy;
        }

        int[][][] g = packWU(n, from, to, w);
        double[] d = dijk(g, x);
        System.out.println(d[y]);
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

    public static double[] dijk(int[][][] g, int from) {
        int n = g.length;
        final double[] td = new double[n];
        Arrays.fill(td, Integer.MAX_VALUE);
        TreeSet<Integer> q = new TreeSet<Integer>(new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                if (td[a] - td[b] != 0)
                    return Double.compare(td[a], td[b]);
                return a - b;
            }
        });
        q.add(from);
        td[from] = 0;

        while (q.size() > 0) {
            int cur = q.pollFirst();

            for (int i = 0; i < g[cur].length; i++) {
                int next = g[cur][i][0];
                double nd = td[cur] + Math.sqrt(g[cur][i][1]);
                if (nd < td[next]) {
                    q.remove(next);
                    td[next] = nd;
                    q.add(next);
                }
            }
        }

        return td;
    }
}
