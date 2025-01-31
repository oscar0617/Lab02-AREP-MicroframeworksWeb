package edu.escuelaing.arep;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Generics {
    public static void main(String[] args) {
        List<Integer> intList = new LinkedList();
        intList.add(0);

        Integer x = (Integer) intList.iterator().next();
        
    }
}
