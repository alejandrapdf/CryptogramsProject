import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

class Tests {


    private final ByteArrayOutputStream console = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(console));
    }




    /**
     * Cryptogram stats tests
     */
    @Test
    void statsTest() {
        Player testPlayer = new Player("Jack");
        Game game = new Game(testPlayer);
        Cryptogram cryptogram = game.generateCryptogram(false, "HI");
        game.enterLetter(cryptogram.getEncrypted()[1], 'G');
        assertEquals(cryptogram.getGuess()[1], 'G');
        game.enterLetter(cryptogram.getEncrypted()[0], 'A');
        game.undoLetter('A');
        game.enterLetter(cryptogram.getEncrypted()[0], 'H');
        assertEquals(cryptogram.getGuess()[0], 'H');
        game.undoLetter('G');
        game.enterLetter(cryptogram.getEncrypted()[1], 'I');
        assertEquals(testPlayer.getNumGuesses(), 4);
        assertEquals(testPlayer.getCorrectGuesses(), 2);
        assertEquals(testPlayer.calculatePercentage(), 50.0);
    }

    /**
     * Saving cryptogram tests
     */
    @Test
    void newSaveTest() {
        File testFile = new File("saves/Goodfellow.txt");
        Scanner fileInput;
        //starts a game and makes a letter guess
        Player testPlayer = new Player("Goodfellow");
        Game game = new Game(testPlayer);
        Cryptogram cryptogram = game.generateCryptogram(true, "HELLO");
        game.enterLetter(cryptogram.getEncrypted()[0], 'U');

        String[] current = new String[4];
        //checks for expected data in file
        try {
            game.saveGame("saves/Goodfellow.txt");
            fileInput = new Scanner(testFile);


            while (fileInput.hasNext()) {
                for (int i = 0; i < 4 && fileInput.hasNext(); i++) {
                    current[i] = fileInput.nextLine();
                }
            }
            String encrypted = "";
            for (String glyph : cryptogram.getEncrypted()) {
                encrypted = encrypted + glyph + ",";
            }
            assertEquals(current[0], "n");
            assertEquals(current[1], "HELLO");
            assertEquals(current[2], encrypted);
            assertEquals(current[3], "U____");

            fileInput.close();
            testFile.delete();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }
    
    // Save file exists and player chooses to overwrite it
    @Test
    void overwriteTest() {
        File testFile = new File("saves/Goodfellow.txt");
        Scanner fileInput;
        //starts a game and makes a letter guess
        Player testPlayer = new Player("Goodfellow");
        Game game = new Game(testPlayer);
        Cryptogram cryptogram = game.generateCryptogram(false, "HELLO");
        game.enterLetter(cryptogram.getEncrypted()[0], 'G');

        String[] current = new String[4];
        //checks for expected data in file
        try {
            game.saveGame("saves/Goodfellow.txt");
            fileInput = new Scanner(testFile);


            while (fileInput.hasNext()) {
                for (int i = 0; i < 4 && fileInput.hasNext(); i++) {
                    current[i] = fileInput.nextLine();
                }
            }
            String encrypted = "";
            for (String glyph : cryptogram.getEncrypted()) {
                encrypted = encrypted + glyph + ",";
            }
            assertEquals(current[0], "a");
            assertEquals(current[1], "HELLO");
            assertEquals(current[2], encrypted);
            assertEquals(current[3], "G____");

            fileInput.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }

        //enters another letter
        game.enterLetter(cryptogram.getEncrypted()[1], 'A');

        //replies no to the overwrite request
        InputStream inBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Y\n".getBytes());
        System.setIn(in);
        game.input = new Scanner(System.in);
        game.processInput("save");
        System.setIn(inBackup);
        //checks that file is still the same
        try {
            fileInput = new Scanner(testFile);


            while (fileInput.hasNext()) {
                for (int i = 0; i < 4 && fileInput.hasNext(); i++) {
                    current[i] = fileInput.nextLine();
                }
            }
            String encrypted = "";
            for (String glyph : cryptogram.getEncrypted()) {
                encrypted = encrypted + glyph + ",";
            }
            assertEquals(current[0], "a");
            assertEquals(current[1], "HELLO");
            assertEquals(current[2], encrypted);
            assertEquals(current[3], "GA___");

            fileInput.close();
            testFile.delete();

        } catch (FileNotFoundException e) {
            fail();
        }
    }

    // Save file exists and player chooses not to overwrite it
    @Test
    void noOverwriteTest() {
        File testFile = new File("saves/Goodfellow.txt");
        Scanner fileInput;
        //starts a game and makes a letter guess
        Player testPlayer = new Player("Goodfellow");
        Game game = new Game(testPlayer);
        Cryptogram cryptogram = game.generateCryptogram(false, "HELLO");
        game.enterLetter(cryptogram.getEncrypted()[0], 'G');

        String[] current = new String[4];
        //checks for expected data in file
        try {
            game.saveGame("saves/Goodfellow.txt");
            fileInput = new Scanner(testFile);


            while (fileInput.hasNext()) {
                for (int i = 0; i < 4 && fileInput.hasNext(); i++) {
                    current[i] = fileInput.nextLine();
                }
            }
            String encrypted = "";
            for (String glyph : cryptogram.getEncrypted()) {
                encrypted = encrypted + glyph + ",";
            }
            assertEquals(current[0], "a");
            assertEquals(current[1], "HELLO");
            assertEquals(current[2], encrypted);
            assertEquals(current[3], "G____");

            fileInput.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }

        //enters another letter
        game.enterLetter(cryptogram.getEncrypted()[1], 'A');

        //replies no to the overwrite request
        InputStream inBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("N\n".getBytes());
        System.setIn(in);
        game.input = new Scanner(System.in);
        game.processInput("save");
        System.setIn(inBackup);
        //checks that file is still the same
        try {
            fileInput = new Scanner(testFile);


            while (fileInput.hasNext()) {
                for (int i = 0; i < 4 && fileInput.hasNext(); i++) {
                    current[i] = fileInput.nextLine();
                }
            }
            String encrypted = "";
            for (String glyph : cryptogram.getEncrypted()) {
                encrypted = encrypted + glyph + ",";
            }
            assertEquals(current[0], "a");
            assertEquals(current[1], "HELLO");
            assertEquals(current[2], encrypted);
            assertEquals(current[3], "G____");

            fileInput.close();
            testFile.delete();

        } catch (FileNotFoundException e) {
            fail();
        }
    }
    
    /**
     * Loading cryptogram tests
     */
    void loading() {
        //creates new file
        File testFile = new File("test_load.txt");
        //starts a game and makes a letter guess
        Player testPlayer = new Player("Kevin");
        Game game = new Game(testPlayer);
        Cryptogram cryptogram = game.generateCryptogram(false, "HELLO");
        game.enterLetter(cryptogram.getEncrypted()[0], 'G');

        try {
            //saves game
            game.saveGame("test_load.txt");
            //continues on current game makes a letter guess
            game.enterLetter(cryptogram.getEncrypted()[3], 'K');
            assertEquals(cryptogram.getGuess()[3], "K");
            //loads game
            game.loadGame("test_load.txt");
            //checks that changes that were made before load are not in the guess
            assertEquals(cryptogram.getGuess()[0], "G");
            assertNotEquals(cryptogram.getGuess()[3], "K");

        } catch (FileNotFoundException e) {
            fail();
        }

        testFile.delete();

    }

    void loadingWhenNoPreviousData() {
        //creates new file
        File testFile = new File("load_test.txt");
        //starts a game
        Player testPlayer = new Player("Derek");
        Game game = new Game(testPlayer);

        //tries to load game without previous progress
        game.loadGame("load_test.txt");
        game.processInput("load");
        //checks for error message
        assertEquals(console.toString().trim(), "You have no previously saved progress.");

        testFile.delete();

    }


    @Test
    void corruptFile() {
        //creates new file
        File testFile = new File("load_test.txt");
        //starts a game
        Player testPlayer = new Player("Kira");
        Game game = new Game(testPlayer);
        game.generateCryptogram(false, "HELLO");

        try {
            //saves game
            game.saveGame("load_test.txt");
            //overwrites file with gibberish
            FileOutputStream fos = new FileOutputStream("load_test.txt");
            try {
                fos.write("3DK!".getBytes());

                fos.close();
                //tries to load game and checks for error message
                game.loadGame("load_test.txt");
                assertTrue(console.toString().trim().contains("Corrupt data in the save file, could not load."));


            } catch (IOException e) {
                fail();
            }

        } catch (FileNotFoundException e) {
            fail();
        }

        testFile.delete();

    }
    
    /**
     * 6.Show solution
     */
    
    @Test
    void showSolution() {
    	String name = "Lewis";
        Player player = new Player(name);
        Game game = new Game(player);
        Cryptogram cryptogram = game.generateCryptogram(false, "HI");
        game.enterLetter(cryptogram.getEncrypted()[0], 'S');
        game.showSolution();
        assertTrue(console.toString().contains("Solution: H I"));
    }

    /**
     * 8.Store player details
     */

    @Test
    void storePlayerDetails() {
        String name = "Jack";
        Player player = new Player(name);
        Game game = new Game(player);
        Cryptogram cryptogram = game.generateCryptogram(false, "SUNNY");
        game.enterLetter(cryptogram.getEncrypted()[0], 'S');
        game.enterLetter(cryptogram.getEncrypted()[1], 'Y');
        game.enterLetter(cryptogram.getEncrypted()[2], 'N');

        int guesses = player.getNumGuesses();
        int correctGuesses = player.getCorrectGuesses();
        int cryptogramsPlayed = player.getCryptogramsPlayed();
        int cryptogramsCompleted = player.getCryptogramsCompleted();

        File file = new File("player_test.txt");
        ArrayList<String[]> progress = new ArrayList<>();
        String[] current = new String[5];
        game.savePlayerDetails("player_test.txt");
        Scanner fileInput = null;
        try {
            fileInput = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
            fail();
        }

        if (fileInput != null) {
            while (fileInput.hasNext()) {
                for (int i = 0; i < 5 && fileInput.hasNext(); i++) {
                    current[i] = fileInput.nextLine();
                }
                progress.add(current);
            }
        }
        for (String[] strings : progress) {
            if (strings[0].equals(name)) {
                current = strings;

            }
        }
        assertEquals(name, current[0]);
        assertEquals(guesses, Integer.parseInt(current[1]));
        assertEquals(correctGuesses, Integer.parseInt(current[2]));
        assertEquals(cryptogramsPlayed, Integer.parseInt(current[3]));
        assertEquals(cryptogramsCompleted, Integer.parseInt(current[4]));
        fileInput.close();
        file.delete();
    }

    /**
     * 9.Successfully completed cryptograms
     */
    @Test
    void successfullyCompletedCryptogram() {
        Player player = new Player("Jack");
        Game game = new Game(player);
        int complete = player.getCryptogramsCompleted();
        Cryptogram cryptogram = game.generateCryptogram(false, "BANANA");
        game.enterLetter(cryptogram.getEncrypted()[1], 'A');
        game.enterLetter(cryptogram.getEncrypted()[0], 'B');
        game.enterLetter(cryptogram.getEncrypted()[4], 'N');
        assertEquals(complete + 1, player.getCryptogramsCompleted());
        assertTrue(console.toString().contains("Total games completed: "));
    }

    @Test
    void unsuccessfullyCompleted() {
        Player player = new Player("George");
        Game game = new Game(player);
        Cryptogram cryptogram = game.generateCryptogram(false, "YO");
        game.enterLetter(cryptogram.getEncrypted()[1], 'O');
        game.enterLetter(cryptogram.getEncrypted()[0], 'T');
        assertEquals("Sorry you've incorrectly completed the cryptogram.", console.toString().trim());
        game.undoLetter('T');
        assertEquals('_', cryptogram.getGuess()[0]);
    }

    /**
     * 10.Tracking number of cryptograms played
     */

    //the number of cryptograms played should be incremented after starting a new game
    @Test
    void newCryptogramPlayed() {
        Player player = new Player("George");
        int cryptogramsPlayed = player.getCryptogramsPlayed();
        Game game = new Game(player);
        Cryptogram cryptogram = game.generateCryptogram(false, "YO");
        player.incrementCryptogramsPlayed();
        game.enterLetter(cryptogram.getEncrypted()[1], 'O');
        assertEquals(cryptogramsPlayed + 1, player.getCryptogramsPlayed());
    }

    //loading a game shouldn't change the number of cryptograms played
    @Test
    void cryptogramLoaded() throws FileNotFoundException {
        Player player = new Player("John");
        Game game = new Game(player);
        Cryptogram cryptogram = game.generateCryptogram(false, "HELLO");
        player.incrementCryptogramsPlayed();
        game.enterLetter(cryptogram.getEncrypted()[0], 'G');
        int cryptogramsPlayed = player.getCryptogramsPlayed();
        game.saveGame("saves/John.txt");
        game.loadGame("saves/John.txt");
        new File("saves/John.txt").delete();
        
        assertEquals(cryptogramsPlayed, game.getCurrentPlayer().getCryptogramsPlayed());
    }

    /**
     * 11.Percentage calculation for accuracy
     */

    @Test
    void correctGuess() {
        Player player = new Player("Jack");
        Game game = new Game(player);
        Cryptogram cryptogram = game.generateCryptogram(false, "BANANA");
        game.enterLetter(cryptogram.getEncrypted()[1], 'A');
        assertEquals(1, player.getCorrectGuesses());
        assertEquals(1, player.getNumGuesses());
    }

    @Test
    void incorrectGuess() {
        Player player = new Player("Jack");
        Game game = new Game(player);
        Cryptogram cryptogram = game.generateCryptogram(false, "BANANA");
        game.enterLetter(cryptogram.getEncrypted()[1], 'Z');
        assertEquals(0, player.getCorrectGuesses());
        assertEquals(1, player.getNumGuesses());
    }

    @Test
    void testPercentage() {
        Player player = new Player("Jack");
        for (int i = 0; i < 5; ++i)
            player.incrementCorrectGuesses();
        for (int i = 0; i < 9; ++i)
            player.incrementNumGuesses();
        assertEquals(55.56, player.calculatePercentage());
    }
    
    /**
     * 13.Top ten scores
     */
    @Test
    void displayLeaderboard() {
    	//create a new file with 3 new players with different number of completed games
    	File file = new File("details_test.txt");
    	// 0 games completed
        Player player = new Player("John");
        Game game = new Game(player);
        Cryptogram cryptogram = game.generateCryptogram(false, "NO");
        game.enterLetter(cryptogram.getEncrypted()[1], 'Z');
        game.savePlayerDetails("details_test.txt");
        
        // 1 game completed
        player = new Player("Pete");
        Game game1 = new Game(player);
        game1.loadPlayerDetails("details_test.txt",player);
        cryptogram = game1.generateCryptogram(false, "NO");
        game1.enterLetter(cryptogram.getEncrypted()[1], 'O');
        game1.enterLetter(cryptogram.getEncrypted()[0], 'N');
        game1.savePlayerDetails("details_test.txt");
        
        // 2 games completed
        player = new Player("Mark");
        Game game2  = new Game(player);
        game2.loadPlayerDetails("details_test.txt",player);
        cryptogram = game2.generateCryptogram(false, "NO");
        game2.enterLetter(cryptogram.getEncrypted()[1], 'O');
        game2.enterLetter(cryptogram.getEncrypted()[0], 'N');
        cryptogram = game2.generateCryptogram(false, "Y");
        game2.enterLetter(cryptogram.getEncrypted()[0], 'Y');
        game2.savePlayerDetails("details_test.txt");
        
        game2.leaderboard("details_test.txt");
        
        // checks correct ordering of the leaderboard
        assertTrue(console.toString().contains("1: Mark - 2"));
        assertTrue(console.toString().contains("2: Pete - 1"));
        assertTrue(console.toString().contains("3: John - 0"));
        file.delete();
    }
    
    @Test
    void leaderboardFileMissing() {
    	//creates a new file but does not save any player details
    	File file = new File("details_test.txt");
        Player player = new Player("Gregory");
        Game game = new Game(player);
        game.loadPlayerDetails("details_test.txt",player);
        Cryptogram cryptogram = game.generateCryptogram(false, "NO");
        game.leaderboard("details_test.txt");
        assertTrue(console.toString().contains("Could not find the player details file, cannot display a leaderboard."));
    }
    
    @Test
    void leaderboardFileEmpty() {
    	//creates a new file but does not save any player details
    	File file = new File("details_test.txt");
    	try {
			if (file.createNewFile()) {
			    Player player = new Player("Gregory");
			    Game game = new Game(player);
			    game.loadPlayerDetails("details_test.txt",player);
			    Cryptogram cryptogram = game.generateCryptogram(false, "NO");
			    game.leaderboard("details_test.txt");
			    assertTrue(console.toString().contains("No saved player details, cannot display a leaderboard."));
			    file.delete();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * 14.Hint
     */

    @Test
    void notMappedLetter() {
        Game game = new Game(new Player("George"));
        Cryptogram cryptogram = game.generateCryptogram(false, "YES");
        game.hint();
        int i = 0;
        while (cryptogram.getGuess()[i] == '_') {
            i++;
        }
        assertEquals(cryptogram.getSolution().toCharArray()[i], cryptogram.getGuess()[i]);
    }

    @Test
    void mappedLetter() {
        Game game = new Game(new Player("George"));
        Cryptogram cryptogram = game.generateCryptogram(false, "YO");
        game.enterLetter(cryptogram.getEncrypted()[0], 'N');
        game.enterLetter(cryptogram.getEncrypted()[1], 'O');
        char guess = cryptogram.getGuess()[0];
        game.hint();

        Object[] lines = console.toString().trim().lines().toArray();
        assertEquals(cryptogram.getSolution().toCharArray()[0], cryptogram.getGuess()[0]);
        assertEquals("Letter " + guess + " was changed to " + cryptogram.getSolution().toCharArray()[0], lines[1]);
    }
    /**
     * Generating tests
     */
    @Test
    void loadPhrasesFound() {
        try {
            File testFile = new File("test_phrases.txt");
            FileOutputStream fos = new FileOutputStream("test_phrases.txt");
            fos.write("hello world".getBytes());
            fos.close();
            Game game = new Game(new Player("Jack"));
            assertNotEquals(game.loadPhrases("test_phrases.txt"), null);
            testFile.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    void loadPhrasesEmpty() {
        try {
            File testFile = new File("test_phrases.txt");
            if (testFile.createNewFile()) {
                Game game = new Game(new Player("Jack"));
                assertEquals(game.loadPhrases("test_phrases.txt"), null);
                testFile.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void loadPhrasesMissing() {
        Game game = new Game(new Player("Jack"));
        try {
            assertEquals(game.loadPhrases("test_phrases.txt"), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void generateCryptogramNumeric() {
        ArrayList<String> glyphs = new ArrayList<String>();
        boolean pass = true;
        Game game = new Game(new Player("Jack"));
        Cryptogram nc = game.generateCryptogram(true, "hello world");
        for (String glyph : nc.getGlyphs()) {
            if (glyphs.contains(glyph)) {
                pass = false;
                break;
            }
            try {
                int n = Integer.parseInt(glyph);
                if (n < 1 || n > 26) {
                    pass = false;
                    break;
                }
            } catch (NumberFormatException e) {
                pass = false;
                break;
            }
            glyphs.add(glyph);
        }
        assertEquals(pass, true);
    }

    @Test
    void generateCryptogramAlphabetic() {
        ArrayList<String> glyphs = new ArrayList<String>();
        boolean pass = true;
        Game game = new Game(new Player("Jack"));
        Cryptogram nc = game.generateCryptogram(false, "hello world");
        for (String glyph : nc.getGlyphs()) {
            if (glyphs.contains(glyph)) {
                pass = false;
                break;
            }
            if (glyph.length() != 1 || glyph.charAt(0) < 'A' || glyph.charAt(0) > 'Z') {
                pass = false;
                break;
            }
            glyphs.add(glyph);
        }
        assertEquals(pass, true);
    }

    /**
     * Enter letter tests
     */
    @Test
    void statsUpdatedMapCheck() {
        Player testPlayer = new Player("Jack");
        Game game = new Game(testPlayer);
        Cryptogram cryptogram = game.generateCryptogram(false, "HELLO");
        game.enterLetter(cryptogram.getEncrypted()[1], 'G');
        assertEquals(cryptogram.getGuess()[1], 'G');
        game.enterLetter(cryptogram.getEncrypted()[0], 'H');
        assertEquals(cryptogram.getGuess()[0], 'H');
        assertEquals(testPlayer.getNumGuesses(), 2);
        assertEquals(testPlayer.getCorrectGuesses(), 1);
    }

    @Test
    void glyphAlreadyMappedTest() {
        Player testPlayer = new Player("Bobby");
        Game game = new Game(testPlayer);
        Cryptogram cryptogram = game.generateCryptogram(false, "HELLO");
        game.enterLetter(cryptogram.getEncrypted()[0], 'G');
        InputStream inBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("N\n".getBytes());
        System.setIn(in);
        game.input = new Scanner(System.in);
        game.enterLetter(cryptogram.getEncrypted()[0], 'H');
        System.setIn(inBackup);
        assertEquals(cryptogram.getGuess()[0], 'G');

        in = new ByteArrayInputStream("Y\n".getBytes());
        System.setIn(in);
        game.input = new Scanner(System.in);
        game.enterLetter(cryptogram.getEncrypted()[0], 'H');
        System.setIn(inBackup);
        assertEquals(cryptogram.getGuess()[0], 'H');

    }

    @Test
    void plainLetterAlreadyMapped() {
        Game game = new Game(new Player("Henry"));
        Cryptogram cryptogram = game.generateCryptogram(false, "HELLO");
        game.enterLetter(cryptogram.getEncrypted()[1], 'G');
        game.enterLetter(cryptogram.getEncrypted()[0], 'G');
        assertEquals(console.toString().trim(), "This letter has already been mapped to a glyph.");
    }

   @Test
    void successfullyCompleted() {
        Player testPlayer = new Player("Harriet");
        Game game = new Game(testPlayer);
        Cryptogram cryptogram = game.generateCryptogram(false, "YO");
        testPlayer.incrementCryptogramsPlayed();
        game.enterLetter(cryptogram.getEncrypted()[1], 'O');
        game.enterLetter(cryptogram.getEncrypted()[0], 'Y');
        assertTrue(console.toString().contains("Total games completed: "));
        assertEquals(1, testPlayer.getCryptogramsPlayed());
        assertEquals(1, testPlayer.getCryptogramsCompleted());
    }

    @Test
    void lastValueUnsuccessfullyMapped() {
        Player testPlayer = new Player("George");
        Game game = new Game(testPlayer);
        Cryptogram cryptogram = game.generateCryptogram(false, "YO");
        testPlayer.incrementCryptogramsPlayed();
        game.enterLetter(cryptogram.getEncrypted()[1], 'O');
        game.enterLetter(cryptogram.getEncrypted()[0], 'T');
        assertEquals(console.toString().trim(), "Sorry you've incorrectly completed the cryptogram.");
        assertEquals(1, testPlayer.getCryptogramsPlayed());
        assertEquals(testPlayer.getCryptogramsCompleted(), 0);
    }

    /**
     * Undo tests
     */

    @Test
    void undoLetter() {
        Game game = new Game(new Player("Jack"));
        Cryptogram cryptogram = game.generateCryptogram(false, "BANANA");
        game.enterLetter(cryptogram.getEncrypted()[1], 'K');
        game.enterLetter(cryptogram.getEncrypted()[0], 'L');
        game.undoLetter('L');
        assertEquals(cryptogram.getGuess()[0], '_');
    }

    @Test
    void undoNonExistentLetter() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String expectedOutput = " The letter has not been mapped yet!";
        Game game = new Game(new Player("Jack"));
        Cryptogram cryptogram = game.generateCryptogram(false, "BANANA");
        game.enterLetter(cryptogram.getEncrypted()[1], 'K');
        game.enterLetter(cryptogram.getEncrypted()[0], 'L');
        game.undoLetter('B');
        assertEquals(expectedOutput.replaceAll("\\s+", ""), outContent.toString().replaceAll("\\s+", ""));
    }
    
    @Test
    void printFrequenciesTest() {
    	Game game = new Game(new Player("Jack"));
        Cryptogram cryptogram = game.generateCryptogram(false, "BANANA");
        game.printFrequencies();
        assertTrue(console.toString().contains(cryptogram.getEncrypted()[0] + ": 16.67% | " + cryptogram.getEncrypted()[1] + ": 50.00% | " + cryptogram.getEncrypted()[2] + ": 33.33% | "));
    }

}
