package application;

import java.util.ArrayList;
import java.util.Scanner;
import model.*;

/**
 * This class is responsible for running the Gomoku application for the user,
 * through the utilization of all other classes.
 * 
 * @author Fu-Yin Lin, Justin Chua
 *
 */
public class GomokuText {
	private GameConfiguration config = new GameConfiguration();
	private Player playerBlack;
	private Player playerWhite;
	private boolean blackTurn = true;
	private int winnerScore = 20;

	/**
	 * Get current player black of the game.
	 * 
	 * @return playerBlack a Player object that contains information related to the
	 *         player of color Black (i.e. name of player, num of moves made, color
	 *         of player).
	 */
	public Player getPlayerBlack() {
		return playerBlack;
	}

	/**
	 * Get current player white of the game.
	 * 
	 * @return playerWhite a Player object that contains information related to the
	 *         player of color White (i.e. name of player, num of moves made, color
	 *         of player).
	 */
	public Player getPlayerWhite() {
		return playerWhite;
	}

	/**
	 * Method that prompts the user using the message passed in as parameter, and
	 * returns the corresponding user input.
	 * 
	 * @param scanner a Scanner object used to read the text from user input.
	 * @param message the message used to prompt the user in the terminal.
	 * @return the next line entered by user.
	 */
	private String promptUser(Scanner scanner, String message) {
		System.out.print(message);
		return scanner.nextLine();
	}

	/**
	 * Method that prints a message to the terminal. No user inputs are handled in
	 * this method.
	 * 
	 * @param message the message to be printed in the terminal.
	 */
	private void messageToUser(String message) {
		System.out.println(message);
	}

	/**
	 * Method that handles the user prompts for the opponent type filter option.
	 * 
	 * @param scanner a Scanner object used to read the text from user input.
	 * @return opponent a HumanPlayer/ComputerPlayer object dependent on user input.
	 *         This object contains information of the other player (i.e. stone
	 *         color, num of moves, etc.).
	 */
	private Player setupOpponent(Scanner scanner) {
		Player opponent = null;
		String playAgainst = promptUser(scanner, "Do you want to play against computer or human? (computer/human)? ");
		if (playAgainst.equalsIgnoreCase("computer")) {
			String difficultyLevel = promptUser(scanner, "Set difficulty level (easy/medium/hard): ");
			Level difficulty = Level.MEDIUM;
			if (difficultyLevel.equalsIgnoreCase("easy")) {
				difficulty = Level.EASY;
			} else if (difficultyLevel.equalsIgnoreCase("medium")) {
				difficulty = Level.MEDIUM;
			} else if (difficultyLevel.equalsIgnoreCase("hard")) {
				difficulty = Level.HARD;
			} else {
				messageToUser("Invalid input, AI is set to be in medium level.\n");
			}
			opponent = new ComputerPlayer(difficulty);
		} else if (playAgainst.equalsIgnoreCase("human")) {
			opponent = new HumanPlayer();
		} else {
			messageToUser("Invalid input, opponent set to an AI in medium level.\n");
			opponent = new ComputerPlayer(Level.MEDIUM);
		}
		return opponent;
	}

	/**
	 * Method that handles the user prompts for the stone color filter option.
	 * 
	 * @param scanner  a Scanner object used to read the text from user input.
	 * @param opponent the opponent player set from previous user choice.
	 */
	private void chooseColor(Scanner scanner, Player opponent) {
		String userColor = promptUser(scanner, "You want to play as? black always goes first (black/white) ");
		if (userColor.equalsIgnoreCase("black")) {
			this.playerBlack = new HumanPlayer(Stone.BLACK);
			opponent.setPlayerColor(Stone.WHITE);
			this.playerWhite = opponent;
		} else if (userColor.equalsIgnoreCase("white")) {
			this.playerWhite = new HumanPlayer(Stone.WHITE);
			opponent.setPlayerColor(Stone.BLACK);
			this.playerBlack = opponent;
			/*
			 * If an invalid input is entered (i.e. not white or black), the color of the
			 * main player is set to black by default.
			 */
		} else {
			messageToUser("Invalid input, your color is set to black.\n");
			this.playerBlack = new HumanPlayer(Stone.BLACK);
			opponent.setPlayerColor(Stone.WHITE);
			this.playerWhite = opponent;
		}
	}

