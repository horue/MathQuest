<img src="Assets\Images\mathquestlogo2.png" alt="project logo" width="300">

[Read in Portuguese](readme-pt.md)

## Overview

**Math Quest** is an interactive math game developed in Java using the Swing library. The objective is to solve math problems (addition, subtraction, multiplication, and division) that appear on the screen within a time limit. The game dynamically updates the score based on the player's responses, and sounds play for correct and incorrect answers. Players can view their scores on a leaderboard and play again after the game ends.

## Features

1.  **Math Problem Gameplay:**
    
    -   Players must solve randomly generated math problems.
    -   Correct answers to addition and subtraction give 1 point, while multiplication and division give 3 points.
    -   The player’s score and remaining time are displayed and updated in real-time.
2.  **Leaderboard:**
    
    -   The game tracks player scores and maintains a leaderboard stored in a file (`placar.txt`) and a global leaderboard hosted on Firebase.
    -   Players can view the top scores via the "View Score" button.
3.  **Sounds and Music:**
    
    -   Background music plays during gameplay, with different music tracks for the game, end screen, and background.
    -   Sound effects for correct answers, incorrect answers, and game over.
4.  **Custom Fonts:**
    
    -   The game uses custom fonts for both the game interface and numbers.
5.  **Graphical User Interface (GUI):**
    
    -   The game utilizes a Java Swing-based interface.
    -   The math problem, score, and time are presented in a clean, intuitive interface.
    -   A logo is displayed at the start of the game.
6.  **User Input:**
    
    -   The player enters the answer to the math problem via a text input field.
    -   Input is cleared automatically after submission.
7.  **Restart and Exit Options:**
    
    -   When the game ends, the player can choose to play again or exit.
    -   The player must input their name to save their score to the leaderboard.
8.  **Timer Mechanics:**
    
    -   Players start with 35 seconds. Time increases when correct answers are provided and decreases for wrong answers.

## Setup Instructions

### Prerequisites

-   Java Development Kit (JDK) 8 or higher.
-   A text editor or an Integrated Development Environment (IDE) like IntelliJ IDEA, Eclipse, or NetBeans.
-   Required libraries and assets: custom fonts, sound effects, and music files must be placed in the appropriate directories.


### Steps to Run the Game

1.  **Compile and Run:**
    - Clone the github repository using `git clone https://github.com/horue/MathQuest.git`
    -   Compile the Java files using an IDE or command line.
    -   Run the `Jogo` class's `main` method to start the game.
2.  **Fonts and Assets:**
    
    -   Ensure that the fonts and sound files are correctly placed in their respective directories (`Font` for fonts and `Assets` for images and audio).
    - Music composed by [Bert Cole](https://bitbybitsound.com/).
3.  **Gameplay:**
    
    -   Press "Play" to start the game.
    -   Enter the result of the math problem in the text field and press "Enter" to submit.
    -   The game ends when time runs out, and the player is prompted to save their score.

### Controls

-   **Play Button:** Starts the game.
-   **View Score Button:** Displays the current leaderboard.
-   **Text Input:** Type your answer to the math problem here and press "Enter."

### Saving Scores

Scores are saved automatically to `placar.txt` after each game. Players can view their ranking based on their scores.

## Customization

1.  **Change Fonts:**
    
    -   Custom fonts can be changed by replacing the `.ttf` files in the `Font` folder.
2.  **Change Sound Effects:**
    
    -   The sound effects and background music are located in the `Assets` folder and can be replaced by other `.wav` files.
3.  **Time Modifications:**
    
    -   Adjust the starting time or the time gained/lost on correct/incorrect answers in the `responder()` method of the `Jogador` class.

## Known Issues

-   The game currently only supports single-player mode.
- The cursors wont select the answer input automatically.
-   Ensure that the music and sound files are in `.wav` format for compatibility with the `AudioSystem`.

## Future Improvements

-   Add more complex math problems (exponents, square roots, etc.).
-   Introduce multiple difficulty levels.
-   Add multiplayer functionality or challenge mode.
- Add a story mode.

## License

This project is open-source and free to use under the MIT License.