package ca.jrvs.practice.dataStructure.list;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LinkedListAPIs {
  public static void main(String[] args) {
    List<String> fruits = new LinkedList<>();
    fruits.add("mango");
    fruits.add("watermelon");
    fruits.add("pear");
    fruits.add("clementines");

    int x = fruits.size();

    //get element from linkedlist
    String secondElement = fruits.get(1);
    System.out.println(secondElement);

    //contains
    boolean hasMango = fruits.contains("mango");

    //set
    fruits.set(2, "oranges");

    //remove
    fruits.remove("clementines");
    fruits.remove(2);

    //indexOf
    int watermelonIndex = fruits.indexOf("watermelon");

    System.out.println(Arrays.toString(fruits.toArray()));
  }



}
