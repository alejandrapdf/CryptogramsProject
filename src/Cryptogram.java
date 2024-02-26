import java.util.Random;

/**
 * Represents a cryptogram puzzle.
 */
public class Cryptogram {

    private final String solution;//The solution of the cryptogram puzzle.
    private String[] glyphs;//Table of glyphs replacing the alphabet in the encrypted solution.
    private String[] encrypted;//The encrypted version of the solution.
    private char[] guess; // The guess or partial solution.
    private final int length;//The length of the solution.
    private final boolean type;//The type of the cryptogram - false if alphabetic, true if numeric.

    /**
     * Class constructor.
     *
     * @param type   the type of the cryptogram - false if alphabetic, true if numeric
     * @param phrase the phrase used as a solution for the cryptogram
     */
    public Cryptogram(boolean type, String phrase) {
        this.solution = phrase.toUpperCase();
        this.type = type;
        this.length = phrase.length();
        this.encrypted = new String[length];
        this.guess = new char[length];
        encrypt();
    }

    public boolean getType() {
        return type;
    }

    public String getSolution() {
        return solution;
    }

    public String[] getGlyphs() {
        return glyphs;
    }

    public String[] getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(String[] encrypted) {
        this.encrypted = encrypted;
    }

    public char[] getGuess() {
        return guess;
    }

    public void setGuess(char[] guess) {
        this.guess = guess;
    }

    public int getLength() {
        return length;
    }

    /**
     * Generates a randomized table of glyphs and the encrypted solution, sets up an empty guess.
     */
    private void encrypt() {
        glyphs = new String[26];
        Random r = new Random();
        int glyphOffset;

        //randomly distributing the glyphs
        for (int i = 0; i < 26; i++) {
            glyphOffset = r.nextInt(26 - i);
            while (glyphs[(i + 1 + glyphOffset) % 26] != null) {
                glyphOffset++;
            }
            glyphs[(i + 1 + glyphOffset) % 26] = type ? String.valueOf(i + 1) : String.valueOf((char) ('A' + i));
        }

        //encrypting and setting up an empty guess
        for (int i = 0; i < length; i++) {
            if (Character.isLetter(solution.charAt(i))) {
                encrypted[i] = glyphs[solution.charAt(i) - 'A'];
                guess[i] = '_';
            } else {
                encrypted[i] = String.valueOf(solution.charAt(i));
                guess[i] = solution.charAt(i);
            }
        }
    }

    /**
     * Checks if the cryptogram has been completed
     *
     * @return true if the cryptogram is complete
     */
    public boolean completed() {
        boolean correct = true;
        for (int i = 0; i < length; i++) {
            if (guess[i] != solution.charAt(i)) {
                correct = false;
                break;
            }
        }
        return correct;
    }

}
