package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class OddEvenTest {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void oddEvenMod() {
    OddEven oddeven = new OddEven();
    assertEquals("Even", oddeven.oddEvenMod(8));
    assertEquals("Odd", oddeven.oddEvenMod(5));
    assertEquals("Odd", oddeven.oddEvenMod(9));
  }

  @Test
  public void oddEvenBit() {
    OddEven oddeven = new OddEven();
    assertEquals("Odd", oddeven.oddEvenBit(3));
    assertEquals("Even", oddeven.oddEvenBit(10));
    assertEquals("Even", oddeven.oddEvenBit(0));

  }
}