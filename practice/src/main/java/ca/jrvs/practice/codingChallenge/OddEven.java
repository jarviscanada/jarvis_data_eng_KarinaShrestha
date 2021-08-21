package ca.jrvs.practice.codingChallenge;

/**
 * ticket URL: https://www.notion.so/jarvisdev/Sample-Check-if-a-number-is-even-or-odd-903cf51350ae4321b63fc90d1c658667
 */
public class OddEven {
  /**
   * Big-O: O(1)
   */
  public String oddEvenMod(int x) {
    return x % 2 ==0 ? "Even" : "Odd";
  }

  /**
   * Big-O: O(1)
   */
  public String oddEvenBit(int x) {
    return (x & 1) == 1 ? "Odd": "Even";
  }
}
