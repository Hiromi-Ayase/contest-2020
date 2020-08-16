
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// f 20m
public class C implements Runnable {
	static final boolean PROD = true;
	static final int NTHREAD = 1;
	static String BASEPATH = "";

	static String INPATH = BASEPATH + "quarantine_validation_input.txt";
	static String OUTPATH = INPATH.substring(0, INPATH.length() - 4) + new SimpleDateFormat("-HHmmss").format(new Date())
			+ ".out";

	static String INPUT = "";

	int n;
	int[][] g;
	char[] s;

	public void read() // not parallelized
	{
		n = ni();
		int K = ni();
		s = in.next().toCharArray();
		int[] E = make(na(K), ni(), ni(), ni(), n - 1);
		int[] par = new int[n];
		par[0] = -1;
		for (int i = 0; i < n - 1; i++) {
			par[i + 1] = E[i] - 1;
		}
		g = parentToG(par);
	}

	public static int[][] parentToG(int[] par) {
		int n = par.length;
		int[] ct = new int[n];
		for (int i = 0; i < n; i++) {
			if (par[i] >= 0) {
				ct[i]++;
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
				g[i][--ct[i]] = par[i];
			}
		}
		return g;
	}

	int[] make(int[] a, long A, long B, long C, int n) {
		int[] ret = new int[n];
		for (int i = 0; i < a.length; i++)
			ret[i] = a[i];
		for (int i = a.length; i < n; i++) {
			ret[i] = (int) ((A * ret[i - 2] + B * ret[i - 1] + C) % (i + 1) + 1);
		}
		return ret;
	}

	public void process() // parallelized!
	{
		int Y = 0;
		for (char c : s) {
			if (c == '*')
				Y++;
		}
		int[][] pars = parents3(g, 0);
		int[] par = pars[0], ord = pars[1], dep = pars[2];

		int[] des = new int[n];
		Arrays.fill(des, 1);
		for (int i = n - 1; i >= 1; i--) {
			des[par[ord[i]]] += des[ord[i]];
		}

		if (Y == 0) {
			out.print(0 + " ");
			long ans = 0;
			for (int i = 1; i < n; i++) {
				ans += (long) des[i] * (n - des[i]);
			}
			out.println(ans);
			return;
		}

		DJSet ds = new DJSet(n);
		for (int i = 1; i < n; i++) {
			if (s[i] == '*' && s[par[i]] == '*') {
				ds.union(i, par[i]);
			}
		}
		int[] ss = new int[n];
		int p = 0;
		for (int i = 0; i < n; i++) {
			if (ds.upper[i] < 0 && s[i] == '*') {
				ss[p++] = -ds.upper[i];
			}
		}
		ss = Arrays.copyOf(ss, p);
		Arrays.parallelSort(ss);
		;
		long base = 0;
		for (int x : ss) {
			base += (long) x * (x - 1) / 2;
		}

		if (p == 1) {
			int[] yet = new int[n];
			for (int i = 0; i < n; i++) {
				if (s[i] == '*')
					yet[i] = 1;
			}
			for (int i = n - 1; i >= 1; i--) {
				yet[par[ord[i]]] += yet[ord[i]];
			}

			out.print(base + " ");

			long ans = 0;
			for (int i = 1; i < n; i++) {
				if (s[i] == '*' && s[par[i]] == '*') {
					ans += (long) yet[i] * (Y - yet[i]);
				} else {
					ans += (long) des[i] * (n - des[i]);
				}
			}
			out.println(ans);
			return;
		}

		if (ss[p - 1] == ss[p - 2]) {
			int[] inm = new int[n];
			for (int i = 0; i < n; i++) {
				if (s[i] == '*' && -ds.upper[ds.root(i)] == ss[p - 1]) {
					inm[i]++;
				}
			}
			for (int i = n - 1; i >= 1; i--) {
				inm[par[ord[i]]] += inm[ord[i]];
			}
			out.print(
					base - (long) ss[p - 1] * (ss[p - 1] - 1) / 2 * 2 + (long) (2 * ss[p - 1]) * (2 * ss[p - 1] - 1) / 2 + " ");

			long ans = 0;
			for (int i = 1; i < n; i++) {
				if (inm[i] > 0 && inm[i] < inm[0] && inm[i] % ss[p - 1] == 0 && !(s[i] == '*' && s[par[i]] == '*')) {
					ans += (long) inm[i] * (inm[0] - inm[i]);
				}
			}
			out.println(ans);
			return;
		}

		int root = -1;
		for (int i = 0; i < n; i++) {
			if (ss[p - 1] == -ds.upper[i]) {
				root = i;
			}
		}
		pars = parents3(g, root);
		par = pars[0];
		ord = pars[1];
		dep = pars[2];

		int[] inn = new int[n];
		for (int i = 0; i < n; i++) {
			if (s[i] == '*' && -ds.upper[ds.root(i)] == ss[p - 2]) {
				inn[i]++;
			}
		}
		for (int i = n - 1; i >= 1; i--) {
			inn[par[ord[i]]] += inn[ord[i]];
		}
		out.print(base - (long) ss[p - 1] * (ss[p - 1] - 1) / 2 - (long) ss[p - 2] * (ss[p - 2] - 1) / 2
				+ (long) (ss[p - 1] + ss[p - 2]) * (ss[p - 1] + ss[p - 2] - 1) / 2 + " ");

		long ans = 0;
		for (int i = 0; i < n; i++) {
			if (i == root)
				continue;
			if (inn[i] > 0 && inn[i] % ss[p - 2] == 0 && !ds.equiv(i, root) && !(s[i] == '*' && s[par[i]] == '*')) {
				ans += (long) inn[i] * ss[p - 1];
			}
		}
		out.println(ans);
	}

