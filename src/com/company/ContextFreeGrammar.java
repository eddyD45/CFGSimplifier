package com.company;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class ContextFreeGrammar {
    private ArrayList<Character> nonTerminalSymbols;
    private ArrayList<Character> terminalSymbols;
    private ArrayList<Map<Character, ArrayList<String>>> productions;

    public ContextFreeGrammar(ArrayList<Character> nonTerminalSymbols) {
        this.nonTerminalSymbols = nonTerminalSymbols;

    }

    public void generateProductions(ArrayList<Character> nonTerminalSymbols) {
        System.out.println("Generate the productions for your non-terminal symbols.");
        for (Character nonTerminal : nonTerminalSymbols) {
            Scanner sc = new Scanner(System.in);
            System.out.println("What are the productions for: " + nonTerminal + " -> ");
        }
    }

}
