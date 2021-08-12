package ca.jrvs.practice.dataStructure.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayListAPIs {
  public static void main(String[] args) {
    //List vs ArrayList
    //you don't have to specify the type in <>
    //ArrayList is dynamic length, so no size is required
    List<String> animals = new ArrayList<>();
    //add element
    animals.add("Lion");
    animals.add("Tiger");
    animals.add(2, "Cat");

    //size, not length
    int s = animals.size();

    //get
    String firstElement = animals.get(0);

    //search O(n)
    Boolean hasCat = animals.contains("Cat"); // return true

    //index
    int catIndex = animals.indexOf("Cat"); //return 2

    //remove
    boolean isCatRemoved = animals.remove("Cat"); //remove by object
    String removedElement = animals.remove(1); //remove by index

    //sort
    //pass comparator using lambda
    animals.sort(String::compareToIgnoreCase);

    //convert list ot array
    System.out.println(Arrays.toString(animals.toArray()));
  }
}
