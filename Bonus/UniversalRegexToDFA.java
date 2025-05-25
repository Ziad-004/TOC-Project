package Bonus;

import java.util.*;


// Universal Regex to DFA converter using Thompson's construction and subset construction
class UniversalRegexToDFA {
    private int stateCounter = 0;
    
    /**
     * Main conversion method that converts a regular expression to a DFA
     * Uses Thompson's construction to convert regex to NFA, then subset construction to convert NFA to DFA
     * @param regex The regular expression to convert
     * @return The resulting DFA
     */
    public DFA convertToDFA(String regex) {
        try {
            // Step 1: Parse and convert regex to NFA using Thompson's construction
            NFA nfa = regexToNFA(regex);
            
            // Step 2: Convert NFA to DFA using subset construction
            DFA dfa = nfaToDFA(nfa);
            
            return dfa;
        } catch (Exception e) {
            System.out.println("Error parsing regex: " + e.getMessage());
            return createEmptyDFA();
        }
    }
    
    /**
     * Converts a regular expression to an NFA using Thompson's construction
     * @param regex The regular expression to convert
     * @return The resulting NFA
     */
    private NFA regexToNFA(String regex) {
        return parseRegex(regex, 0).nfa;
    }
    
    // Result class to hold NFA and current position during parsing
    private static class ParseResult {
        NFA nfa;
        int position;
        
        ParseResult(NFA nfa, int position) {
            this.nfa = nfa;
            this.position = position;
        }
    }
    
    /**
     * Main regex parsing method that handles alternation and concatenation
     * Recursively parses terms and handles alternation operator '|'
     * @param regex The regular expression to parse
     * @param start Starting position in the regex string
     * @return ParseResult containing the constructed NFA and ending position
     */
    private ParseResult parseRegex(String regex, int start) {
        List<NFA> terms = new ArrayList<>();
        int pos = start;
        
        while (pos < regex.length() && regex.charAt(pos) != ')') {
            if (regex.charAt(pos) == '|') {
                pos++; // Skip the '|'
                ParseResult right = parseRegex(regex, pos);
                NFA leftNFA = concatenateNFAs(terms);
                NFA result = alternation(leftNFA, right.nfa);
                return new ParseResult(result, right.position);
            } else {
                ParseResult termResult = parseTerm(regex, pos);
                terms.add(termResult.nfa);
                pos = termResult.position;
            }
        }
        
        return new ParseResult(concatenateNFAs(terms), pos);
    }
    
    /**
     * Parses a single term in the regular expression
     * Handles characters, groups, escaped characters, wildcards, and quantifiers
     * @param regex The regular expression being parsed
     * @param start Starting position in the regex string
     * @return ParseResult containing the constructed NFA and ending position
     */
    private ParseResult parseTerm(String regex, int start) {
        int pos = start;
        NFA baseNFA;
        
        if (pos >= regex.length()) {
            return new ParseResult(createEpsilonNFA(), pos);
        }
        
        char c = regex.charAt(pos);
        
        if (c == '(') {
            // Parse group
            pos++; // Skip '('
            ParseResult groupResult = parseRegex(regex, pos);
            pos = groupResult.position;
            if (pos < regex.length() && regex.charAt(pos) == ')') {
                pos++; // Skip ')'
            }
            baseNFA = groupResult.nfa;
        } else if (c == '\\' && pos + 1 < regex.length()) {
            // Escaped character
            pos++; // Skip '\'
            baseNFA = createCharacterNFA(regex.charAt(pos));
            pos++;
        } else if (c == '.') {
            // Any character (for simplicity, we'll use a-z)
            baseNFA = createAnyCharNFA();
            pos++;
        } else if (isRegularChar(c)) {
            // Regular character
            baseNFA = createCharacterNFA(c);
            pos++;
        } else {
            // Skip invalid characters
            return new ParseResult(createEpsilonNFA(), pos + 1);
        }
        
        // Check for quantifiers
        if (pos < regex.length()) {
            char quantifier = regex.charAt(pos);
            if (quantifier == '*') {
                baseNFA = kleeneStar(baseNFA);
                pos++;
            } else if (quantifier == '+') {
                baseNFA = oneOrMore(baseNFA);
                pos++;
            } else if (quantifier == '?') {
                baseNFA = zeroOrOne(baseNFA);
                pos++;
            }
        }
        
        return new ParseResult(baseNFA, pos);
    }
    
    /**
     * Checks if a character is a regular alphabet character
     * Includes letters, digits, and whitespace
     * @param c The character to check
     * @return true if character is a regular alphabet character, false otherwise
     */
    private boolean isRegularChar(char c) {
        return Character.isLetterOrDigit(c) || " \t".indexOf(c) >= 0;
    }
    
