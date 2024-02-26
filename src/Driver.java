import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Driver {
    private static final Scanner input = new Scanner(System.in);
    private static Game game;
    private static Player player;
    private static Cryptogram cryptogram;
    private static List<String> phrases;

    private static void newGame() {
        String in;
        System.out.println("Starting a new game");
        while (true) {
            System.out.println("Should glyphs be alphabetic or numeric? [A / N]");
            in = input.next();
            if (in.equalsIgnoreCase("A")) {
                cryptogram = game.generateCryptogram(false, phrases.get(player.getCryptogramsCompleted()));
                break;
            } else if (in.equalsIgnoreCase("N")) {
                cryptogram = game.generateCryptogram(true, phrases.get(player.getCryptogramsCompleted()));
                break;
            } else {
                System.out.println("Choose a game cryptogram type to continue.");
            }
        }
        player.incrementCryptogramsPlayed();
    }

    public static void main(String[] args) throws FileNotFoundException {
        String in;
        System.out.println("Cryptogram Game Ver.0.0.0");
        System.out.println("Please enter your name");
        player = new Player(input.next());
        game = new Game(player);
        if ((phrases = game.loadPhrases("phrases.txt")) == null) {
            System.exit(0);
        }
        String filename = "saves/" + player.getName() + ".txt";
        File saveFile = new File(filename);
        game.loadPlayerDetails("player_details.txt", player);
        if (saveFile.exists()) {
            while (true) {
                // asks if they would like to load progress
                System.out.println("You have some previously saved progress on a cryptogram");
                System.out.println("Would you like to continue playing it? [Y / N]");
                in = input.next();
                // if yes...
                if (in.equalsIgnoreCase("Y")) {
                    // load progress
                    game.loadGame(filename);
                    break;
                }
                // if no...
                else if (in.equalsIgnoreCase("N")) {
                    // starts a new game
                    newGame();
                    break;
                }
                // makes sure they answer yes or no
                else {
                    System.out.println("Please answer the question");
                }
            }
        } else {
            // starts a new game
            newGame();
        }

        cryptogram = game.getCurrentCryptogram();

        while (player.getCryptogramsCompleted() < 15) {

            // prints cryptogram and asks which glyph to replace
            game.printCryptogram();
            System.out.println("Which glyph do you wish to replace? (enter \"help\" for a list of additional commands)");
            in = input.next();
            // checks if input is too long, if so displays an error
            if ((in.length() > 2 && cryptogram.getType()))
                game.processInput(in);

            else if (in.length() > 1 && !cryptogram.getType())
                game.processInput(in);
                // if input is in the encryption asks what it should be replaced with
            else if (Arrays.toString(cryptogram.getEncrypted()).contains(in.toUpperCase())) {
                System.out.println("Which character would you like to replace it with?");
                String letter = input.next();
                letter = letter.toUpperCase();
                char character = letter.charAt(0);

                // checks if they have entered a letter and not a special character or multiple
                // letters
                if (letter.length() > 1 || character < 65 || character > 90)
                    game.processInput(letter);
                else
                    game.enterLetter(in, character);
            }
            if (cryptogram.completed()) {
                while (true) {
                    System.out.println("Do you want to play next cryptogram? [Y / N]");
                    String answer = input.next();
                    if (answer.equalsIgnoreCase("y")) {
                        newGame();
                        break;
                    } else if (answer.equalsIgnoreCase("n")) {
                        game.exit();
                        break;
                    } else
                        System.out.println("Please answer the question");
                }

            }

        }
        input.close();
    }
}
