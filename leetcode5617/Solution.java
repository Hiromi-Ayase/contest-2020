import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Solution {
  public int minimumIncompatibility(int[] nums, int k) {
    int n = nums.length;
    int m = n / k;
    Arrays.sort(nums);
    int ret = dfs(0, 0, 0, new int[k][m], new boolean[n], new int[n + 1], nums, k, m, 0);
    return ret >= Integer.MAX_VALUE / 2 ? -1 : ret;
  }

  private static int dfs(int i, int j, int p, int[][] buc, boolean[] used, int[] numUse, int[] nums, int k, int m,
      int v) {
    int n = nums.length;
    if (i == k) {
      return v;
    }
    int ret = Integer.MAX_VALUE;
    if (j == m) {
      int min = Integer.MAX_VALUE;
      int max = -1;
      boolean ok = true;
      for (int s = 0; s < m; s++) {
        if (s > 0 && buc[i][s] == buc[i][s - 1]) {
          ok = false;
          break;
        }
        min = Math.min(min, buc[i][s]);
        max = Math.max(max, buc[i][s]);
      }

      ret = ok ? dfs(i + 1, 0, 0, buc, used, numUse, nums, k, m, v + (max - min)) : Integer.MAX_VALUE;
    } else {
      for (int s = p; s < n; s++) {
        if (used[s])
          continue;
        used[s] = true;
        buc[i][j] = nums[s];
        ret = Math.min(ret, dfs(i, j + 1, s + 1, buc, used, numUse, nums, k, m, v));
        used[s] = false;

        if (j == 0)
          break;
      }
    }
    return ret;
  }

  // [7,3,3,9,4,4,9,9,3,8,5]
  // 11
  public static void main(String[] args) {
    // int[] nums = { 7, 3, 3, 9, 4, 4, 9, 9, 3, 8, 5, 9, 3, 8, 5 };
    // int k = nums.length;

    int[] nums = { 6, 3, 8, 1, 3, 1, 2, 2 };
    int k = 4;
    System.out.println(new Solution().minimumIncompatibility(nums, k));
  }
}
