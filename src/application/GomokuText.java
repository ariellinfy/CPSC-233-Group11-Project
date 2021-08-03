package application;

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

	public Player getPlayerBlack() {
		return playerBlack;
	}

	public Player getPlayerWhite() {
		return playerWhite;
	}

	private String promptUser(Scanner scanner, String message) {
		System.out.print(message);
		return scanner.nextLine();
	}

	private void messageToUser(String message) {
		System.out.println(message);
	}

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
		} else {
			messageToUser("Invalid input, your color is set to black.\n");
			this.playerBlack = new HumanPlayer(Stone.BLACK);
			opponent.setPlayerColor(Stone.WHITE);
			this.playerWhite = opponent;
		}
	}

	private void setupBoard(Scanner scanner) {
		try {
			int boardSize = Integer.parseInt(promptUser(scanner, "Set board size (9/13/15/19): "));
			if (boardSize == 9 || boardSize == 13 || boardSize == 15 || boardSize == 19) {
				this.config.setChessBoard(new Board(boardSize));
			} else {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException ex) {
			messageToUser("Invalid input, the board size was set to 15x15 by default.\n");
			this.config.setChessBoard(new Board());
		}
	}

	public void setup(Scanner scanner) {
		messageToUser("Welcome to Gomoku!");
		Player opponent = setupOpponent(scanner);
		chooseColor(scanner, opponent);
		setupBoard(scanner);
		play(scanner);
	}

	public void play(Scanner scanner) {
		Board chessboard = config.getChessBoard();
		boolean endGame = false;
		if (playerBlack instanceof ComputerPlayer == false) {
			chessboard.printBoard();
		}

		while (!endGame) {
			Move move = null;
			boolean validInput = false;

			while (!validInput) {
				if (blackTurn) {
					if (playerBlack instanceof ComputerPlayer) {
						move = playerBlack.getMove(config);
					} else {
						String coord = promptUser(scanner, "Black's turn, please enter a valid coord (eg. A2) ")
								.toUpperCase();
						move = playerBlack.getMove(config, coord);
					}
				} else {
					if (playerWhite instanceof ComputerPlayer) {
						move = playerWhite.getMove(config);
					} else {
						String coord = promptUser(scanner, "White's turn, please enter a valid coord (eg. A2) ")
								.toUpperCase();
						move = playerWhite.getMove(config, coord);
					}
				}

				if (move != null) {
					validInput = true;
					config.updateBoard(move);
					if (blackTurn) {
						playerBlack.getAllValidMoves().add(move);
						playerBlack.incrementMoveCount();
					} else {
						playerWhite.getAllValidMoves().add(move);
						playerWhite.incrementMoveCount();
					}
				} else {
					validInput = false;
				}
			}

			messageToUser("");
			chessboard.printBoard();

			Result roundResult = config.checkWinningLine(move);
			if (roundResult == Result.CONTINUE) {
				blackTurn = !blackTurn;
			} else if (roundResult == Result.BLACK) {
				this.winnerScore = config.calculateScore(playerBlack, playerWhite);
				messageToUser("\nBlack wins!");
				messageToUser("Game score is " + winnerScore);
				endGame = true;
			} else if (roundResult == Result.WHITE) {
				this.winnerScore = config.calculateScore(playerWhite, playerBlack);
				messageToUser("\nWhite wins!");
				messageToUser("Game score is " + winnerScore);
				endGame = true;
			} else if (roundResult == Result.DRAW) {
				setDrawScore();
				messageToUser("\nIt's a draw!");
				messageToUser("Game score is " + winnerScore);
				endGame = true;
			}
		}
	}
	
	private void setDrawScore() {
		this.winnerScore = 0;
	}

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