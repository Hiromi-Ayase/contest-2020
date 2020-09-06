class Solution {

  public static void main(String[] args) {
    System.out.println(new Solution().minCost("abaac", new int[]{1,2,3,4,5}));
  }
  public int minCost(String s, int[] cost) {
    int n = s.length();

    int ret = 0;
    for (int from = 0; from < n;) {
      int to = from;
      int max = cost[to];
      int sum = 0;
      while (to < n && s.charAt(to) == s.charAt(from)) {
        max = Math.max(max, cost[to]);
        sum += cost[to];
        to ++;
      }
      ret += sum - max;
      from = to;
    }
    return ret;
  }
}