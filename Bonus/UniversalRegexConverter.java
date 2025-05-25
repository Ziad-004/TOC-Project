package Bonus;

import java.util.Scanner;


// Main class that provides the user interface and orchestrates the conversions
public class UniversalRegexConverter {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UniversalRegexToDFA regexConverter = new UniversalRegexToDFA();
        DFAToTuringMachine tmConverter = new DFAToTuringMachine();
        
        System.out.println("=".repeat(70));
        System.out.println("Universal Regular Expression to DFA to Turing Machine Converter");
        System.out.println("=".repeat(70));
        System.out.println("This converter supports ANY regular expression including:");
        System.out.println("- Basic characters: a, b, c, 1, 2, 3, etc.");
        System.out.println("- Concatenation: ab, abc, hello");
        System.out.println("- Alternation: a|b, (cat|dog)");
        System.out.println("- Kleene Star: a*, (ab)*, (a|b)*");
        System.out.println("- Plus: a+, (ab)+");
        System.out.println("- Optional: a?, (hello)?");
        System.out.println("- Parentheses for grouping: (a|b)*c, a(b|c)*d");
        System.out.println("- Escape sequences: \\*, \\+, \\(, \\)");
        System.out.println("- Any character: . (matches a-z, A-Z, 0-9)");
        System.out.println();
        System.out.println("Examples: a*, a+b, (a|b)*c, hello|world, a?b+c*");
        System.out.println("=".repeat(70));
        
        while (true) {
            System.out.print("\nEnter Regular Expression (or 'exit' to quit): ");
            String regex = scanner.nextLine().trim();
            
            if (regex.equalsIgnoreCase("exit")) {
                System.out.println("Thank you for using the Universal Regex Converter!");
                break;
            }
            
            if (regex.isEmpty()) {
                System.out.println("Please enter a valid regex!");
                continue;
            }
            
            try {
                System.out.println("\n" + "=".repeat(60));
                System.out.println("Processing Regex: " + regex);
                System.out.println("=".repeat(60));
                
                // Step 1: Convert Regex to DFA
                System.out.println("Step 1: Converting Regex to DFA...");
                long startTime = System.currentTimeMillis();
                DFA dfa = regexConverter.convertToDFA(regex);
                long endTime = System.currentTimeMillis();
                System.out.println("Conversion completed in " + (endTime - startTime) + "ms");
                dfa.printDFA();
                
                // Step 2: Convert DFA to TM
                System.out.println("Step 2: Converting DFA to Turing Machine...");
                TuringMachine tm = tmConverter.convertToTM(dfa);
                tm.printTM();
                
                // Step 3: Test the automata
                System.out.println("Step 3: Testing the Automata");
                System.out.println("Enter test strings (enter 'done' to finish, 'examples' for suggestions):");
                
                while (true) {
                    System.out.print("Test input: ");
                    String testInput = scanner.nextLine().trim();
                    
                    if (testInput.equalsIgnoreCase("done")) {
                        break;
                    }
                    
                    if (testInput.equalsIgnoreCase("examples")) {
                        System.out.println("Try these examples based on your regex '" + regex + "':");
                        suggestTestCases(regex);
                        continue;
                    }
                    
                    System.out.println("\n" + "-".repeat(50));
                    
                    // Test on DFA first
                    boolean dfaResult = dfa.accept(testInput);
                    System.out.println("DFA result: " + (dfaResult ? "ACCEPTED ✓" : "REJECTED ✗"));
                    
                    System.out.println("\nRunning Turing Machine:");
                    boolean tmResult = tm.run(testInput);
                    
                    System.out.println("\nResult comparison:");
                    System.out.println("DFA: " + (dfaResult ? "ACCEPTED" : "REJECTED"));
                    System.out.println("TM:  " + (tmResult ? "ACCEPTED" : "REJECTED"));
                    
                    if (dfaResult == tmResult) {
                        System.out.println("✓ Results match!");
                    } else {
                        System.out.println("⚠ Results don't match!");
                    }
                    
                    System.out.println("-".repeat(50));
                }
                
            } catch (Exception e) {
                System.out.println("Error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        scanner.close();
    }
    
    // Suggest test cases based on the regex pattern
    private static void suggestTestCases(String regex) {
        System.out.println("Suggested test cases:");
        
        // Basic suggestions
        System.out.println("- Empty string: \"\"");
        System.out.println("- Single characters: \"a\", \"b\", \"c\"");
        
        // Pattern-specific suggestions
        if (regex.contains("*")) {
            System.out.println("- Try empty string and repeated patterns");
        }
        if (regex.contains("+")) {
            System.out.println("- Try single occurrence and multiple occurrences");
        }
        if (regex.contains("|")) {
            System.out.println("- Try both alternatives in the alternation");
        }
        if (regex.contains("?")) {
            System.out.println("- Try with and without the optional part");
        }
        
        // Common test strings
        System.out.println("- Short strings: \"a\", \"ab\", \"abc\"");
        System.out.println("- Longer strings: \"hello\", \"world\", \"test123\"");
        System.out.println("- Numbers: \"123\", \"42\"");
        System.out.println("- Mixed: \"a1b2c3\"");
    }
}

