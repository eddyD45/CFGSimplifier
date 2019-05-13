package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Map<Character, ArrayList<String>> userCFG = getCFGFromUser();


//        //grammar from the example
//        Map<Character, ArrayList<String>> exampleCFG = new HashMap<>();
//        exampleCFG.put('S', new ArrayList<>(Arrays.asList("aA", "aBB")));
//        exampleCFG.put('A', new ArrayList<>(Arrays.asList("aaA", "0")));
//        exampleCFG.put('B', new ArrayList<>(Arrays.asList("bB", "bbC")));
//        exampleCFG.put('C', new ArrayList<>(Arrays.asList("B")));
//
//        System.out.println("\nOriginal grammar from the example: " + exampleCFG.toString());

//        System.out.println("REMOVE EPSILON");
//        Map<Character, ArrayList<String>> exampleCFGNoEpsilon = new HashMap<>(removeEpsilonProductions(exampleCFG));
//        System.out.println("Before: " + exampleCFG.toString());
//        System.out.println("After: " + exampleCFGNoEpsilon.toString());
//
//        System.out.println("\nREMOVE USELESS PRODUCTIONS");
//
//        Map<Character, ArrayList<String>> exampleSimplified = new HashMap<>(removeUselessProductions(exampleCFGNoEpsilon));
//        System.out.println("Before: " + exampleCFG);
//        System.out.println("After: " + exampleSimplified);

        System.out.println("\nOriginal grammar from user: " + userCFG.toString());

        System.out.println("REMOVE EPSILON");
        Map<Character, ArrayList<String>> userCFGNoEpsilon = new HashMap<>(removeEpsilonProductions(userCFG));
        System.out.println("Before: " + userCFG.toString());
        System.out.println("After: " + userCFGNoEpsilon.toString());

        System.out.println("\nREMOVE USELESS PRODUCTIONS");

        Map<Character, ArrayList<String>> userCFGSimplified = new HashMap<>(removeUselessProductions(userCFGNoEpsilon));
        System.out.println("Before: " + userCFGNoEpsilon);
        System.out.println("After: " + userCFGSimplified);


    }

    private static Map<Character, ArrayList<String>> getCFGFromUser() {
        Map<Character, ArrayList<String>> userCFG = new HashMap<>();
        Character userVariable = 'S';
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> instructions = getVariableInstructionsFromUser(userVariable);
        userCFG.put(userVariable, instructions);

        while (userVariable != '/') {
            System.out.println("Enter another variable (enter '/' when done):");
            userVariable = scanner.nextLine().toCharArray()[0];
            if (userVariable != '/') {
                instructions = getVariableInstructionsFromUser(userVariable);
                userCFG.put(userVariable, instructions);
            }
        }

        return userCFG;
    }

    private static ArrayList<String> getVariableInstructionsFromUser(char variable) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> instructions = new ArrayList<>();
        String instructionString = "";
        while (!(instructionString.equals("/"))) {
            System.out.println("Enter instructions for variable " + variable + " (enter '/' when done):");
            instructionString = scanner.nextLine();
            if (!(instructionString.equals("/"))) {
                instructions.add(instructionString);
            }
        }
        return instructions;
    }


    /*
    @param a Map Entry from a CFG
    @return true if epsilon production, denoted as a string "0", is found in the value of the entry,
    false if string "0" not found
     */
    private static boolean hasEpsilonProduction(Map.Entry<Character, ArrayList<String>> mapEntry) {
        for (String production : mapEntry.getValue()) {
            if (production.equals("0")) {
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
    private static boolean isUselessProduction(Map.Entry<Character, ArrayList<String>> mapEntry) {
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

    private static Map<Character, ArrayList<String>> removeEpsilonProductions(Map<Character, ArrayList<String>> productions) {
        Map<Character, ArrayList<String>> newCFG = new HashMap<>();
        ArrayList<Character> epsilonEntries = new ArrayList<>();

        //check all productions. if any of them have a epsilon, add their keys to epsilonEntries to check
        //add them to a new ArrayList to create a new grammar without epsilons, to be used in conjunction with the list
        //of variables we've collected that have epsilons, so we can evaluate a given production with a variable as if it
        //wasn't there
        for (Map.Entry<Character, ArrayList<String>> entry : productions.entrySet()) {
            ArrayList<String> nonEpsilonProductions = new ArrayList<>();
            if (hasEpsilonProduction(entry)) {
                epsilonEntries.add(entry.getKey());
            }
            for (String production : entry.getValue()) {
                if (entry.getKey() == 'S') {
                    nonEpsilonProductions.add(production);
                } else if (!production.equals("0")) {
                    nonEpsilonProductions.add(production);
                }
            }
            newCFG.put(entry.getKey(), nonEpsilonProductions);
        }

//        go through all productions and replace production strings that have a characterWithEpsilon with a blank and add
//        it to to a new set of productions. Also keep the original. Ignore epsilon. Push a new entry to a new CFG map with the same key
//        and the new productions (a combo of the old productions and the new productions with epsilon removed.
        for (Character characterWithEpsilon : epsilonEntries) {
            for (Map.Entry<Character, ArrayList<String>> entry : newCFG.entrySet()) {
                ArrayList<String> newProductions = new ArrayList<>();
                for (String production : entry.getValue()) {
                    newProductions.add(production);
                    if (production.contains(characterWithEpsilon.toString())) {
                        newProductions.add(production.replace(characterWithEpsilon.toString(), ""));
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
    private static Map<Character, ArrayList<String>> removeUselessProductions(Map<Character, ArrayList<String>> productions) {
        Map<Character, ArrayList<String>> newCFG = new HashMap<>(productions);
        ArrayList<Character> removedVariables = new ArrayList<>();

        for (Map.Entry<Character, ArrayList<String>> entry : productions.entrySet()) {
            if (isUselessProduction(entry)) {
                removedVariables.add(entry.getKey());
                newCFG.remove(entry.getKey());
            }
        }

        Map<Character, ArrayList<String>> newCFG2 = new HashMap<>(newCFG);
        for (Map.Entry<Character, ArrayList<String>> entry : newCFG.entrySet()) {
            ArrayList<String> entryInstructionList = new ArrayList<>(entry.getValue());
            for (String instruction :
                    entry.getValue()) {
                for (Character removedVariable :
                        removedVariables) {
                    //if the string contains the removed variable, remove that instruction
                    if (instruction.indexOf(removedVariable) != -1) {
                        entryInstructionList.remove(instruction);
                    }
                }
            }
            newCFG2.put(entry.getKey(), entryInstructionList);
        }
        return newCFG2;
    }
}
