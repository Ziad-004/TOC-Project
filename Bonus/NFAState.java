package Bonus;

import java.util.*;


/**
 * NFAState class represents a state in an NFA (Non-deterministic Finite Automaton)
 * Similar to DFA State but allows multiple transitions for same input and epsilon transitions
 */
class NFAState {
    private int id;                                     // Unique identifier for the state
    private boolean isAccepting;                        // Whether this is an accepting state
    private Map<Character, Set<NFAState>> transitions;  // Multiple states possible for same input
    private Set<NFAState> epsilonTransitions;          // Transitions without consuming input
    
    /**
     * Creates a new NFA state with given ID
     * Initially non-accepting with no transitions
     */
    public NFAState(int id) {
        this.id = id;
        this.isAccepting = false;
        this.transitions = new HashMap<>();
        this.epsilonTransitions = new HashSet<>();
    }
    
    /**
     * Adds a transition on input symbol to another state
     * Multiple states can be reached with same input symbol
     * @param symbol The input symbol
     * @param state The destination state
     */
    public void addTransition(char symbol, NFAState state) {
        transitions.computeIfAbsent(symbol, k -> new HashSet<>()).add(state);
    }
    
    /**
     * Adds an epsilon transition (transition without consuming input)
     * @param state The destination state reachable without consuming input
     */
    public void addEpsilonTransition(NFAState state) {
        epsilonTransitions.add(state);
    }
    
    /**
     * Gets all states reachable with given input symbol
     * @param symbol The input symbol
     * @return Set of states reachable with this symbol
     */
    public Set<NFAState> getTransitions(char symbol) {
        return transitions.getOrDefault(symbol, new HashSet<>());
    }
    
    /**
     * Gets all states reachable through epsilon transitions
     * @return Set of states reachable without consuming input
     */
    public Set<NFAState> getEpsilonTransitions() {
        return epsilonTransitions;
    }
    
    // Basic getters and setters
    public int getId() { return id; }
    public boolean isAccepting() { return isAccepting; }
    public void setAccepting(boolean accepting) { this.isAccepting = accepting; }
    public Map<Character, Set<NFAState>> getAllTransitions() { return transitions; }
}
