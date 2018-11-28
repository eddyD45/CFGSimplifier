package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        //grammar from Exercise 7
        Map<Character, ArrayList<String>> excercise7Grammar = new HashMap<>();
        excercise7Grammar.put('S', new ArrayList<>(Arrays.asList("aS", "AB", "0")));
        excercise7Grammar.put('A', new ArrayList<>(Arrays.asList("aB", "0")));
        excercise7Grammar.put('B', new ArrayList<>(Arrays.asList("Aa")));
        excercise7Grammar.put('C', new ArrayList<>(Arrays.asList("cCD")));
        excercise7Grammar.put('D', new ArrayList<>(Arrays.asList("ddd", "Cd")));

        Map<Character, ArrayList<String>> exercise10Grammar = new HashMap<>();
        exercise10Grammar.put('S', new ArrayList<>(Arrays.asList("aA", "aBB")));
        exercise10Grammar.put('A', new ArrayList<>(Arrays.asList("aaA", "0")));
        exercise10Grammar.put('B', new ArrayList<>(Arrays.asList("bB", "bbC")));
        exercise10Grammar.put('C', new ArrayList<>(Arrays.asList("B")));

        System.out.println("Original grammar from exercise 7: " + excercise7Grammar.toString());

        System.out.println("REMOVE LAMBDAS");
        Map<Character, ArrayList<String>> excercise7GrammarWithoutLambda = new HashMap<>(removeLambdaProductions(excercise7Grammar));
        System.out.println(excercise7Grammar.toString());
        System.out.println(excercise7GrammarWithoutLambda.toString());

        System.out.println("\nREMOVE VARIABLES");

        for (Map.Entry<Character, ArrayList<String>> entry : excercise7Grammar.entrySet()) {
            System.out.println(isVariableProduction(entry));
        }

        System.out.println("\nREMOVE USELESS PRODUCTIONS");

        for (Map.Entry<Character, ArrayList<String>> entry : excercise7Grammar.entrySet()) {
            System.out.println(isUselessProduction(entry));
        }

        System.out.println("\nOriginal grammar from exercise 10: " + exercise10Grammar.toString());

        System.out.println("REMOVE LAMBDAS");
        Map<Character, ArrayList<String>> exercise10GrammarWithoutLambda = new HashMap<>(removeLambdaProductions(exercise10Grammar));
        System.out.println(exercise10Grammar.toString());
        System.out.println(exercise10GrammarWithoutLambda.toString());

        System.out.println("\nREMOVE VARIABLES");

        for (Map.Entry<Character, ArrayList<String>> entry : exercise10Grammar.entrySet()) {
            System.out.println(isVariableProduction(entry));
        }

        System.out.println("\nREMOVE USELESS PRODUCTIONS");

        for (Map.Entry<Character, ArrayList<String>> entry : exercise10Grammar.entrySet()) {
            System.out.println(isUselessProduction(entry));
        }


    }

    /*
    @param a Map Entry from a CFG
    @return true if lambda production, denoted as a string "0", is found in the value of the entry,
    false if string "0" not found
     */
    public static boolean hasLambdaProduction(Map.Entry<Character, ArrayList<String>> mapEntry) {
        for (String production : mapEntry.getValue()) {
            if (production.equals("0")) {
//                System.out.println("Found lambda I must remove. " + " -> " + mapEntry.toString() );
                return true;
            }
        }
        return false;
    }

    /*
    @param a Map Entry from a CFG
    @return true if a string in the list of productions is an uppercase character, denoting a variable,
    false otherwise
     */
    public static boolean isVariableProduction(Map.Entry<Character, ArrayList<String>> mapEntry) {
        ArrayList<String> productions = mapEntry.getValue();
        for (String production : productions) {
            if (production.length() == 1 && production.matches("[A-Z]")) {
                System.out.println("Found variable production I must remove.");
                return true;
            }
        }
        return false;
    }

    /*
        The idea is: Iterate through all strings in the production of a map entry. Suppose, to begin, your production has no
        non-terminal (considered some upper case character). Check each character in your string. If a character is uppercase,
        set hasNonTerminal to true and end the iteration of the string there. After iterating through a string, check if has
        a non terminal. If it does, do nothing and check the next string. If it does not have a non terminal, return the statement
        with false, meaning this is non a useless production. If it goes through the entire list of strings and finds a non-terminal
        in each string, then the statement returns true; it is a useless statement and must be removed.

        @param Map Entry of a CFG
        @return true if all strings in the productions have at least one uppercase character, denoting a variable,
        false if at least one string in the productions are all lowercase, denoting a production of only terminals
     */
    public static boolean isUselessProduction(Map.Entry<Character, ArrayList<String>> mapEntry) {
        for (String production : mapEntry.getValue()) {
            boolean hasNonTerminal = false;
            for (int i = 0; i < production.length(); i++) {
                if (Character.isUpperCase(production.charAt(i))) {
                    hasNonTerminal = true;
                    break;
                }
            }
            if (!hasNonTerminal) {
                return false;
            }
        }
        System.out.println("Non terminals found in all strings. Found useless production I must remove.");
        return true;
    }

    /*
    @param The original Map of the CFG
    @return new Map of the CFG with lambdas replaced.
     */

    public static Map<Character, ArrayList<String>> removeLambdaProductions(Map<Character, ArrayList<String>> productions) {
        Map<Character, ArrayList<String>> newCFG = new HashMap<>();
        ArrayList<Character> lambdaEntries = new ArrayList<>();

        //check all productions. if any of them have a lambda, add their keys to lambdaEntries to check
        //add them to a new ArrayList to create a new grammar without lambdas, to be used in conjunction with the list
        //of variables we've collected that have lambdas, so we can evaluate a given production with a variable as if it
        //wasn't there
        for (Map.Entry<Character, ArrayList<String>> entry : productions.entrySet()) {
            ArrayList<String> nonLambdaProductions = new ArrayList<>();
            if (hasLambdaProduction(entry)) {
                lambdaEntries.add(entry.getKey());
            }
            for (String production : entry.getValue()) {
                if (!production.equals("0")) {
                    nonLambdaProductions.add(production);
                }
            }
            newCFG.put(entry.getKey(), nonLambdaProductions);
        }

//        go through all productions and replace production strings that have a characterWithLambda with a blank and add
//        it to to a new set of productions. Also keep the original. Ignore lambdas. Push a new entry to a new CFG map with the same key
//        and the new productions (a combo of the old productions and the new productions with lambdas removed.
        for (Character characterWithLambda : lambdaEntries) {
            for (Map.Entry<Character, ArrayList<String>> entry : newCFG.entrySet()) {
                ArrayList<String> newProductions = new ArrayList<>();
                for (String production : entry.getValue()) {
                    newProductions.add(production);
                    if (production.contains(characterWithLambda.toString())) {
                        newProductions.add(production.replace(characterWithLambda.toString(), ""));
                    }
                }
                newCFG.put(entry.getKey(), newProductions);
            }
        }

        return newCFG;
    }
}
