package Bonus;

import java.util.*;


/**
 * DFA class represents a complete Deterministic Finite Automaton
 * A DFA has exactly one transition for each input symbol from each state
 * Used as the final representation of the regex pattern
 */
class DFA {
    private State startState;       // Initial state of the DFA
    private Set<State> states;      // Set of all states in the DFA
    private Set<Character> alphabet; // Input alphabet
    
    /**
     * Creates an empty DFA
     * States and alphabet must be added using appropriate methods
     */
    public DFA() {
        this.states = new HashSet<>();
        this.alphabet = new HashSet<>();
    }
    
    /**
     * Sets the initial/start state of the DFA
     * @param state The state to set as initial state
     */
    public void setStartState(State state) {
        this.startState = state;
    }
    
    /**
     * Adds a state to the DFA's set of states
     * @param state The state to add
     */
    public void addState(State state) {
        states.add(state);
    }
    
    /**
     * Adds an input symbol to the DFA's alphabet
     * @param symbol The symbol to add
     */
    public void addSymbol(char symbol) {
        alphabet.add(symbol);
    }
    
    /**
     * Tests whether the DFA accepts a given input string
     * Follows transitions for each input symbol and checks if final state is accepting
     * @param input The string to test
     * @return true if string is accepted, false if rejected
     */
    public boolean accept(String input) {
        State currentState = startState;
        
        for (char c : input.toCharArray()) {
            State nextState = currentState.getTransition(c);
            if (nextState == null) {
                return false;  // Reject if no valid transition exists
            }
            currentState = nextState;
        }
        
        return currentState.isAccepting();
    }
    
    /**
     * Determines if a state is a dead state
     * A dead state is non-accepting and either has no transitions
     * or all transitions lead back to itself
     * @param state The state to check
     * @return true if state is a dead state, false otherwise
     */
    private boolean isDeadState(State state) {
        if (state.isAccepting()) {
            return false;
        }
        
        Map<Character, State> transitions = state.getTransitions();
        if (transitions.isEmpty()) {
            return true;
        }
        
        for (State nextState : transitions.values()) {
            if (nextState != state) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Prints a human-readable representation of the DFA structure
     * Shows states, transitions, and identifies special states (start, accepting, dead)
     */
    public void printDFA() {
        System.out.println("=== DFA Structure ===");
        System.out.println("Alphabet: " + alphabet);
        System.out.println("States:");
        
        // Print regular states
        for (State state : states) {
            if (!isDeadState(state)) {
                String status = state.isAccepting() ? " (Accepting)" : "";
                String start = (state == startState) ? " (Start)" : "";
                System.out.println("State " + state.getId() + status + start);
                
                for (Map.Entry<Character, State> entry : state.getTransitions().entrySet()) {
                    System.out.println("  " + entry.getKey() + " -> State " + entry.getValue().getId());
                }
            }
        }
        
        // Print dead states
        System.out.println("\nDead States:");
        boolean hasDeadStates = false;
        for (State state : states) {
            if (isDeadState(state)) {
                hasDeadStates = true;
                System.out.println("State " + state.getId() + " (Dead State)");
                
                for (Map.Entry<Character, State> entry : state.getTransitions().entrySet()) {
                    System.out.println("  " + entry.getKey() + " -> State " + entry.getValue().getId());
                }
            }
        }
        
        if (!hasDeadStates) {
            System.out.println("  No dead states in this DFA");
        }
        
        System.out.println();
    }
    
    // Getters used by TM conversion
    /** Gets all states in the DFA */
    public Set<State> getStates() { return states; }
    
    /** Gets the initial state of the DFA */
    public State getStartState() { return startState; }
    
    /** Gets the input alphabet */
    public Set<Character> getAlphabet() { return alphabet; }
}