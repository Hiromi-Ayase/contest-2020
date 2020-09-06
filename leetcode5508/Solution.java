import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Solution {
  public int numTriplets(int[] nums1, int[] nums2) {
    int n = nums1.length;
    int m = nums2.length;

    Map<Long, Integer> set1 = new HashMap<>();
    Map<Long, Integer> set2 = new HashMap<>();
    for (int v : nums1) {
      long x = (long) v * v;
      set1.putIfAbsent(x, 0);
      set1.put(x, set1.get(x) + 1);
    }
    for (int v : nums2) {
      long x = (long) v * v;
      set2.putIfAbsent(x, 0);
      set2.put(x, set2.get(x) + 1);
    }

    int ret = 0;
    for (int j = 0; j < n; j++) {
      for (int k = j + 1; k < n; k++) {
        long x = nums1[j];
        long y = nums1[k];
        if (set2.containsKey(x * y)) {
          ret+= set2.get(x * y);
        }
      }
    }
    for (int j = 0; j < m; j++) {
      for (int k = j + 1; k < m; k++) {
        long x = nums2[j];
        long y = nums2[k];
        if (set1.containsKey(x * y)) {
          ret+= set1.get(x * y);
        }
      }
    }
    return ret;
  }
}