	/**
	 * Method that handles the user prompts for the board size filter option.
	 * 
	 * @param scanner a Scanner object used to read the text from user input.
	 */
	private void setupBoard(Scanner scanner) {
		try {
			int boardSize = Integer.parseInt(promptUser(scanner, "Set board size (9/13/15/19): "));
			if (boardSize == 9 || boardSize == 13 || boardSize == 15 || boardSize == 19) {
				this.config.setChessBoard(new Board(boardSize));
			} else {
				throw new NumberFormatException();
			}
			/*
			 * If an invalid board size is entered, a NumberFormatException is thrown and
			 * board size is set to 15 by default.
			 */
		} catch (NumberFormatException ex) {
			messageToUser("Invalid input, the board size was set to 15x15 by default.\n");
			this.config.setChessBoard(new Board());
		}
	}

	/**
	 * Method that handles the user prompts for the undo option.
	 * 
	 * @param scanner a Scanner object used to read the text from user input.
	 */
	private void setUndo(Scanner scanner) {
		String noUndo = promptUser(scanner, "No Undo (y/n): ");
		if (noUndo.equalsIgnoreCase("y")) {
			this.config.setUndo(false);
		} else if (noUndo.equalsIgnoreCase("n")) {
			this.config.setUndo(true);
		} else {
			messageToUser("Invalid input, default set to no undo in this game.\n");
			this.config.setUndo(false); // Default is no undo.
		}
	}

	/**
	 * Method that is responsible for handling user inputs for all available game
	 * options, as well as running the game application in the terminal.
	 * 
	 * @param scanner a Scanner object used to read the text from user input.
	 */
	public void setup(Scanner scanner) {
		messageToUser("Welcome to Gomoku!");
		Player opponent = setupOpponent(scanner);
		chooseColor(scanner, opponent);
		setupBoard(scanner);
		setUndo(scanner);
		play(scanner);
	}

	/**
	 * Method that is responsible for running the Gomoku game for the user.
	 * 
	 * @param scanner a Scanner object used to read the text from user input.
	 */
	public void play(Scanner scanner) {
		Board chessboard = config.getChessBoard();
		boolean gameOver = false;
		if (playerBlack instanceof ComputerPlayer == false) {
			chessboard.printBoard();
		}
		/*
		 * This while loop allows the user to be continuously prompted for making moves
		 * on the game board until a win is detected.
		 */
		while (!gameOver) {
			Move move = null;
			boolean validInput = false;
			Player currentPlayer = playerBlack;
			/*
			 * This while loop allows the user to be continuously prompted for a move until
			 * a valid move is entered.
			 */
			while (!validInput) {
				if (blackTurn) {
					currentPlayer = playerBlack;
					move = nextMove(playerBlack, scanner);
				} else {
					currentPlayer = playerWhite;
					move = nextMove(playerWhite, scanner);
				}
				// Check if the move is valid or not and update the game accordingly.
				validInput = checkValidMove(move, currentPlayer);
			}

			/*
			 * After a new stone has been placed, the game board is re-printed in the
			 * terminal with the updated stone placements.
			 */
			messageToUser("");
			chessboard.printBoard();
			// Check if the game is over or not, skip if undo entered.
			if (move.getStone() != Stone.EMPTY) {
				gameOver = checkRoundResult(move);
			}
		}
	}

	/**
	 * This method generates move from the current player, also handles undo if user
	 * enters undo to undo previous moves.
	 * 
	 * @param currentPlayer the player that is making the next move.
	 * @param scanner       a Scanner object used to read the text from user input.
	 * @return the next move object generated from either AI or human.
	 */
	private Move nextMove(Player currentPlayer, Scanner scanner) {
		Move move = null;
		/*
		 * The user is only prompted if the corresponding player is a human. If
		 * ComputerPlayer was selected, no message is printed and instead the computer
		 * just makes it's own move accordingly.
		 */
		if (currentPlayer instanceof ComputerPlayer) {
			move = currentPlayer.getMove(config);
		} else {
			String prompt = promptUser(scanner, currentPlayer.getPlayerColor()
					+ "'s turn, enter a valid coord (eg. A2) or 'undo' to undo previous move: ").toUpperCase();
			/*
			 * Check if user input is "undo" and check if undo is allowed in the current
			 * game setting.
			 */
			if (config.getUndo() && prompt.equalsIgnoreCase("undo")) {
				move = onUndo(playerBlack, playerWhite);
				if (move != null) {
					// If onUndo returns a move other than null, then set the move stone to empty.
					move.setEmptyStone();
				} else {
					/*
					 * This new move is meant to be a placeholder only, so we can pass the gameover
					 * check when return.
					 */
					move = new Move(-1, -1, Stone.EMPTY);
				}
			} else {
				move = currentPlayer.getMove(config, prompt);
			}
		}
		return move;
	}