    /**
     * Creates an NFA that accepts a single character
     * @param c The character to create NFA for
     * @return NFA that accepts only the given character
     */
    private NFA createCharacterNFA(char c) {
        NFAState start = new NFAState(stateCounter++);
        NFAState accept = new NFAState(stateCounter++);
        start.addTransition(c, accept);
        
        NFA nfa = new NFA(start, accept);
        nfa.addState(start);
        nfa.addState(accept);
        nfa.addSymbol(c);
        return nfa;
    }
    
    /**
     * Creates an NFA that accepts any character (a-z, A-Z, 0-9)
     * Used for handling wildcard '.' in regex
     * @return NFA that accepts any alphanumeric character
     */
    private NFA createAnyCharNFA() {
        NFAState start = new NFAState(stateCounter++);
        NFAState accept = new NFAState(stateCounter++);
        
        // Add transitions for common characters
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (char c : chars.toCharArray()) {
            start.addTransition(c, accept);
        }
        
        NFA nfa = new NFA(start, accept);
        nfa.addState(start);
        nfa.addState(accept);
        for (char c : chars.toCharArray()) {
            nfa.addSymbol(c);
        }
        return nfa;
    }
    
    /**
     * Creates an NFA that accepts only the empty string (epsilon)
     * @return NFA that accepts only epsilon transitions
     */
    private NFA createEpsilonNFA() {
        NFAState start = new NFAState(stateCounter++);
        NFA nfa = new NFA(start, start);
        nfa.addState(start);
        return nfa;
    }
    
    /**
     * Concatenates a list of NFAs in sequence
     * @param nfas List of NFAs to concatenate
     * @return Single NFA representing concatenation of all input NFAs
     */
    private NFA concatenateNFAs(List<NFA> nfas) {
        if (nfas.isEmpty()) {
            return createEpsilonNFA();
        }
        
        NFA result = nfas.get(0);
        for (int i = 1; i < nfas.size(); i++) {
            result = concatenation(result, nfas.get(i));
        }
        return result;
    }
    
    /**
     * Concatenates two NFAs in sequence
     * Creates NFA that accepts strings accepted by first NFA followed by strings accepted by second NFA
     * @param nfa1 First NFA
     * @param nfa2 Second NFA
     * @return NFA representing concatenation of the two NFAs
     */
    private NFA concatenation(NFA nfa1, NFA nfa2) {
        nfa1.getAcceptState().addEpsilonTransition(nfa2.getStartState());
        nfa1.getAcceptState().setAccepting(false);
        
        NFA result = new NFA(nfa1.getStartState(), nfa2.getAcceptState());
        result.getStates().addAll(nfa1.getStates());
        result.getStates().addAll(nfa2.getStates());
        result.getAlphabet().addAll(nfa1.getAlphabet());
        result.getAlphabet().addAll(nfa2.getAlphabet());
        
        return result;
    }
    
    /**
     * Creates alternation (union) of two NFAs
     * Creates NFA that accepts strings accepted by either of the input NFAs
     * @param nfa1 First NFA
     * @param nfa2 Second NFA
     * @return NFA representing alternation of the two NFAs
     */
    private NFA alternation(NFA nfa1, NFA nfa2) {
        NFAState newStart = new NFAState(stateCounter++);
        NFAState newAccept = new NFAState(stateCounter++);
        
        newStart.addEpsilonTransition(nfa1.getStartState());
        newStart.addEpsilonTransition(nfa2.getStartState());
        
        nfa1.getAcceptState().addEpsilonTransition(newAccept);
        nfa2.getAcceptState().addEpsilonTransition(newAccept);
        nfa1.getAcceptState().setAccepting(false);
        nfa2.getAcceptState().setAccepting(false);
        
        NFA result = new NFA(newStart, newAccept);
        result.addState(newStart);
        result.addState(newAccept);
        result.getStates().addAll(nfa1.getStates());
        result.getStates().addAll(nfa2.getStates());
        result.getAlphabet().addAll(nfa1.getAlphabet());
        result.getAlphabet().addAll(nfa2.getAlphabet());
        
        return result;
    }
    
