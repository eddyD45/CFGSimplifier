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

        //grammar from Exercise 10
        Map<Character, ArrayList<String>> exercise10Grammar = new HashMap<>();
        exercise10Grammar.put('S', new ArrayList<>(Arrays.asList("aA", "aBB")));
        exercise10Grammar.put('A', new ArrayList<>(Arrays.asList("aaA", "0")));
        exercise10Grammar.put('B', new ArrayList<>(Arrays.asList("bB", "bbC")));
        exercise10Grammar.put('C', new ArrayList<>(Arrays.asList("B")));

        System.out.println("Original grammar from exercise 7: " + excercise7Grammar.toString());

        System.out.println("REMOVE LAMBDAS");
        Map<Character, ArrayList<String>> exercise7GrammarWithoutLambda = new HashMap<>(removeLambdaProductions(excercise7Grammar));
        System.out.println("Before: " + excercise7Grammar.toString());
        System.out.println("After: " + exercise7GrammarWithoutLambda.toString());

        System.out.println("\nREMOVE VARIABLES");

        Map<Character, ArrayList<String>> exercise7GrammarWithoutLambdaOrVariables = new HashMap<>(removeUnitProductions(exercise7GrammarWithoutLambda));
        System.out.println("Before: " + exercise7GrammarWithoutLambda);
        System.out.println("After: " + exercise7GrammarWithoutLambdaOrVariables);

        System.out.println("\nREMOVE USELESS PRODUCTIONS");

        Map<Character, ArrayList<String>> exercise7Simplified = new HashMap<>(removeUselessProductions(exercise7GrammarWithoutLambdaOrVariables));
        System.out.println("Before: " + exercise7GrammarWithoutLambdaOrVariables);
        System.out.println("After: " + exercise7Simplified);

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.println("\nOriginal grammar from exercise 10: " + exercise10Grammar.toString());

        System.out.println("REMOVE LAMBDAS");
        Map<Character, ArrayList<String>> exercise10GrammarWithoutLambda = new HashMap<>(removeLambdaProductions(exercise10Grammar));
        System.out.println("Before: " + exercise10Grammar.toString());
        System.out.println("After: " + exercise10GrammarWithoutLambda.toString());

        System.out.println("\nREMOVE VARIABLES");

        Map<Character, ArrayList<String>> exercise10GrammarWithoutLambdaOrVariables = new HashMap<>(removeUnitProductions(exercise10GrammarWithoutLambda));
        System.out.println("Before: " + exercise10GrammarWithoutLambda);
        System.out.println("After: " + exercise10GrammarWithoutLambdaOrVariables);

        System.out.println("\nREMOVE USELESS PRODUCTIONS");

        Map<Character, ArrayList<String>> exercise10Simplified = new HashMap<>(removeUselessProductions(exercise10GrammarWithoutLambdaOrVariables));
        System.out.println("Before: " + exercise10GrammarWithoutLambdaOrVariables);
        System.out.println("After: " + exercise10Simplified);


    }

    /*
    @param a Map Entry from a CFG
    @return true if lambda production, denoted as a string "0", is found in the value of the entry,
    false if string "0" not found
     */
    public static boolean hasLambdaProduction(Map.Entry<Character, ArrayList<String>> mapEntry) {
        for (String production : mapEntry.getValue()) {
            if (production.equals("0")) {
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
                if (entry.getKey() == 'S') {
                    nonLambdaProductions.add(production);
                } else if (!production.equals("0")) {
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

    /*
    Find productions that have only one character, and check if they are an upper case Character, indicating they are
    unit productions. Add that to a map that holds a Character, the key, and a String, the production. We will swap these
    out later. After finding what keys we're getting rid of, go through and remove them as long as they're not start terminal S.

     */
    public static Map<Character, ArrayList<String>> removeUnitProductions(Map<Character, ArrayList<String>> productions) {
        Map<Character, ArrayList<String>> newCFG = new HashMap<>(productions);
        Map<Character, String> variablesToRemoveFromCFG = new HashMap<>();
        for (Map.Entry<Character, ArrayList<String>> entry : newCFG.entrySet()) {
            if (isVariableProduction(entry)) {
                for (String production : entry.getValue()) {
                    if (production.length() == 1 && production.matches("[A-Z]")) {
                        variablesToRemoveFromCFG.put(entry.getKey(), production);
                    }
                }
            }
        }

        for (Map.Entry<Character, String> entry : variablesToRemoveFromCFG.entrySet()) {
            if (entry.getKey() != 'S') {
                newCFG.remove(entry.getKey());
            }
        }

        for (Map.Entry<Character, String> entryToRemove : variablesToRemoveFromCFG.entrySet()) {
            for (Map.Entry<Character, ArrayList<String>> entry : newCFG.entrySet()) {
                ArrayList<String> newProductions = new ArrayList<>();
                for (String production : entry.getValue()) {
                    if (entry.getKey() != 'S') {
                        newProductions.add(production.replace(entryToRemove.getKey().toString(), entryToRemove.getValue()));
                    } else {
                        newProductions.add(production);
                    }
                }
                newCFG.put(entry.getKey(), newProductions);
            }
        }


        return newCFG;
    }

    /*
    Go through map entries. If the productions of a given entry do not have a string with lower case characters only, it
    is considered useless and removed from the CFG.
     */
    public static Map<Character, ArrayList<String>> removeUselessProductions(Map<Character, ArrayList<String>> productions) {
        Map<Character, ArrayList<String>> newCFG = new HashMap<>(productions);

        for (Map.Entry<Character, ArrayList<String>> entry : productions.entrySet()) {
            if (isUselessProduction(entry)) {
                newCFG.remove(entry.getKey());
            }
        }
        return newCFG;
    }
}
