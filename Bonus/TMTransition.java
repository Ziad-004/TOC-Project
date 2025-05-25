package Bonus;

/**
 * TMTransition class represents a single transition in a Turing Machine
 * Each transition specifies:
 * - Current state and symbol being read
 * - Next state to move to
 * - Symbol to write on tape
 * - Direction to move the head (Left or Right)
 */
class TMTransition {
    private String fromState;    // Current state before transition
    private char readSymbol;     // Symbol read from tape
    private String toState;      // State to transition to
    private char writeSymbol;    // Symbol to write on tape
    private char direction;      // Direction to move head (L/R)
    
    /**
     * Creates a new Turing Machine transition
     * @param fromState Current state
     * @param readSymbol Symbol being read from tape
     * @param toState Next state to transition to
     * @param writeSymbol Symbol to write on tape
     * @param direction Direction to move head ('L' for left, 'R' for right)
     */
    public TMTransition(String fromState, char readSymbol, String toState, char writeSymbol, char direction) {
        this.fromState = fromState;
        this.readSymbol = readSymbol;
        this.toState = toState;
        this.writeSymbol = writeSymbol;
        this.direction = direction;
    }
    
    // Getters with descriptive names
    /** Gets the current state before transition */
    public String getFromState() { return fromState; }
    
    /** Gets the symbol being read from tape */
    public char getReadSymbol() { return readSymbol; }
    
    /** Gets the next state to transition to */
    public String getToState() { return toState; }
    
    /** Gets the symbol to write on tape */
    public char getWriteSymbol() { return writeSymbol; }
    
    /** Gets the direction to move head ('L' or 'R') */
    public char getDirection() { return direction; }
    
    /**
     * Returns string representation of transition in format:
     * (currentState, readSymbol) -> (nextState, writeSymbol, direction)
     */
    @Override
    public String toString() {
        return "(" + fromState + ", " + readSymbol + ") -> (" + toState + ", " + writeSymbol + ", " + direction + ")";
    }
}
