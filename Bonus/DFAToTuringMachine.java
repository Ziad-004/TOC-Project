package Bonus;

import java.util.Map;


// DFAToTuringMachine class handles conversion from DFA to Turing Machine
class DFAToTuringMachine {
    
    /**
     * Converts a DFA to an equivalent Turing Machine
     * Creates a TM that simulates the DFA by:
     * 1. Reading input symbols from left to right
     * 2. Transitioning between states based on DFA transitions
     * 3. Accepting/rejecting based on DFA accepting states
     * @param dfa The DFA to convert
     * @return Equivalent Turing Machine
     */
    public TuringMachine convertToTM(DFA dfa) {
        TuringMachine tm = new TuringMachine();
        
        for (State state : dfa.getStates()) {
            tm.addState("q" + state.getId());
            if (state.isAccepting()) {
                tm.addAcceptState("q" + state.getId());
            }
        }
        
        tm.addState("qReject");
        tm.setRejectState("qReject");
        
        for (char symbol : dfa.getAlphabet()) {
            tm.addInputSymbol(symbol);
            tm.addTapeSymbol(symbol);
        }
        tm.addTapeSymbol('#');
        
        tm.setStartState("q" + dfa.getStartState().getId());
        
        for (State state : dfa.getStates()) {
            String stateName = "q" + state.getId();
            
            for (Map.Entry<Character, State> transition : state.getTransitions().entrySet()) {
                char inputSymbol = transition.getKey();
                String toState = "q" + transition.getValue().getId();
                
                TMTransition tmTransition = new TMTransition(
                    stateName, inputSymbol, toState, inputSymbol, 'R'
                );
                tm.addTransition(tmTransition);
            }
            
            if (state.isAccepting()) {
                TMTransition blankTransition = new TMTransition(
                    stateName, '#', stateName, '#', 'R' 
                );
                tm.addTransition(blankTransition);
            } else {
                TMTransition blankTransition = new TMTransition(
                    stateName, '#', "qReject", '#', 'R'
                );
                tm.addTransition(blankTransition);
            }
        }
        
        return tm;
    }
}