
public class Player {
    private String username;
    private int cryptogramsPlayed;
    private int cryptogramsCompleted;
    private int numGuesses;
    private int correctGuesses;

    public Player(String s) {
        username = s;
        cryptogramsPlayed = 0;
        cryptogramsCompleted = 0;
        numGuesses = 0;
        correctGuesses = 0;
    }

    public void setCryptogramsPlayed(int cryptogramsPlayed) {
        this.cryptogramsPlayed = cryptogramsPlayed;
    }

    public void setCryptogramsCompleted(int cryptogramsCompleted) {
        this.cryptogramsCompleted = cryptogramsCompleted;
    }

    public void setNumGuesses(int numGuesses) {
        this.numGuesses = numGuesses;
    }

    public void setCorrectGuesses(int correctGuesses) {
        this.correctGuesses = correctGuesses;
    }

    public void incrementCryptogramsPlayed() {
        ++cryptogramsPlayed;
    }

    public void incrementCryptogramsCompleted() {
        ++cryptogramsCompleted;
    }

    public int getCryptogramsPlayed() {
        return cryptogramsPlayed;
    }

    public int getCryptogramsCompleted() {
        return cryptogramsCompleted;
    }

    public void incrementNumGuesses() {
        ++numGuesses;
    }

    public void incrementCorrectGuesses() {
        ++correctGuesses;
    }

    public int getNumGuesses() {
        return numGuesses;
    }

    public int getCorrectGuesses() {
        return correctGuesses;
    }

    public String getName() {
        return username;
    }

    public double calculatePercentage() {
        double percentage = ((double) correctGuesses / (double) numGuesses) * 100;
        percentage = (double) Math.round(percentage * 100) / 100;
        return percentage;
    }
}
