package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        //grammar from Exercise 7
        Map<Character, ArrayList<String>> excercise7Grammar = new HashMap<>()
        String [] productions1 = {"aS", "AB", "0"};
        ArrayList<String> productionsList = new ArrayList<>(Arrays.asList(productions1));
        excercise7Grammar.put('S', productionsList);

        String [] productions2 = {"aB", "0"};



    }
}
