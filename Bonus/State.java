package Bonus;

import java.util.HashMap;
import java.util.Map;


/**
 * State class represents a state in a DFA (Deterministic Finite Automaton)
 * Each state has a unique ID, can be accepting/non-accepting, and contains transitions to other states
 */
class State {
    private int id;                              // Unique identifier for the state
    private boolean isAccepting;                 // Whether this is an accepting/final state
    private Map<Character, State> transitions;    // Maps input symbols to next states
    
    /**
     * Creates a new state with the given ID
     * Initially non-accepting with no transitions
     */
    public State(int id) {
        this.id = id;
        this.isAccepting = false;
        this.transitions = new HashMap<>();
    }
    
    /**
     * Adds a transition from this state to another state on the given input symbol
     * @param symbol The input symbol triggering the transition
     * @param state The destination state
     */
    public void addTransition(char symbol, State state) {
        transitions.put(symbol, state);
    }
    
    /**
     * Gets the next state for a given input symbol
     * @param symbol The input symbol
     * @return The next state, or null if no transition exists
     */
    public State getTransition(char symbol) {
        return transitions.get(symbol);
    }
    
    /**
     * Sets whether this state is an accepting/final state
     */
    public void setAccepting(boolean accepting) {
        this.isAccepting = accepting;
    }
    
    /**
     * Checks if this state is an accepting/final state
     */
    public boolean isAccepting() {
        return isAccepting;
    }
    
    /**
     * Gets the unique ID of this state
     */
    public int getId() {
        return id;
    }
    
    /**
     * Gets all transitions from this state
     * @return Map of input symbols to their corresponding next states
     */
    public Map<Character, State> getTransitions() {
        return transitions;
    }
}
