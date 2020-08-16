
// import java.io.BufferedReader;
// import java.io.BufferedWriter;
// import java.io.IOException;
// import java.lang.reflect.Array;
// import java.nio.charset.StandardCharsets;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Collections;
// import java.util.List;
// import java.util.StringTokenizer;
// import java.util.TreeSet;

// class DisjointSet {
// public int[] upper; // minus:num_element(root) plus:root(normal)
// // public int[] w;

// public DisjointSet(int n) {
// upper = new int[n];
// Arrays.fill(upper, -1);
// // w = new int[n];
// }

// public DisjointSet(DisjointSet ds) {
// this.upper = Arrays.copyOf(ds.upper, ds.upper.length);
// }

// public int root(int x) {
// return upper[x] < 0 ? x : (upper[x] = root(upper[x]));
// }

// public boolean equiv(int x, int y) {
// return root(x) == root(y);
// }

// public boolean union(int x, int y) {
// x = root(x);
// y = root(y);
// if (x != y) {
// if (upper[y] < upper[x]) {
// int d = x;
// x = y;
// y = d;
// }
// // w[x] += w[y];
// upper[x] += upper[y];
// upper[y] = x;
// }
// return x == y;
// }

// public int count() {
// int ct = 0;
// for (int u : upper) {
// if (u < 0)
// ct++;
// }
// return ct;
// }

// public int[][] toBucket() {
// int n = upper.length;
// int[][] ret = new int[n][];
// int[] rp = new int[n];
// for (int i = 0; i < n; i++) {
// if (upper[i] < 0)
// ret[i] = new int[-upper[i]];
// }
// for (int i = 0; i < n; i++) {
// int r = root(i);
// ret[r][rp[r]++] = i;
// }
// return ret;
// }
// }

// public class Main {
// private static final String INPUT_ID =
// "/home/ubuntu/workspaces/contests/fhc2020_r1_C/quarantine";
// // 0:Sample 1:Validation 2:Submit
// private static final int STATE = 0;

// public static String solve() {
// int n = ni();
// int k = ni();
// char[] s = ns();
// int[] e = new int[n - 1];
// inputArray(e, k);

// int[] par = new int[n];
// par[0] = -1;
// for (int i = 1; i < n; i++) {
// par[i] = e[i - 1] - 1;
// }
// int[][] g = parentToChildren(par);
// int[] leaf = new int[n];
// DisjointSet ds = new DisjointSet(n);

// dfs(0, g, leaf, s, ds);
// int[][] cluster = new int[n][2];
// for (int i = 0; i < n; i++) {
// int v = ds.root(i);
// if (s[i] == '*') {
// cluster[v][0]++;
// cluster[v][1] += leaf[i];
// } else {
// cluster[v] = null;
// }
// }

// List<int[]> list = new ArrayList<>();
// for (int i = 0; i < n; i++) {
// if (cluster[i] == null || i != ds.root(i))
// continue;
// list.add(cluster[i]);
// }

// long count;
// long max;
// if (list.size() == 1) {
// int node = list.get(0)[0];
// int edge = list.get(0)[1];
// long[] r = new long[1];
// max = node;
// for (int i = 0; i < n; i++) {
// if (s[i] == '*') {
// dfs2(i, g, r, true, s);
// break;
// }
// }
// count = r[0] + (long) edge * (n - 1);
// } else if (list.size() == 0) {
// long[] r = new long[1];
// dfs2(0, g, r, false, s);
// max = 0;
// count = r[0];

// } else {
// Collections.sort(list, (o1, o2) -> o2[0] - o1[0]);
// List<int[]> top = new ArrayList<>();

// int topNum = list.get(0)[0];
// int secNum = list.get(1)[0];

// long topNode = 0;
// long topEdge = 0;

// long topNodeEdge = 0;
// for (int[] v : list) {
// if (v[0] == topNum) {
// topNode += v[0];
// topEdge += v[1];
// topNodeEdge += (long) v[0] * v[1];
// top.add(v);
// }
// }
// if (topNum > secNum) {
// count = 0;
// for (int[] v : list) {
// if (v[0] == secNum) {
// count += topNode * v[0];
// }
// }
// max = topNum + secNum;
// } else {
// max = topNum * 2;
// count = 0;

// for (int[] v : list) {
// if (v[0] == topNum) {
// long node = v[0];
// long edge = v[1];
// long nodeEdge = node * edge;
// count += nodeEdge * (topNode - node) + node * (topNodeEdge - nodeEdge);
// }
// }
// count /= 2;
// }
// }
// max = max * (max - 1) / 2;

// return max + " " + count;
// }

