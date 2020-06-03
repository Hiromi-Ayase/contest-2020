import java.util.Arrays;

public class Main {

    private static void solve() {
        int n = ni();
        int[] a = na(n);

        int[][] b = new int[n][2];
        for (int i = 0; i < n; i++) {
            b[i][0] = i;
            b[i][1] = a[i];
        }

        Arrays.sort(b, (o1, o2) -> o2[1] - o1[1]);

        long[][] dp = new long[n + 1][n + 1];
        for (int i = 0; i < n; i++) {
            for (int l = 0; l <= i; l++) {
                int r = n - (i - l) - 1;
                // left:j個 right:i-j個

                long lp = (long) b[i][1] * Math.abs(b[i][0] - l);
                dp[i + 1][l + 1] = Math.max(dp[i + 1][l + 1], dp[i][l] + lp);
                long rp = (long) b[i][1] * Math.abs(b[i][0] - r);
                dp[i + 1][l] = Math.max(dp[i + 1][l], dp[i][l] + rp);
            }
        }

        long ret = 0;
        for (int i = 0; i <= n; i++) {
            ret = Math.max(ret, dp[n][i]);
        }
        System.out.println(ret);
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