	public static int[][] parents3(int[][] g, int root) {
		int n = g.length;
		int[] par = new int[n];
		Arrays.fill(par, -1);

		int[] depth = new int[n];
		depth[0] = 0;

		int[] q = new int[n];
		q[0] = root;
		for (int p = 0, r = 1; p < r; p++) {
			int cur = q[p];
			for (int nex : g[cur]) {
				if (par[cur] != nex) {
					q[r++] = nex;
					par[nex] = cur;
					depth[nex] = depth[cur] + 1;
				}
			}
		}
		return new int[][] { par, q, depth };
	}

	public static class DJSet {
		public int[] upper;

		public DJSet(int n) {
			upper = new int[n];
			Arrays.fill(upper, -1);
		}

		public int root(int x) {
			return upper[x] < 0 ? x : (upper[x] = root(upper[x]));
		}

		public boolean equiv(int x, int y) {
			return root(x) == root(y);
		}

		public boolean union(int x, int y) {
			x = root(x);
			y = root(y);
			if (x != y) {
				if (upper[y] < upper[x]) {
					int d = x;
					x = y;
					y = d;
				}
				upper[x] += upper[y];
				upper[y] = x;
			}
			return x == y;
		}

		public int count() {
			int ct = 0;
			for (int u : upper)
				if (u < 0)
					ct++;
			return ct;
		}
	}

	public static void preprocess() {
	}

	Scanner in;
	PrintWriter out;
	StringWriter sw;
	int cas;
	static List<Status> running = new ArrayList<Status>();

	@Override
	public void run() {
		long S = System.nanoTime();
		// register
		synchronized (running) {
			Status st = new Status();
			st.id = cas;
			st.S = S;
			running.add(st);
		}
		process();
		// deregister
		synchronized (running) {
			for (Status st : running) {
				if (st.id == cas) {
					running.remove(st);
					break;
				}
			}
		}
		long G = System.nanoTime();

		if (PROD) {
			System.err.println("case " + cas + " solved. [" + (G - S) / 1000000 + "ms]");
			synchronized (running) {
				StringBuilder sb = new StringBuilder("running : ");
				for (Status st : running) {
					sb.append(st.id + ":" + (G - st.S) / 1000000 + "ms, ");
				}
				System.err.println(sb);
			}
		}
	}

	private static class Status {
		public int id;
		public long S;
	}

	public C(int cas, Scanner in) {
		this.cas = cas;
		this.in = in;
		this.sw = new StringWriter();
		this.out = new PrintWriter(this.sw);
	}

	private int ni() {
		return Integer.parseInt(in.next());
	}

	private long nl() {
		return Long.parseLong(in.next());
	}

	private int[] na(int n) {
		int[] a = new int[n];
		for (int i = 0; i < n; i++)
			a[i] = ni();
		return a;
	}

	private double nd() {
		return Double.parseDouble(in.next());
	}

	private void tr(Object... o) {
		if (!PROD)
			System.out.println(Arrays.deepToString(o));
	}

	public static void main(String[] args) throws Exception {
		long start = System.nanoTime();

		ExecutorService es = Executors.newFixedThreadPool(NTHREAD);
		CompletionService<C> cs = new ExecutorCompletionService<C>(es);

		if (PROD) {
			System.out.println("INPATH : " + INPATH);
			System.out.println("OUTPATH : " + OUTPATH);
		}
		preprocess();
		Scanner in = PROD ? new Scanner(new File(INPATH)) : new Scanner(INPUT);
		PrintWriter out = PROD ? new PrintWriter(new File(OUTPATH)) : new PrintWriter(System.out);
		int n = in.nextInt();
		in.nextLine();

		for (int i = 0; i < n; i++) {
			C runner = new C(i + 1, in);
			runner.read();
			cs.submit(runner, runner);
		}
		es.shutdown();
		String[] outs = new String[n];
		for (int i = 0; i < n; i++) {
			C runner = cs.take().get(); // not ordered
			runner.out.flush();
			runner.out.close();
			outs[runner.cas - 1] = runner.sw.toString();
		}
		for (int i = 0; i < n; i++) {
			out.printf("Case #%d: ", i + 1);
			out.append(outs[i]);
			out.flush();
		}

		long end = System.nanoTime();
		System.out.println((end - start) / 1000000 + "ms");
		if (PROD) {
			System.out.println("INPATH : " + INPATH);
			System.out.println("OUTPATH : " + OUTPATH);
		}
	}
}
