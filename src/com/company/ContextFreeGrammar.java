package com.company;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class ContextFreeGrammar {
    private Map<Character, ArrayList<String>> productions;

    public ContextFreeGrammar(Map<Character, ArrayList<String>> productions) {
        this.productions = productions;
    }

    public boolean hasLambdaProduction(Map.Entry<Character, ArrayList<String>> mapEntry) {
        for (String production : mapEntry.getValue()) {
            if (production.equals("0")) {
                System.out.println("Found lambda I must remove.");
                return true;
            }
        }
        return false;
    }

    public boolean isVariableProduction(Map.Entry<Character, ArrayList<String>> mapEntry) {
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
     */
    public boolean isUselessProduction(Map.Entry<Character, ArrayList<String>> mapEntry) {
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

    public void removeLambdaProductions(Map<Character, ArrayList<String>> productions) {
        ArrayList<Character> lambdaEntries = new ArrayList<>();
        for (Map.Entry<Character, ArrayList<String>> entry : productions.entrySet()) {
            if (hasLambdaProduction(entry)) {
                lambdaEntries.add(entry.getKey());
            }
        }

        for (Map.Entry<Character, ArrayList<String>> entry : productions.entrySet()) {

        }
    }

}
