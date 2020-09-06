class Solution {
  public static void main(String[] args) {

  }

  public String modifyString(String s) {
    char[] ret = s.toCharArray();
    int n = ret.length;
    for (int i = 0; i < n; i++) {
      char pre = i == 0 ? 0 : ret[i-1];
      char nex = i == n - 1 ? 0 : ret[i+1];

      if (ret[i] == '?') {
        for (int j = 0; j < 26; j++) {
          char c = (char) (j + 'a');
          if (c != pre && c != nex) {
            ret[i] = c;
            break;
          }
        }
      }
    }
    return new String(ret);
  }
}