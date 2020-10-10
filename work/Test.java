import java.util.Random;

public class Test {
  public static int primitiveRoot(int p, int[] primes, Random gen) {
    int[] fs = new int[10];
    int fp = 0;
    int pm = p - 1;
    for (int q : primes) {
      if (q * q > pm)
        break;
      if (pm % q == 0)
        fs[fp++] = (p - 1) / q;
      while (pm % q == 0)
        pm /= q;
    }
    if (pm > 1)
      fs[fp++] = (p - 1) / pm;

    outer: while (true) {
      int g = gen.nextInt(p - 1) + 1;
      for (int i = 0; i < fp; i++) {
        if (pow(g, fs[i], p) == 1)
          continue outer;
      }
      return g;
    }
  }

  public static long pow(long a, long n, long mod) {
    // a %= mod;
    long ret = 1; // 1%mod if mod=1,n=0
    int x = 63 - Long.numberOfLeadingZeros(n);
    for (; x >= 0; x--) {
      ret = ret * ret % mod;
      if (n << ~x < 0)
        ret = ret * a % mod;
    }
    return ret;
  }
}
