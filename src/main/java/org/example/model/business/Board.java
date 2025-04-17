package org.example.model.business;

/**
 * La classe de gestion du plateau
 */
public class Board {

    /**
     * The identifier
     */
    private int id;

    /**
     * The cases of the board
     */
    private Case[] cases;

    /**
     * Default constructor
     */
    public Board() {
        this.cases = new Case[0];  // Or initialize appropriately
        this.id = -1;  // Default ID value
    }

    /**
     * Constructor
     * @param cases an array of cases
     */
    public Board(Case[] cases) {
        this(-1, cases);
    }

    /**
     * Constructor
     * @param id the identifier
     * @param cases an array of cases
     */
    public Board(int id, Case[] cases) {
        this.cases = cases;
        this.id = id;
    }

    /**
     * The size of the board
     * @return the number of cases
     */
    public int getSize() {
        return cases.length;
    }

    /**
     * Set the size of the board (sets the number of cases)
     * @param size the size
     */
    public void setSize(int size) {
        this.cases = new Case[size];  // Or any other initialization logic you need
    }

    /**
     * Get the symbol representation of the board (as a String)
     * @return the symbols of the board
     */
    public String getSymbols() {
        StringBuilder symbols = new StringBuilder();
        for (Case c : cases) {
            symbols.append(c.getSymbol());  // Assuming `Case` has a method `getSymbol()`
        }
        return symbols.toString();
    }

    /**
     * Set the symbols from a string (updates the board with symbols)
     * @param symbols the symbols
     */
    public void setSymbols(String symbols) {
        this.cases = new Case[symbols.length()];
        for (int i = 0; i < symbols.length(); i++) {
            char symbol = symbols.charAt(i);
            Case newCase = new Case(i);  // Create a case at position `i`

            // Associate an effect based on the symbol
            switch (symbol) {
                case 'B':
                    newCase.setCaseEffect(new BackEffect());  // Assign effect for 'B'
                    break;
                case 'Q':
                    newCase.setCaseEffect(new QuestionEffect());  // Assign effect for 'Q'
                    break;
                case 'R':
                    newCase.setCaseEffect(new ReturnEffect());  // Assign effect for 'R'
                    break;
                default:
                    newCase.setCaseEffect(null);  // No effect for empty or unrecognized symbol
            }

            this.cases[i] = newCase;  // Assign the newly created case to the board
        }
    }

    /**
     * Get a case at a given position
     * @param position the position
     * @return the case
     */
    public Case getCase(int position) {
        if (position >= 0 && position < cases.length) {
            return cases[position];
        }
        return null;
    }

    /**
     * Get the array of cases
     * @return the array
     */
    public Case[] getCases() {
        return cases;
    }

    /**
     * Get the identifier
     * @return the identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Set the identifier
     * @param id the identifier
     */
    public void setId(int id) {
        this.id = id;
    }
}
