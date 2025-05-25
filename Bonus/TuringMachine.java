package Bonus;

import java.util.*;


/**
 * TuringMachine class represents a complete Turing Machine
 * A TM is more powerful than DFAs/NFAs as it can both read and write to its tape
 * This implementation is used as the final stage of regex processing
 */
class TuringMachine {
    private Set<String> states;              // Set of all states
    private Set<Character> inputAlphabet;    // Input alphabet (initial tape symbols)
    private Set<Character> tapeAlphabet;     // Tape alphabet (includes blank symbol)
    private List<TMTransition> transitions;  // List of all transitions
    private String startState;               // Initial state
    private Set<String> acceptStates;        // Set of accepting states
    private String rejectState;              // Rejecting state
    private char blankSymbol;               // Blank symbol for tape
    
    /**
     * Creates an empty Turing Machine
     * Uses '_' as the blank symbol
     */
    public TuringMachine() {
        this.states = new HashSet<>();
        this.inputAlphabet = new HashSet<>();
        this.tapeAlphabet = new HashSet<>();
        this.transitions = new ArrayList<>();
        this.acceptStates = new HashSet<>();
        this.blankSymbol = '_';
    }
    
    /**
     * Adds a state to the TM's set of states
     * @param state The state to add
     */
    public void addState(String state) { states.add(state); }
    
    /**
     * Adds a symbol to the input alphabet
     * @param symbol The symbol to add
     */
    public void addInputSymbol(char symbol) { inputAlphabet.add(symbol); }
    
    /**
     * Adds a symbol to the tape alphabet
     * @param symbol The symbol to add
     */
    public void addTapeSymbol(char symbol) { tapeAlphabet.add(symbol); }
    
    /**
     * Adds a transition to the TM's transition function
     * @param transition The transition to add
     */
    public void addTransition(TMTransition transition) { transitions.add(transition); }
    
    /**
     * Sets the initial state of the TM
     * @param state The state to set as initial state
     */
    public void setStartState(String state) { this.startState = state; }
    
    /**
     * Adds a state to the set of accepting states
     * @param state The state to add as accepting state
     */
    public void addAcceptState(String state) { acceptStates.add(state); }
    
    /**
     * Sets the rejecting state of the TM
     * @param state The state to set as rejecting state
     */
    public void setRejectState(String state) { this.rejectState = state; }
    
    /**
     * Finds the appropriate transition for current state and symbol
     * @param currentState The current state
     * @param currentSymbol The current symbol under the head
     * @return The matching transition, or null if none exists
     */
    private TMTransition findTransition(String currentState, char currentSymbol) {
        for (TMTransition t : transitions) {
            if (t.getFromState().equals(currentState) && t.getReadSymbol() == currentSymbol) {
                return t;
            }
        }
        return null;
    }
    
    /**
     * Runs the Turing Machine on given input string
     * Simulates the TM until it accepts, rejects, or reaches max steps
     * @param input The input string to process
     * @return true if string is accepted, false if rejected
     */
    public boolean run(String input) {
        // Initialize tape with input
        List<Character> tape = new ArrayList<>();
        for (char c : input.toCharArray()) {
            tape.add(c);
        }
        if (tape.isEmpty()) {
            tape.add(blankSymbol);
        }
        
        String currentState = startState;
        int headPosition = 0;
        int steps = 0;
        int maxSteps = 1000;  // Prevent infinite loops
        
        System.out.println("Starting TM on input: \"" + input + "\"");
        System.out.println("Initial tape: " + tapeToString(tape, headPosition));
        
        while (steps < maxSteps) {
            // Check for accepting state
            if (acceptStates.contains(currentState)) {
                System.out.println("String ACCEPTED! Final state: " + currentState);
                return true;
            }
            
            // Check for rejecting state
            if (currentState.equals(rejectState)) {
                System.out.println("String REJECTED! Final state: " + currentState);
                return false;
            }
            
            // Handle tape boundaries
            if (headPosition < 0) {
                headPosition = 0;
                tape.add(0, blankSymbol);
            }
            if (headPosition >= tape.size()) {
                tape.add(blankSymbol);
            }
            
            // Find and apply transition
            char currentSymbol = tape.get(headPosition);
            TMTransition transition = findTransition(currentState, currentSymbol);
            
            if (transition == null) {
                System.out.println("No transition from state " + currentState + " on symbol " + currentSymbol);
                System.out.println("String REJECTED!");
                return false;
            }
            
            // Apply transition: write symbol and change state
            tape.set(headPosition, transition.getWriteSymbol());
            currentState = transition.getToState();
            
            // Move head
            if (transition.getDirection() == 'L') {
                headPosition--;
            } else if (transition.getDirection() == 'R') {
                headPosition++;
            }
            
            // Print step (limited to first 10 steps)
            steps++;
            if (steps <= 10) {
                System.out.println("Step " + steps + ": " + transition + " | Tape: " + tapeToString(tape, headPosition));
            } else if (steps == 11) {
                System.out.println("... (showing only first 10 steps)");
            }
        }
        
        System.out.println("Maximum steps reached!");
        return false;
    }
    
    /**
     * Creates string representation of tape contents with head position marked
     * @param tape The current tape contents
     * @param headPosition Current position of the head
     * @return String showing tape contents with head position marked by []
     */
    private String tapeToString(List<Character> tape, int headPosition) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tape.size(); i++) {
            if (i == headPosition) {
                sb.append("[").append(tape.get(i)).append("]");
            } else {
                sb.append(tape.get(i));
            }
        }
        return sb.toString();
    }
    
    /**
     * Prints human-readable representation of the TM structure
     * Shows states, alphabets, and number of transitions
     */
    public void printTM() {
        System.out.println("=== Turing Machine Structure ===");
        System.out.println("States: " + states);
        System.out.println("Input Alphabet: " + inputAlphabet);
        System.out.println("Start State: " + startState);
        System.out.println("Accept States: " + acceptStates);
        System.out.println("Reject State: " + rejectState);
        System.out.println("Number of Transitions: " + transitions.size());
        System.out.println();
    }
}