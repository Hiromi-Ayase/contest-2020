import java.util.Arrays;

public class Main {

    private static void solve() {
        int n = ni();

    }

    private static long dfs(int i, boolean[] even, int ecnt, int[] a, int[] b) {
        int n = even.length;
        if (n == i) {
            int[] arr = new int[n];
            boolean[] used = new boolean[n];
            for (int k = 0; k < n; k += 2) {
                for (int j = 0; j < n; j++) {
                    if (!used[j] && even[j] && k == 0 || arr[k - 2] >= a[j]) {
                        arr[k] = a[j];
                        used[j] = true;
                        break;
                    }
                }
            }

            for (int k = 0; k < n; k += 2) {
                for (int j = 0; j < n; j++) {
                    if (!used[j] && even[j] && k == 0 || arr[k - 2] >= a[j]) {
                        arr[k] = a[j];
                        used[j] = true;
                        break;
                    }
                }
            }
        }

        long ret = 0;
        if (ecnt < (n + 1) / 2) {
            even[i] = true;
            ret += dfs(i + 1, even, ecnt + 1, a, b);
        }

        if (n - ecnt < n / 2) {
            even[i] = false;
            ret += dfs(i + 1, even, ecnt, a, b);
        }
        return ret;
    }

    public static long bubbleCount(int[] a, int l, int r) {
        if (r - l <= 1)
            return 0;
        int m = (l + r) / 2;
        long ret = bubbleCount(a, l, m) + bubbleCount(a, m, r);
        int[] temp = Arrays.copyOfRange(a, l, r);
        for (int p0 = 0, p1 = m - l, p = l; p < r;) {
            if (p0 == m - l) {
                a[p++] = temp[p1++];
            } else if (p1 == r - l) {
                a[p++] = temp[p0++];
            } else if (temp[p0] <= temp[p1]) {
                a[p++] = temp[p0++];
            } else {
                a[p++] = temp[p1++];
                ret += m - l - p0;
            }
        }
        return ret;
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
