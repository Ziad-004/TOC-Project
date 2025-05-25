package Bonus;

import java.util.*;


/**
 * NFA class represents a complete Non-deterministic Finite Automaton
 * Used as an intermediate representation when converting regex to DFA
 */
class NFA {
    private NFAState startState;    // Initial state of the NFA
    private NFAState acceptState;   // Single accepting state (for Thompson construction)
    private Set<NFAState> states;   // Set of all states in the NFA
    private Set<Character> alphabet; // Input alphabet
    
    /**
     * Creates an NFA with given start and accept states
     * Used during Thompson construction where each NFA has exactly one accept state
     * @param start The initial state
     * @param accept The accepting state
     */
    public NFA(NFAState start, NFAState accept) {
        this.startState = start;
        this.acceptState = accept;
        this.states = new HashSet<>();
        this.alphabet = new HashSet<>();
        accept.setAccepting(true);
    }
    
    // Getters and setters with descriptive names
    /** Gets the initial state of the NFA */
    public NFAState getStartState() { return startState; }
    
    /** Gets the single accepting state of the NFA */
    public NFAState getAcceptState() { return acceptState; }
    
    /** Gets all states in the NFA */
    public Set<NFAState> getStates() { return states; }
    
    /** Gets the input alphabet */
    public Set<Character> getAlphabet() { return alphabet; }
    
    /** Adds a state to the NFA's set of states */
    public void addState(NFAState state) { states.add(state); }
    
    /** Adds a symbol to the input alphabet */
    public void addSymbol(char symbol) { alphabet.add(symbol); }
}
