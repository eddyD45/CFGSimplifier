package com.company;

import java.util.ArrayList;
import java.util.Map;

public class ContextFreeGrammar {
    private ArrayList<Character> nonTerminalSymbols;
    private ArrayList<Character> terminalSymbols;
    private ArrayList<Map<Character, ArrayList<String>>> productions;

    public ContextFreeGrammar(ArrayList<Character> nonTerminalSymbols, ArrayList<Character> terminalSymbols) {
        this.nonTerminalSymbols = nonTerminalSymbols;
        this.terminalSymbols = terminalSymbols;
        
    }

}