// private static long dfs2(int cur, int[][] g, long[] ret, boolean flg, char[]
// s) {
// int n = g.length;
// long r = 1;
// for (int nex : g[cur]) {
// if (flg && s[nex] == '#')
// continue;

// long v = dfs2(nex, g, ret, flg, s);
// ret[0] += v * (n - v);
// r += v;
// }
// return r;
// }

// private static void dfs(int cur, int[][] g, int[] leaf, char[] s, DisjointSet
// ds) {
// for (int nex : g[cur]) {
// if (s[cur] == '*') {
// if (s[cur] == s[nex]) {
// ds.union(nex, cur);
// } else {
// leaf[cur]++;
// }
// } else {
// if (s[nex] == '*') {
// leaf[nex]++;
// }
// }
// dfs(nex, g, leaf, s, ds);
// }
// }

// public static int[][] parentToChildren(int[] par) {
// int n = par.length;
// int[] ct = new int[n];
// for (int i = 0; i < n; i++) {
// if (par[i] >= 0) {
// ct[par[i]]++;
// }
// }
// int[][] g = new int[n][];
// for (int i = 0; i < n; i++) {
// g[i] = new int[ct[i]];
// }
// for (int i = 0; i < n; i++) {
// if (par[i] >= 0) {
// g[par[i]][--ct[par[i]]] = i;
// }
// }

// return g;
// }

// private static void inputArray(int[] array, int k) {
// int n = array.length;
// for (int i = 0; i < k; i++) {
// array[i] = ni();
// }
// long a = nl();
// long b = nl();
// long c = nl();
// for (int i = k; i < n; i++) {
// array[i] = (int) ((a * array[i - 2] + b * array[i - 1] + c) % i + 1);
// }
// }

// public static String batch() {
// int t = ni();
// StringBuilder ret = new StringBuilder();
// for (int i = 0; i < t; i++) {
// String v = solve();
// String line = "Case #" + (i + 1) + ": " + v;
// System.out.println(line);
// ret.append(line);
// ret.append("\n");
// }
// return ret.toString();
// }

// public static void main(String[] args) throws IOException {
// String id = INPUT_ID;
// String[] suffix = { "_sample_input.txt", "_validation_input.txt",
// "_input.txt" };
// Path input = Paths.get(id + suffix[STATE]);
// new Thread(null, new Runnable() {
// @Override
// public void run() {
// try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("output.txt"),
// StandardCharsets.UTF_8)) {
// reader = Files.newBufferedReader(input, StandardCharsets.UTF_8);
// String ret = batch();
// bw.write(ret);

// if (STATE == 0) {
// StringBuilder sb = new StringBuilder();
// try (BufferedReader br = Files.newBufferedReader(Paths.get(id +
// "_sample_output.txt"),
// StandardCharsets.UTF_8)) {
// String line;
// while ((line = br.readLine()) != null) {
// sb.append(line);
// sb.append("\n");
// }
// }
// System.out.println();
// System.out.println("Sample output:");
// System.out.println(sb);
// System.out.println(sb.toString().equals(ret) ? "OK!" : "NG!");
// }
// } catch (IOException e) {
// throw new RuntimeException(e);
// }
// }
// }, "", 64000000).start();
// }

// private static StringTokenizer tokenizer = null;
// private static BufferedReader reader;

// public static String next() {
// while (tokenizer == null || !tokenizer.hasMoreTokens()) {
// try {
// tokenizer = new java.util.StringTokenizer(reader.readLine());
// } catch (Exception e) {
// throw new RuntimeException(e);
// }
// }
// return tokenizer.nextToken();
// }

// private static double nd() {
// return Double.parseDouble(next());
// }

// private static long nl() {
// return Long.parseLong(next());
// }

// private static int[] na(int n) {
// int[] a = new int[n];
// for (int i = 0; i < n; i++)
// a[i] = ni();
// return a;
// }

// private static char[] ns() {
// return next().toCharArray();
// }

// private static long[] nal(int n) {
// long[] a = new long[n];
// for (int i = 0; i < n; i++)
// a[i] = nl();
// return a;
// }

// private static int[][] ntable(int n, int m) {
// int[][] table = new int[n][m];
// for (int i = 0; i < n; i++) {
// for (int j = 0; j < m; j++) {
// table[i][j] = ni();
// }
// }
// return table;
// }

// private static int[][] nlist(int n, int m) {
// int[][] table = new int[m][n];
// for (int i = 0; i < n; i++) {
// for (int j = 0; j < m; j++) {
// table[j][i] = ni();
// }
// }
// return table;
// }

// private static int ni() {
// return Integer.parseInt(next());
// }
// }