    /**
     * Kleene star operation - creates an NFA that accepts zero or more repetitions of the input NFA
     * @param nfa The NFA to apply Kleene star to
     * @return A new NFA that accepts zero or more repetitions of the input NFA
     */
    private NFA kleeneStar(NFA nfa) {
        NFAState newStart = new NFAState(stateCounter++);
        NFAState newAccept = new NFAState(stateCounter++);
        
        newStart.addEpsilonTransition(nfa.getStartState());
        newStart.addEpsilonTransition(newAccept);
        
        nfa.getAcceptState().addEpsilonTransition(nfa.getStartState());
        nfa.getAcceptState().addEpsilonTransition(newAccept);
        nfa.getAcceptState().setAccepting(false);
        
        NFA result = new NFA(newStart, newAccept);
        result.addState(newStart);
        result.addState(newAccept);
        result.getStates().addAll(nfa.getStates());
        result.getAlphabet().addAll(nfa.getAlphabet());
        
        return result;
    }
    
    /**
     * One or more operation - creates an NFA that accepts one or more repetitions of the input NFA
     * Implemented as concatenation of NFA with its Kleene star
     * @param nfa The NFA to apply one-or-more operation to
     * @return A new NFA that accepts one or more repetitions of the input NFA
     */
    private NFA oneOrMore(NFA nfa) {
        return concatenation(nfa, kleeneStar(nfa));
    }
    
    /**
     * Zero or one operation - creates an NFA that accepts either empty string or the input NFA
     * Implemented as alternation between NFA and epsilon NFA
     * @param nfa The NFA to apply zero-or-one operation to
     * @return A new NFA that accepts either empty string or the input NFA
     */
    private NFA zeroOrOne(NFA nfa) {
        return alternation(nfa, createEpsilonNFA());
    }
    
    // Convert NFA to DFA using subset construction
    private DFA nfaToDFA(NFA nfa) {
        DFA dfa = new DFA();
        Map<Set<NFAState>, State> stateMap = new HashMap<>();
        Queue<Set<NFAState>> unprocessed = new LinkedList<>();
        int dfaStateId = 0;
        
        // Get epsilon closure of start state
        Set<NFAState> startClosure = epsilonClosure(Set.of(nfa.getStartState()));
        State dfaStart = new State(dfaStateId++);
        stateMap.put(startClosure, dfaStart);
        unprocessed.add(startClosure);
        dfa.setStartState(dfaStart);
        dfa.addState(dfaStart);
        
        // Check if start state should be accepting
        for (NFAState state : startClosure) {
            if (state.isAccepting()) {
                dfaStart.setAccepting(true);
                break;
            }
        }
        
        // Process all state sets
        while (!unprocessed.isEmpty()) {
            Set<NFAState> currentSet = unprocessed.poll();
            State currentDFAState = stateMap.get(currentSet);
            
            // For each symbol in alphabet
            for (char symbol : nfa.getAlphabet()) {
                Set<NFAState> nextSet = new HashSet<>();
                
                // Get all states reachable by this symbol
                for (NFAState state : currentSet) {
                    nextSet.addAll(state.getTransitions(symbol));
                }
                
                if (!nextSet.isEmpty()) {
                    // Get epsilon closure of the result
                    nextSet = epsilonClosure(nextSet);
                    
                    // Create new DFA state if needed
                    State nextDFAState = stateMap.get(nextSet);
                    if (nextDFAState == null) {
                        nextDFAState = new State(dfaStateId++);
                        stateMap.put(nextSet, nextDFAState);
                        unprocessed.add(nextSet);
                        dfa.addState(nextDFAState);
                        
                        // Check if this should be accepting state
                        for (NFAState state : nextSet) {
                            if (state.isAccepting()) {
                                nextDFAState.setAccepting(true);
                                break;
                            }
                        }
                    }
                    
                    currentDFAState.addTransition(symbol, nextDFAState);
                    dfa.addSymbol(symbol);
                }
            }
        }
        
        return dfa;
    }
    
    /**
     * Computes epsilon closure of a set of NFA states
     * Finds all states reachable through epsilon transitions
     * @param states Initial set of states
     * @return Set of all states reachable through epsilon transitions
     */
    private Set<NFAState> epsilonClosure(Set<NFAState> states) {
        Set<NFAState> closure = new HashSet<>(states);
        Queue<NFAState> queue = new LinkedList<>(states);
        
        while (!queue.isEmpty()) {
            NFAState current = queue.poll();
            for (NFAState next : current.getEpsilonTransitions()) {
                if (!closure.contains(next)) {
                    closure.add(next);
                    queue.add(next);
                }
            }
        }
        
        return closure;
    }
    
    /**
     * Creates an empty DFA as fallback when regex parsing fails
     * @return A DFA that rejects all inputs
     */
    private DFA createEmptyDFA() {
        DFA dfa = new DFA();
        State rejecting = new State(0);
        dfa.setStartState(rejecting);
        dfa.addState(rejecting);
        return dfa;
    }
}