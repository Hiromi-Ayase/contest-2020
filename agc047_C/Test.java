import java.util.Arrays;
import java.util.Random;

public class Test {
  public static void main(String[] args) {
    Random gen = new Random();
    int[] primes = sieveEratosthenes(1000000);
    System.out.println(primitiveRoot(200003, primes, gen));
  }

  public static int[] sieveEratosthenes(int n) {
    if (n <= 32) {
      int[] primes = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31 };
      for (int i = 0; i < primes.length; i++) {
        if (n < primes[i]) {
          return Arrays.copyOf(primes, i);
        }
      }
      return primes;
    }

    int u = n + 32;
    double lu = Math.log(u);
    int[] ret = new int[(int) (u / lu + u / lu / lu * 1.5)];
    ret[0] = 2;
    int pos = 1;

    int[] isnp = new int[(n + 1) / 32 / 2 + 1];
    int sup = (n + 1) / 32 / 2 + 1;

    int[] tprimes = { 3, 5, 7, 11, 13, 17, 19, 23, 29, 31 };
    for (int tp : tprimes) {
      ret[pos++] = tp;
      int[] ptn = new int[tp];
      for (int i = (tp - 3) / 2; i < tp << 5; i += tp)
        ptn[i >> 5] |= 1 << i;
      for (int j = 0; j < sup; j += tp) {
        for (int i = 0; i < tp && i + j < sup; i++) {
          isnp[j + i] |= ptn[i];
        }
      }
    }

    // 3,5,7
    // 2x+3=n
    int[] magic = { 0, 1, 23, 2, 29, 24, 19, 3, 30, 27, 25, 11, 20, 8, 4, 13, 31, 22, 28, 18, 26, 10, 7, 12, 21, 17, 9,
        6, 16, 5, 15, 14 };
    int h = n / 2;
    for (int i = 0; i < sup; i++) {
      for (int j = ~isnp[i]; j != 0; j &= j - 1) {
        int pp = i << 5 | magic[(j & -j) * 0x076be629 >>> 27];
        int p = 2 * pp + 3;
        if (p > n)
          break;
        ret[pos++] = p;
        if ((long) p * p > n)
          continue;
        for (int q = (p * p - 3) / 2; q <= h; q += p)
          isnp[q >> 5] |= 1 << q;
      }
    }

    return Arrays.copyOf(ret, pos);
  }

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
