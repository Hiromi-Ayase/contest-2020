import java.util.Arrays;

public class Main {

    private static void solve() {
        long l = nl();
        long r = nl();

        long x = f(r, r);
        long y = f(l - 1, r);

        int mod = (int) 1e9 + 7;
        System.out.println((x - y + mod) % mod);
    }

    private static long f(long a, long b) {
        int mod = (int) 1e9 + 7;
        // dp[n桁目][started][xがa上限][yがb上限]
        long[][][] dp = new long[65][2][2];

        int as = 1;
        int bs = 1;

        for (int i = 63; i >= 0; i--) {
            int abit = (int) ((a >> i) & 1);
            int bbit = (int) ((b >> i) & 1);

            if ((abit == 1 || as == 0) && (bbit == 1 || bs == 0)) {
                dp[i][as][bs]++;
            }

            // a && b
            if (abit == 1 && bbit == 1) {
                dp[i][1][1] += dp[i + 1][1][1]; // (1, 1)
                dp[i][0][0] += dp[i + 1][1][1]; // (0, 0)
                dp[i][0][1] += dp[i + 1][1][1]; // (0, 1)
            } else if (abit == 0 && bbit == 1) {
                dp[i][1][1] += dp[i + 1][1][1]; // (0, 1)
                dp[i][1][0] += dp[i + 1][1][1]; // (0, 0)
            } else if (abit == 0 && bbit == 0) {
                dp[i][1][1] += dp[i + 1][1][1]; // (0, 0)
            } else {
                // abit == 1 && bbit == 0
                dp[i][0][1] += dp[i + 1][1][1]; // (0, 0)
            }

            // !a && !b
            dp[i][0][0] += dp[i + 1][0][0] * 3; // (0, 0) (0, 1), (1, 1);

            // b only
            if (bbit == 1) {
                dp[i][0][1] += dp[i + 1][0][1] * 2; // (1, 1), (0, 1)
                dp[i][0][0] += dp[i + 1][0][1]; // (0, 0);
            } else {
                dp[i][0][1] += dp[i + 1][0][1]; // (0, 0)
            }

            // a only
            if (abit == 1) {
                dp[i][1][0] += dp[i + 1][1][0]; // (1, 1)
                dp[i][0][0] += dp[i + 1][1][0] * 2; // (0, 1), (0, 0);
            } else {
                dp[i][1][0] += dp[i + 1][1][0] * 2; // (0, 0), (0, 1);
            }

            if (abit == 1) {
                as = 0;
            }
            if (bbit == 1) {
                bs = 0;
            }
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    dp[i][j][k] %= mod;
                }
            }
        }
        long ret = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                ret += dp[0][i][j];
                ret %= mod;
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
