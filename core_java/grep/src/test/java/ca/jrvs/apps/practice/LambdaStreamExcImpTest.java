package ca.jrvs.apps.practice;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.Math;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LambdaStreamExcImpTest {
  private LambdaStreamExcImp lse = new LambdaStreamExcImp();;
  int[] numArray = {1, 2, 3, 4, 5};
  String[] words = {"hello", "world", "!"};
  Consumer<String> printer;

  @Before
  public void setUp() throws Exception {
    lse = new LambdaStreamExcImp();
  }

  @Test
  void createStrStream() {
    lse.createStrStream(words).forEach(System.out::println);
  }

  @Test
  void toUpperCase() {
    Stream<String> teststr = lse.toUpperCase("x", "y", "z");
    String result = teststr.collect(Collectors.joining(""));
    assertEquals("XYZ", result);
  }

  @Test
  void createIntStream() {
    IntStream intStream = lse.createIntStream(numArray);
    assertEquals(5, intStream.max().getAsInt());
  }

  @Test
  void toList() {
    Stream<String> teststr = lse.createStrStream("x", "y", "z");
    List<String> tstring = new ArrayList<String>();
    tstring.add("x");
    tstring.add("y");
    tstring.add("z");
    assertEquals(tstring, lse.toList(teststr));
  }

  @Test
  void squareRootIntStream() {
    int[] numsqrt = {1, 9, 25};
    IntStream intStream = lse.createIntStream(numsqrt);
    DoubleStream result = lse.squareRootIntStream(intStream);
    assertEquals(5.0, result.max().getAsDouble());
  }

  @Test
  void getOdd() {
    IntStream intStream = lse.createIntStream(1, 3);
    assertEquals(4, lse.getOdd(intStream));
  }

  @Test
  void printMessages() {
    String[] mssgs = {"h", "e", "l", "l", "o"};
    lse.printMessages(mssgs, lse.getLambdaPrinter("msg:", "!"));
  }

  @Test
  void printOdd() {
    lse.printOdd(lse.createIntStream(0, 5), lse.getLambdaPrinter("odd number: ", "!"));
  }

  @Test
  void flatNestedInt() {
    List<Integer> first = Arrays.asList(1, 5, 10);
    List<Integer> second = Arrays.asList(2, 4, 8);
    List<List<Integer>> numbers= Arrays.asList(first, second);
    Stream<List<Integer>> result = numbers.stream();
    lse.flatNestedInt(result).forEach(System.out::println);

  }
}