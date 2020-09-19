import java.util.*;

class MaxFlow4 {
	public static class Edge {
		public int from, to;
		public int capacity;
		public int flow;
		public Edge complement;
		public boolean cloned;

		public Edge(int from, int to, int capacity) {
			this.from = from;
			this.to = to;
			this.capacity = capacity;
		}
	}

	public static void reset(Edge[][] g) {
		for (Edge[] row : g) {
			for (Edge e : row) {
				e.flow = e.cloned ? e.capacity : 0;
			}
		}
	}

	public static Edge[][] compileWD(int n, List<Edge> edges) {
		Edge[][] g = new Edge[n][];
		// cloning
		for (int i = edges.size() - 1; i >= 0; i--) {
			Edge origin = edges.get(i);
			Edge clone = new Edge(origin.to, origin.from, origin.capacity);
			clone.flow = origin.capacity;
			clone.complement = origin;
			clone.cloned = true;
			origin.complement = clone;
			edges.add(clone);
		}

		int[] p = new int[n];
		for (Edge e : edges)
			p[e.from]++;
		for (int i = 0; i < n; i++)
			g[i] = new Edge[p[i]];
		for (Edge e : edges)
			g[e.from][--p[e.from]] = e;
		return g;
	}

	public static Edge[][] compileWU(int n, List<Edge> edges) {
		Edge[][] g = new Edge[n][];
		// cloning
		for (int i = edges.size() - 1; i >= 0; i--) {
			Edge origin = edges.get(i);
			Edge clone = new Edge(origin.to, origin.from, origin.capacity * 2);
			origin.flow = origin.capacity;
			clone.flow = origin.capacity;
			clone.complement = origin;
			clone.cloned = true;
			origin.complement = clone;
			origin.capacity *= 2;
			edges.add(clone);
		}

		int[] p = new int[n];
		for (Edge e : edges)
			p[e.from]++;
		for (int i = 0; i < n; i++)
			g[i] = new Edge[p[i]];
		for (Edge e : edges)
			g[e.from][--p[e.from]] = e;
		return g;
	}

	public static long maximumFlowDinic(Edge[][] g, int source, int sink) {
		int n = g.length;
		int[] d = new int[n];
		int[] q = new int[n];
		long ret = 0;
		while (true) {
			Arrays.fill(d, -1);
			q[0] = source;
			int r = 1;
			d[source] = 0;

			for (int v = 0; v < r; v++) {
				int cur = q[v];
				for (Edge ne : g[cur]) {
					if (d[ne.to] == -1 && ne.capacity - ne.flow > 0) {
						q[r++] = ne.to;
						d[ne.to] = d[cur] + 1;
					}
				}
			}
			if (d[sink] == -1)
				break;
			int[] sp = new int[n];
			for (int i = 0; i < n; i++)
				sp[i] = g[i].length - 1;
			ret += dfsDinic(d, g, sp, source, sink, Long.MAX_VALUE);
		}

		return ret;
	}

	private static long dfsDinic(int[] d, Edge[][] g, int[] sp, int cur, int sink, long min) {
		if (cur == sink)
			return min;
		long left = min;
		for (int i = sp[cur]; i >= 0; i--) {
			Edge ne = g[cur][i];
			if (d[ne.to] == d[cur] + 1 && ne.capacity - ne.flow > 0) {
				long fl = dfsDinic(d, g, sp, ne.to, sink, Math.min(left, ne.capacity - ne.flow));
				if (fl > 0) {
					left -= fl;
					ne.flow += fl;
					ne.complement.flow -= fl;
					if (left == 0) {
						sp[cur] = i;
						return min;
					}
				}
			}
		}
		sp[cur] = -1;
		return min - left;
	}
}

@SuppressWarnings("unused")
public class Main {

	private static void solve() {
		int h = ni();
		int w = ni();
		char[][] map = new char[h][w];
		for (int i = 0; i < h; i++) {
			map[i] = ns();
		}

		List<MaxFlow4.Edge> edges = new ArrayList<>();
		int src = h * w;
		int sink = h * w + 1;
		int n = h * w + 2;

		int[][] dir = { { 0, 1 }, { 1, 0 }, { -1, 0 }, { 0, -1 } };
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				int v = i * w + j;
				if (map[i][j] == '#')
					continue;
				if ((i + j) % 2 == 0) {
					edges.add(new MaxFlow4.Edge(src, v, 1));
					for (int[] d : dir) {
						int ni = i + d[0];
						int nj = j + d[1];
						if (0 <= ni && ni < h && 0 <= nj && nj < w) {
							if (map[ni][nj] == '#')
								continue;
							edges.add(new MaxFlow4.Edge(v, ni * w + nj, 1));
						}
					}
				} else {
					edges.add(new MaxFlow4.Edge(v, sink, 1));
				}
			}
		}
		MaxFlow4.Edge[][] g = MaxFlow4.compileWD(n, edges);
		long ret = MaxFlow4.maximumFlowDinic(g, src, sink);

		for (MaxFlow4.Edge e : edges) {
			if (e.cloned != false &&  e.flow == 0 && e.from != src && e.from != sink && e.to != src && e.to != sink) {
				int a = Math.min(e.from, e.to);
				int b = Math.max(e.from, e.to);
				if (w >= 2 && b - a == 1) {
					map[a/w][a%w] = '>';
					map[b/w][b%w] = '<';
				} else {
					map[a/w][a%w] = 'v';
					map[b/w][b%w] = '^';
				}
			}
		}
		System.out.println(ret);
		for (char[] line : map) {
			System.out.println(line);
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
