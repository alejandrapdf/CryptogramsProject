import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {

	private Cryptogram currentCryptogram;
	private final Player currentPlayer;
	public Scanner input = new Scanner(System.in);
	private ArrayList<String[]> details = new ArrayList<>();

	public Game(Player player) {
		currentPlayer = player;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public Cryptogram getCurrentCryptogram() {
		return currentCryptogram;
	}

	/*
	 * For marking
	 */
	public void printGeneratingInfo() {
		System.out.println("Solution: " + currentCryptogram.getSolution());
		for (int i = 0; i < 26; i++) {
			System.out.print((char) ('A' + i) + "\t");
		}
		System.out.print("\n");
		for (int i = 0; i < 26; i++) {
			System.out.print(currentCryptogram.getGlyphs()[i] + "\t");
		}
		System.out.print("\n");
	}

	/**
	 * Prints cryptogram with encrypted sentence and current status of the guess.
	 */
	public void printCryptogram() {
		System.out.println("=".repeat(150));
		System.out.print("Guess:\t");
		for (char c : currentCryptogram.getGuess()) {
			System.out.print(" " + c);
		}
		System.out.print("\n\nGlyphs:\t");
		for (String s : currentCryptogram.getEncrypted()) {
			System.out.print(" " + s);
		}
		System.out.print("\n\n");
	}

	/**
	 * Processes user's input if he doesn't want to replace any glyphs
	 *
	 * @param in user input
	 */
	public void processInput(String in) {
		switch (in.toLowerCase()) {
		case "exit":
			exit();
			break;
			
		case "hint":

                hint();
                break;

		case "help":
			System.out.println("Commands:");
			System.out.println("Undo - prompts for a letter to clear in the guess");
			System.out.println("Hint - decrypts one of glyphs");
			System.out.println("Frequency - displays the relative frequency of each letter (percentage of solution's length)");
			System.out.println("Save - saves current cryptogram");
			System.out.println("Load - loads a saved cryptogram");
			System.out.println("Solution - displays solution");
			System.out.println("Leaderboard - displays top 10 players by cryptograms completed");
			break;


		case "frequency":
			printFrequencies();
			break;

		case "undo":
			System.out.println("Which letter would you like to undo?");
			char letter = input.next().charAt(0);
			undoLetter(letter);
			break;

		case "load":
			System.out.println("Any unsaved progress will be lost");
			System.out.println("Do you still wish to load? [Y / N]");
			if (input.next().equalsIgnoreCase("Y")) {
				loadGame("saves/" + currentPlayer.getName() + ".txt");
			}
			break;

		case "save":
			String filename = "saves/" + currentPlayer.getName() + ".txt";
			File saveFile = new File(filename);
			if (saveFile.exists()) {
				System.out.println("You have previously saved progress");
				System.out.println("Would you like to overwrite it? [Y / N]");
				if (!input.next().toUpperCase().equals("Y")) {
					break;
				}
			}
			try {
				saveGame(filename);
			} catch (FileNotFoundException e) {
				System.out.println("File not found");
			} catch (NumberFormatException e) {
				System.out.println("Corrupt data in the file.");
				return;
			}
			break;

		case "solution":
			showSolution();
			break;

		case "leaderboard":
			leaderboard("player_details.txt");
			break;

		default:
			System.out.println("Please enter a valid glyph or command.");
			break;

		}
	}

	public List<String> loadPhrases(String filename) throws FileNotFoundException {
		List<String> phrases = new ArrayList<>();
		File f = new File(filename);
		Scanner fileInput;
		try {
			fileInput = new Scanner(f);
			if (fileInput.hasNext()) {
				phrases.add(fileInput.nextLine());
				while (fileInput.hasNext()) {
					phrases.add(fileInput.nextLine());
				}
				fileInput.close();
				return phrases;
			} else {
				fileInput.close();
				System.out.println("Phrases file was found but it is empty. Exiting...");
			}
		} catch (FileNotFoundException e) {
			System.out.println("Phrases file missing. Exiting...");
		}
		return null;
	}

	public Cryptogram generateCryptogram(boolean numeric, String phrase) {
		return currentCryptogram = new Cryptogram(numeric, phrase);
	}

	public void enterLetter(String glyph, char character) {
		char[] guess = currentCryptogram.getGuess();
		boolean check = false;
		boolean seen = false;
		ArrayList<Integer> positions = new ArrayList<>();
		for (int i = 0; i < currentCryptogram.getLength(); i++) {
			// checks to see if the encrypted letter they are trying to replace exists
			if (currentCryptogram.getEncrypted()[i].equals(glyph.toUpperCase())) {
				// if there is already a guess there, ask if they would like to overwrite it
				if (guess[i] != '_' && !seen) {
					System.out.println("This glyph has already been mapped");
					System.out.println("Would you like to overwrite it? [Y / N]");
					String inp = input.next();
					inp = inp.toUpperCase();
					seen = true;
					// if user does not want to overwrite the the letter they will be prompted to
					// enter another letter
					if (inp.equals("N")) {
						return;
					}
				}
				check = true;
				positions.add(i);
			}
		}
		// if the encrypted letter does exist...
		if (check) {
			// checks if the character they have entered has already been mapped somewhere
			for (char g : guess) {
				if (character == g) {
					System.out.println("This letter has already been mapped to a glyph.");
					return;
				}
			}
			// updates player stats
			if (currentCryptogram.getSolution().charAt(positions.get(0)) == (character)) {
				currentPlayer.incrementCorrectGuesses();
			}
			currentPlayer.incrementNumGuesses();
			// updates guess
			for (int index : positions) {
				guess[index] = character;
			}
			currentCryptogram.setGuess(guess);
			// checks guess to see if it is full
			checkGuess();
		} else {
			System.out.println("Please enter one of the glyphs shown.");
		}
		// can have system.out asking with which letter user wants to replace this glyph
	}

	/**
	 * Undoes letter from current guess.
	 *
	 * @param letter that will be removed from current guess
	 */
	public void undoLetter(char letter) {
		char[] guess = currentCryptogram.getGuess();
		int changes = 0;
		for (int i = 0; i < guess.length; i++) {
			if (guess[i] == Character.toUpperCase(letter)) {
				guess[i] = '_';
				changes += 1;
			}
		}
		if (changes != 0)
			currentCryptogram.setGuess(guess);
		else
			System.out.println("The letter has not been mapped yet!");
	}
/**
     * Gives a hint to a user
     */
    public void hint() {
        Random rand = new Random();
        char[] solution = currentCryptogram.getSolution().toCharArray();
        int length = solution.length;
        while (true) {
            int random = rand.nextInt(length);
            if (solution[random] != ' ' && solution[random] != currentCryptogram.getGuess()[random]) {
                char[] guess = currentCryptogram.getGuess();
                if (guess[random] != '_') {
                    System.out.println("Letter " + guess[random] + " was changed to " + solution[random]);
                }
                guess[random] = solution[random];
                currentCryptogram.setGuess(guess);
                checkGuess();
                break;
            }
        }
    }

	public void checkGuess() {
		boolean finished = true;
		char[] guess = currentCryptogram.getGuess();
		// checks for spaces in guess
		for (char letter : guess) {
			if (letter == '_') {
				finished = false;
				break;
			}
		}
		// if there are no spaces in guess...
		if (finished) {
			// check if the guess is correct and display appropriate methods
			if (currentCryptogram.completed()) {
				printCryptogram();
				currentPlayer.incrementCryptogramsCompleted();
				System.out.println("Well Done!!");
				System.out.println("Total guesses: " + currentPlayer.getNumGuesses());
				System.out.println("Correct guesses: " + currentPlayer.getCorrectGuesses());
				System.out.println("Percentage of correct guesses: " + currentPlayer.calculatePercentage());
				System.out.println("Total games played: " + currentPlayer.getCryptogramsPlayed());
				System.out.println("Total games completed: " + currentPlayer.getCryptogramsCompleted());
				printGeneratingInfo();
				if (currentPlayer.getCryptogramsCompleted() > 14) {
					System.out.println("Congratulations! You have solved all cryptograms!");
					exit();
				}
			} else {
				System.out.println("Sorry you've incorrectly completed the cryptogram.");
			}
		}
	}

	public void saveGame(String filename) throws FileNotFoundException {
		// tries to open the progress.txt. file
		try {
			// writes into the file, overwriting previous contents
			FileWriter writer = new FileWriter(filename);
			writer.write((currentCryptogram.getType() ? 'n' : 'a') + "\r\n");
			writer.write(currentCryptogram.getSolution() + "\r\n");
			for (String glyph : currentCryptogram.getEncrypted()) {
				writer.write(glyph + ",");
			}
			writer.write("\r\n" + new String(currentCryptogram.getGuess()));
			writer.close();
			System.out.println("Game saved.");
		}
		// If file can't be found, creates a new file
		catch (FileNotFoundException a) {
			File progressFile = new File(filename);
			try {
				if (progressFile.createNewFile())
					saveGame(filename);
			}
			// if fails in creation display error
			catch (IOException e) {
				System.out.println("Error creating new file.");
			}
		}

		// If finds file but fails to save display error
		catch (IOException e) {
			System.out.println("Error saving to file.");
		}
	}

	public void loadGame(String filename) {
		File progressFile = new File(filename);
		String[] save = new String[4];
		boolean dataCorrupt = false;
		try {
			Scanner reader = new Scanner(progressFile);
			while (reader.hasNext()) {
				for (int i = 0; i < 4; i++) {
					if (reader.hasNext()) {
						save[i] = reader.nextLine();
					} else {
						System.out.println("Corrupt data in the save file, could not load.");
						reader.close();
						return;
					}
				}
			}
			String[] encrypted = save[2].split(",");
			if (save[1].length() != encrypted.length || save[1].length() != save[3].length()) {
				dataCorrupt = true;
			}
			if (save[0].equals("a")) {
				for (String glyph : encrypted) {
					if (glyph.length() != 1 || (!glyph.equals(" ") && !Character.isUpperCase(glyph.toCharArray()[0]))) {
						dataCorrupt = true;
					}
				}
			} else if (save[0].equals("n")) {
				for (String glyph : encrypted) {
					if (!glyph.equals(" ")) {
						try {
							int numericGlyph = Integer.parseInt(glyph);
							if (numericGlyph < 1 || numericGlyph > 26) {
								dataCorrupt = true;
							}
						} catch (NumberFormatException e) {
							dataCorrupt = true;
						}
					}
				}
			} else {
				dataCorrupt = true;
			}

			if (dataCorrupt) {
				System.out.println("Corrupt data in the save file, could not load.");
				reader.close();
				return;
			}

			currentCryptogram = new Cryptogram(save[0].equals("n") ? true : false, save[1]);
			currentCryptogram.setEncrypted(encrypted);
			currentCryptogram.setGuess(save[3].toCharArray());
			reader.close();
		} // If file cannot be found display error
		catch (FileNotFoundException e) {
			System.out.println("Could not find the save file.");
		} catch (NumberFormatException e) {
			System.out.println("Corrupt data in the save file.");
		}
	}

	/**
	 * Saves player details
	 *
	 * @param filename filename to which details are saved
	 */
	public void savePlayerDetails(String filename) {
		boolean playerFound = false;
		String[] playerDetails = new String[] { currentPlayer.getName(), String.valueOf(currentPlayer.getNumGuesses()),
				String.valueOf(currentPlayer.getCorrectGuesses()), String.valueOf(currentPlayer.getCryptogramsPlayed()),
				String.valueOf(currentPlayer.getCryptogramsCompleted()) };
		for (String[] pd : details) {
			if (pd[0].equals(currentPlayer.getName())) {
				pd[1] = String.valueOf(currentPlayer.getNumGuesses());
				pd[2] = String.valueOf(currentPlayer.getCorrectGuesses());
				pd[3] = String.valueOf(currentPlayer.getCryptogramsPlayed());
				pd[4] = String.valueOf(currentPlayer.getCryptogramsCompleted());
				playerFound = true;
				break;
			}
		}

		if (!playerFound) {
			details.add(playerDetails);
		}

		try {
			FileWriter writer = new FileWriter(filename);
			for (String[] strings : details) {
				for (int i = 0; i < 5; i++) {
					writer.write(strings[i] + "\r\n");
				}
			}
			writer.close();
			System.out.println("Player details saved.");
		} catch (IOException e) {
			System.out.println("Error saving to file.");
		}
	}

	/**
	 * loads player details from a file and adds details to an array
	 *
	 * @param filename from which details are loaded
	 */
	public void loadPlayerDetails(String filename, Player player) {

		try {
			File file = new File(filename);
			Scanner fileInput = new Scanner(file);
			String[] current;
			details = new ArrayList<>();
			while (fileInput.hasNext()) {
				current = new String[5];
				for (int i = 0; i < 5; i++) {
					if (fileInput.hasNext()) {
						current[i] = fileInput.nextLine();
						if (current[i] == null)
							current[i] = "0";
					} else {
						System.out.println("Corrupt data in the player details file, could not load.");
						fileInput.close();
						return;
					}
				}

				if (current[0].equals(player.getName())) {
					player.setNumGuesses(Integer.parseInt(current[1]));
					player.setCorrectGuesses(Integer.parseInt(current[2]));
					player.setCryptogramsPlayed(Integer.parseInt(current[3]));
					player.setCryptogramsCompleted(Integer.parseInt(current[4]));
				}
				details.add(current);
			}
			fileInput.close();

		} catch (FileNotFoundException e) {
			System.out.println("Could not the player details file. A new file will be created when the game is saved.");
		}
	}

	public void showSolution() {
		if (!currentCryptogram.completed()) {
			System.out.println("=".repeat(150));
			System.out.print("Solution:");
			for (int i = 0; i < currentCryptogram.getSolution().length(); i++) {
				System.out.print(" " + currentCryptogram.getSolution().charAt(i));
				currentCryptogram.setGuess(currentCryptogram.getSolution().toCharArray());
			}
			System.out.print("\n\nGlyphs: \t");
			for (String s : currentCryptogram.getEncrypted()) {
				System.out.print(" " + s);
			}
			System.out.println("\n");
		}

	}

	public void leaderboard(String filename) {
		ArrayList<String[]> temp = new ArrayList<>();
		try {
			File file = new File(filename);
			Scanner fileInput = new Scanner(file);
			String[] current;

			// read in file data for player details and add to current
			while (fileInput.hasNext()) {
				current = new String[5];
				for (int i = 0; i < 5; i++) {
					if (fileInput.hasNext()) {
						current[i] = fileInput.nextLine();
						if (i > 0 && current[i].charAt(0) < '0' && current[i].charAt(0) > '9') {
							System.out.println("Corrupt data in file, could not display a leaderboard.");
							fileInput.close();
							return;
						}
						if (current[i] == null)
							current[i] = "0";
					} else {
						System.out.println("Corrupt data in file, could not display a leaderboard.");

						fileInput.close();
						return;
					}
				}
				temp.add(current);

			}
			fileInput.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not find the player details file, cannot display a leaderboard.");
			return;
		} catch (NumberFormatException e) {
			System.out.println("Corrupt data in the player details file, cannot display a leaderboard.");
			return;
		}

		// checks if file contents are empty and if so display message
		if (temp.size() == 0) {
			System.out.println("No saved player details, cannot display a leaderboard.");
			return;
		}

		// sort list of players by descending order of games successfully completed
		ArrayList<String[]> sorted = new ArrayList<>();
		String[] highest;

		int highestIndex = 0;

		// Continues looping until file is empty and has added all remaining player
		// stats in sorted order to new array
		while (temp.size() != 0) {
			highestIndex = 0;
			highest = temp.get(0);
			for (int j = 1; j < temp.size(); j++) {
				if (Integer.valueOf(highest[4]) < Integer.valueOf(temp.get(j)[4])) {
					highest = temp.get(j);
					highestIndex = j;
				}
			}
			sorted.add(highest);
			temp.remove(highestIndex);
		}

		// Prints layout for leader board, numbering of the list followed by name with
		// corresponding score
		System.out.println("Leaderboard:");
		System.out.println("Name - Score:");
		for (int i = 0; i < sorted.size(); i++) {
			System.out.println((i + 1) + ": " + sorted.get(i)[0] + " - " + sorted.get(i)[4]);
		}
		for (int i = 0; i < 10 - sorted.size(); i++) {
			System.out.println((i + 1 + sorted.size()) + ":");
		}
	}

	public void printFrequencies() {
		String solution = currentCryptogram.getSolution();
		int numberOfLetters = 0;
		int[] frequencyValues = new int[solution.length()];
		int[] frequencyMap = new int[solution.length()];
		Arrays.fill(frequencyMap, -1);
		for(int i = 0; i < solution.length(); i++) {
			char c = solution.charAt(i);
			int frequency = 1;
			if(Character.isLetter(c)) {
				numberOfLetters++;
			}
			if (frequencyMap[i] == -1) {
				frequencyMap[i] = i;
				for (int j = i + 1; j < solution.length(); j++) {
					if (solution.charAt(j) == c) {
						frequency++;
						frequencyMap[j] = i;
					}
				}
				frequencyValues[i] = frequency;
			}
		}
		
		System.out.println("\nRelative frequencies of glyphs in the encrypted solution:");
		for (int i = 0; i < solution.length(); i++) {
			if(Character.isLetter(solution.charAt(i)) && frequencyMap[i] == i) {
				System.out.printf(currentCryptogram.getEncrypted()[i] + ": %.2f%s", (float) frequencyValues[frequencyMap[i]] * 100 / numberOfLetters, "% | ");
			}
		}
		
		System.out.println("\nAverage relative frequencies in English, vased on a sample of 40,000 words:");
		System.out.println("A: 8.12% | B: 1.49% | C: 2.71% | D: 4.32% | E: 12.02% | F: 2.30% | G: 2.03% | H: 5.92% | I: 7.31% | J: 0.10% | K: 0.69% | L: 3.98% | M: 2.61% |");
		System.out.println("N: 6.95% | O: 7.68% | P: 1.82% | Q: 0.11% | R: 6.02% | S: 6.28% | T: 9.10% | U: 2.88% | V: 1.11% | W: 2.09% | X: 0.17% | Y: 2.11% | Z: 0.07% |");
	}

	public void exit() {
		savePlayerDetails("player_details.txt");
		System.exit(0);
	}
}
