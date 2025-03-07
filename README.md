# Cryptogram Game

## Overview

Welcome to the Cryptogram Game! In this game, players decrypt messages by replacing encrypted symbols with corresponding letters. The game features two types of cryptograms: alphabetic (letters) and numeric (numbers). Players can track their progress, save their game, and continue from where they left off.

The game is implemented in Java with a console-based interface. Players interact with the cryptogram puzzles by guessing letters, and their progress is saved and managed through player stats such as guesses made and correct guesses.

---

## Features

- **Start a New Game**: Players can start a fresh cryptogram game and choose between alphabetic or numeric encryption.
- **Load Saved Progress**: Players can load their saved game progress and continue from where they last played.
- **Interactive Cryptogram Gameplay**: Players are shown a cryptogram and must guess letters to decrypt it.
- **Player Stats**: Player stats are tracked, including the number of cryptograms played and completed, as well as the total number of guesses and correct guesses.
- **Help and Input Validation**: Players can input their guesses, receive help commands, and get validation for their inputs.

---

## Technologies Used

- **Java**: The game is developed in Java, using object-oriented principles.
- **File Handling**: Saves player progress using text files (`player_details.txt` and `saves/` directory).
- **Console-Based Input/Output**: Players interact with the game through the command line interface, entering guesses and receiving feedback.

---

## Getting Started

### Prerequisites

Ensure you have the following installed:

- **Java 8 or higher**: To compile and run the game.
- **A terminal or command prompt**: To interact with the game via text.

### Setup

1. **Clone the Repository**: 
   If the code is hosted on a platform like GitHub, clone it to your local machine:
   ```bash
   git clone <repository_url>
   cd cryptogram-game
   ```

2. **Compile the Game**: 
   Ensure you have Java installed and set up, then compile the game:
   ```bash
   javac Driver.java
   ```

3. **Run the Game**:
   After compiling, you can run the game:
   ```bash
   java Driver
   ```

---

## Game Flow

### Starting a New Game

When you start the game, you will be prompted to enter your username. After that, you can choose whether you want to continue from a saved cryptogram or start a new one. If you opt to start fresh, you can choose between an alphabetic or numeric cryptogram.

### Gameplay

Once a cryptogram is generated, you will see the puzzle with encrypted glyphs. Your task is to guess the letters that correspond to the glyphs:

1. **Make a Guess**: You will be asked which glyph to replace, and you'll input a guess for the letter.
2. **Check Validity**: The game checks if the guess is valid (e.g., single alphabetical characters only).
3. **Continue the Puzzle**: If the cryptogram is solved, you can either move to the next puzzle or exit the game.

### Player Stats

The game tracks your progress, including:

- **Cryptograms Played**: Total number of cryptograms you’ve attempted.
- **Cryptograms Completed**: Total number of cryptograms you've solved.
- **Guesses Made**: Total number of guesses you've made.
- **Correct Guesses**: Number of correct guesses.

These stats allow you to track your progress over time. Additionally, you can calculate your guess accuracy with the `calculatePercentage` method, which gives you the percentage of correct guesses.

### Saved Progress

If you've previously played the game and saved your progress, the game will ask if you'd like to continue. If you choose to continue, your previous cryptogram will be loaded, and you can pick up where you left off.

---

## Game Classes

The game consists of the following main classes:

### 1. **Driver Class** (Main Program)
The `Driver` class manages the overall flow of the game, including initializing the game, interacting with the user, and handling input/output operations. The player can start a new game, load saved progress, and make guesses through this class.

### 2. **Player Class**
The `Player` class manages player-specific information such as username, cryptogram progress, and stats. It includes methods to track cryptograms played/completed, guesses made, and correct guesses.

Key Methods:
- **`incrementCryptogramsPlayed`**: Increases the number of cryptograms the player has attempted.
- **`incrementCryptogramsCompleted`**: Increases the number of cryptograms the player has solved.
- **`incrementNumGuesses`**: Increases the total number of guesses made.
- **`incrementCorrectGuesses`**: Increases the number of correct guesses.
- **`calculatePercentage`**: Calculates and returns the percentage of correct guesses out of total guesses.

### 3. **Game Class**
The `Game` class handles the logic of the cryptogram game, including generating cryptograms, printing them to the console, processing player input, and saving/loading game progress.

### 4. **Cryptogram Class**
The `Cryptogram` class represents a single cryptogram puzzle. It tracks the encrypted message and checks if the puzzle has been completed.

---

## Example Game Session

Here’s a sample of what a typical game session looks like:

```
Cryptogram Game Ver.0.0.0
Please enter your name
John
Loading player details...
You have some previously saved progress on a cryptogram
Would you like to continue playing it? [Y / N]
Y
Loading saved progress...
--- Game begins ---
Your current cryptogram:
X L E G I I A Y S L M

Which glyph do you wish to replace? (enter "help" for a list of additional commands)
L
Which character would you like to replace it with?
E
Current cryptogram:
X E E G I I A Y S L M
...
Do you want to play the next cryptogram? [Y / N]
Y
Starting a new cryptogram...
```

---

## Example Player Class Usage

Here’s how the `Player` class tracks player stats:

```java
Player player = new Player("John");
player.incrementCryptogramsPlayed();
player.incrementCryptogramsCompleted();
player.incrementNumGuesses();
player.incrementCorrectGuesses();

System.out.println("Cryptograms Played: " + player.getCryptogramsPlayed());
System.out.println("Cryptograms Completed: " + player.getCryptogramsCompleted());
System.out.println("Correct Guesses: " + player.getCorrectGuesses());
System.out.println("Guess Accuracy: " + player.calculatePercentage() + "%");
```

Output:
```
Cryptograms Played: 1
Cryptograms Completed: 1
Correct Guesses: 5
Guess Accuracy: 83.33%
```

---

## Error Handling

- **Invalid Input**: The game ensures that players provide valid inputs, such as single characters for guesses. Invalid input is handled with error messages guiding the user on how to proceed.
- **File Handling Errors**: If files such as saved progress or player details are not found, the game gracefully handles errors and displays an appropriate message.