	/**
	 * Method that will undo the last move(s) and update all related variables.
	 * 
	 * @param playerBlack the current black player of the game.
	 * @param playerWhite the current white player of the game.
	 * @return the last valid move of the game, or null if no moves have made to the
	 *         game.
	 */
	private Move onUndo(Player playerBlack, Player playerWhite) {
		Move lastMove = null;
		try {
			ArrayList<Move> undoMoves = config.undoMove(blackTurn, playerBlack, playerWhite);
			lastMove = undoMoves.get(undoMoves.size() - 1);
		} catch (InvalidUndoException e) {
			System.out.println(e.getMessage());
		}
		return lastMove;
	}

	/**
	 * Checks to see if a valid move was made by the user (i.e. getMove() returns
	 * null if the move is not valid). If the latest move is valid, then update the
	 * board and corresponding player's move data.
	 * 
	 * @param latestMove the most recent move.
	 * @return whether the latestMove is a valid move or not.
	 */
	private boolean checkValidMove(Move latestMove, Player currentPlayer) {
		boolean validInput = false;
		if (latestMove != null) {
			// Update board and corresponding player data.
			if (latestMove.getStone() != Stone.EMPTY) {
				config.updateBoard(latestMove);
				currentPlayer.getAllValidMoves().add(latestMove);
				currentPlayer.incrementMoveCount();
			}
			validInput = true;
		} else {
			/*
			 * If null is returned from getMove(), the move is considered invalid therefore
			 * validInput is set to false. This means that this method will return to the
			 * top of the while (!validInput) loop, allowing the user to be re-prompted
			 * until a valid move is made.
			 */
			validInput = false;
		}
		return validInput;
	}

	/**
	 * At the end of each while loop execution, we check on if the game has a a
	 * winning line (of 5 stones) and if the board has empty spot available. If
	 * there is no winning line and spots are available on the board, the game
	 * continues and it becomes the other player's turn. If there is a winning
	 * line/draw or if the board is full of color stones, a series of messages are
	 * printed displaying which player won, and the corresponding game score.
	 * Afterwards, the game is ended.
	 * 
	 * @param latestMove the most recent move.
	 * @return whether the game has reached the end or not.
	 */
	private boolean checkRoundResult(Move latestMove) {
		boolean gameOver = false;
		Result roundResult = config.checkWinningLine(latestMove);
		Result checkNumOfMoves = config.isBoardFull(playerBlack, playerWhite);
		if (roundResult == Result.CONTINUE && checkNumOfMoves == Result.CONTINUE) {
			blackTurn = !blackTurn;
		} else if (checkNumOfMoves == Result.DRAW) {
			setDrawScore();
			messageToUser("\nIt's a draw!");
			messageToUser("Game score is " + winnerScore);
			gameOver = true;
		} else if (roundResult == Result.BLACK) {
			this.winnerScore = config.calculateScore(playerBlack, playerWhite);
			messageToUser("\nBlack wins!");
			messageToUser("Game score is " + winnerScore);
			gameOver = true;
		} else if (roundResult == Result.WHITE) {
			this.winnerScore = config.calculateScore(playerWhite, playerBlack);
			messageToUser("\nWhite wins!");
			messageToUser("Game score is " + winnerScore);
			gameOver = true;
		} else if (roundResult == Result.DRAW) {
			setDrawScore();
			messageToUser("\nIt's a draw!");
			messageToUser("Game score is " + winnerScore);
			gameOver = true;
		}
		return gameOver;
	}

	/**
	 * Set winner score to be 0 if a draw occurs.
	 */
	private void setDrawScore() {
		this.winnerScore = 0;
	}

	/**
	 * The main method that launches the text-based Gomoku application.
	 */
	public static void main(String[] args) {
		GomokuText game = new GomokuText();
		Scanner scanner = new Scanner(System.in);
		try {
			game.setup(scanner);
		} finally {
			scanner.close();
		}
	}
}