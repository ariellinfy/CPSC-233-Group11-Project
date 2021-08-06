# Gomoku Game

## About
This game revolves around a turn-based two player format, where each player takes turns in placing a single black/white stone on an empty intersection within a grid board. After these stones are placed, they can no longer be moved. The main objective is to create a horizontal, vertical, or diagonal line of exactly five stones before your opponent. A row with six or more counts as a draw. 

The current application will prompt the user several setting options before starting the game:
- To play against a human player or computer - medium level computer by default
- Difficulty level of the computer player (if computer is chosen in #1) - medium by default
- To play as black or white stone (black always goes first) - user goes first by default
- Set the board size (9/13/15/19) - 15x15 by default

A class diagram is included with the source code folder (public/gomoku-class-diagram.png).

## Text-Based Application
### For Eclipse User
1. The text-based version of this application is found under iteration-1 branch.
2. When cloning the repository to Eclipse, on the Local Destination page: set initial branch to iteration-1.
3. Click on the project folder (CPSC_233_Group11_Project) in Package Explorer, then run the game by clicking on the green run icon in the tool bar.
4. Follow instructions to play the game and have fun!

### For All Other Editors (make sure a Java SDK is installed on your device)
1. Clone or download the repository files to a folder in your device.
2. In a terminal, navigate to the  project src folder.
3. Compile files using command line: javac application/GomokuText.java
4. Run game using command line: java application.GomokuText
5. Follow instructions to play the game and have fun!

## Graphical User Interface
1. The GUI version of this application is found under iteration-2 branch.
2. When cloning the repository to Eclipse, on the Local Destination page: set initial branch to iteration-2.
3. Add external libraries to the project module path: JavaFX SDK and User Library (JavaFX).
4. Navigate to application.GomokuGUI in the project src folder and run as Java application.
5. Follow instructions to set up and play the game.
6. Have fun!


Group 11, CPSC 233 Summer 2021