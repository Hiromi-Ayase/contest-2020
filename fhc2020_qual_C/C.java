
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class C implements Runnable {
  static final boolean PROD = true;
  static final int NTHREAD = 1;
  static String BASEPATH = "./";

  static String INPATH = BASEPATH + "timber_sample_input.txt";
  static String OUTPATH = INPATH.substring(0, INPATH.length() - 4) + new SimpleDateFormat("-HHmmss").format(new Date())
      + ".out";

  static String INPUT = "";

  int n;
  int[][] ph;

  public void read() // not parallelized
  {
    n = ni();
    ph = new int[n][];
    for (int i = 0; i < n; i++) {
      ph[i] = new int[] { ni(), ni() };
    }
  }

  public void process() // parallelized!
  {
    Arrays.sort(ph, new Comparator<int[]>() {
      public int compare(int[] a, int[] b) {
        return a[0] - b[0];
      }
    });
    int ans = 0;
    Map<Integer, Integer> dp = new HashMap<>();
    for (int[] u : ph) {
      int t = u[0] + u[1];
      dp.merge(t, dp.getOrDefault(u[0], 0) + u[1], Math::max);
      ans = Math.max(dp.get(t), ans);
    }
    Map<Integer, Integer> dpr = new HashMap<>();
    int[] a = null;
    for (int i = n - 1; i >= 0; i--) {
      int[] u = ph[i];
      int t = u[0] - u[1];
      dpr.merge(t, dpr.getOrDefault(u[0], 0) + u[1], Math::max);

      int now = dpr.getOrDefault(t, 0) + dp.getOrDefault(t, 0);
      if (now > ans) {
        a = new int[] { dpr.getOrDefault(t, 0), dp.getOrDefault(t, 0) };
        ans = Math.max(ans, dpr.getOrDefault(t, 0) + dp.getOrDefault(t, 0));
      }
    }
    System.err.println(Arrays.toString(a));
    out.println(ans);
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
