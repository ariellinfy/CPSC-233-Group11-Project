package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * The class contains data related to the current state of the game, as well as
 * methods that validate and determine the status of the game after each move.
 * 
 * @author Fu-Yin Lin
 * 
 */
public class GameConfiguration {
	private static final int WIN_COUNT = 5;
	private Board chessBoard;
	private boolean allowUndo = false;
	private int gameTime = 5;

	/**
	 * Get the board object of this game.
	 * 
	 * @return the board of current game.
	 */
	public Board getChessBoard() {
		return chessBoard;
	}

	/**
	 * Set and initialize the board of the game.
	 * 
	 * @param board the board to be used in this game.
	 */
	public void setChessBoard(Board board) {
		this.chessBoard = board;
		chessBoard.initBoard();
	}

	public boolean getUndo() {
		return allowUndo;
	}
	
	public void setUndo(boolean undo) {
		this.allowUndo = undo;
	}
	
	public int getGameTime() {
		return gameTime;
	}
	
	public void setGameTime(int gameTime) {
		this.gameTime = gameTime;
	}
	
	/**
	 * Add a move and update the game board.
	 * 
	 * @param move the Move object to be added to the board.
	 */
	public void updateBoard(Move move) {
		chessBoard.setCoord(move.getRow(), move.getCol(), move.getStone());
	}
	
	public boolean checkPlayersNumOfMoves(Player playerBlack, Player playerWhite) {
		return playerBlack.getNumOfMoves() == playerWhite.getNumOfMoves();
	}
	
	public boolean isMoveAvailable(Player currentPlayer) {
		return currentPlayer.getNumOfMoves() > 0;
	}
	
	public ArrayList<Move> undoMove(boolean blackTurn, Player playerBlack, Player playerWhite) throws InvalidUndoException {
		ArrayList<Move> undoMoves = new ArrayList<Move>();
		Move lastMove = null;
		if (blackTurn) {
			if (checkPlayersNumOfMoves(playerBlack, playerWhite) && isMoveAvailable(playerWhite) && isMoveAvailable(playerBlack)) {
				lastMove = removeMove(playerWhite);
				undoMoves.add(lastMove);
				lastMove = removeMove(playerBlack);
				undoMoves.add(lastMove);
			} else if (isMoveAvailable(playerBlack)) {
				lastMove = removeMove(playerBlack);
				undoMoves.add(lastMove);
			} else {
				throw new InvalidUndoException("Undo is not allowed in current board status.");
			}
		} else {
			if (checkPlayersNumOfMoves(playerBlack, playerWhite) && isMoveAvailable(playerWhite)) {
				lastMove = removeMove(playerWhite);
				undoMoves.add(lastMove);
			} else if (isMoveAvailable(playerBlack) && isMoveAvailable(playerWhite)) {
				lastMove = removeMove(playerBlack);
				undoMoves.add(lastMove);
				lastMove = removeMove(playerWhite);
				undoMoves.add(lastMove);
			} else {
				throw new InvalidUndoException("Undo is not allowed in current board status.");
			}
		}
		return undoMoves;
	}
	
	public Move removeMove(Player currentPlayer) {
		Move lastMove = currentPlayer.getAllValidMoves().get(currentPlayer.getNumOfMoves() - 1);
		currentPlayer.getAllValidMoves().remove(currentPlayer.getNumOfMoves() - 1);
		currentPlayer.decrementMoveCount();
		lastMove.setEmptyStone();
		updateBoard(lastMove);
		return lastMove;
	}

	/**
	 * Calculate game score based on winner number of moves, also update each human
	 * player with game ending score accordingly.
	 * 
	 * @param winner   the winner player.
	 * @param opponent the loser player.
	 * @return the winner score of the game.
	 */
	public int calculateScore(Player winner, Player opponent) {
		int numOfMoves = winner.getNumOfMoves();
		int gamescore = numOfMoves % 5 == 0 ? numOfMoves / 5 : (numOfMoves / 5) + 1;
		int winnerScore = 20 - gamescore;
		allocateScore(winner, winnerScore);
		allocateScore(opponent, -winnerScore);
		return winnerScore;
	}

	/**
	 * Update ranking if the player is a human player.
	 * 
	 * @param player the player to be updated.
	 * @param score  the score to be added to player's ranking.
	 */
	private void allocateScore(Player player, int score) {
		if (player instanceof HumanPlayer) {
			((HumanPlayer) player).addRanking(score);
		}
	}

	/**
	 * A method checks whether a move is valid or not.
	 * 
	 * @param coord a string representation of the targeting location of a move.
	 * @param stone the stone color of current player that makes a move.
	 * @return a Move object if the coord input passes all the validity checks.
	 * @throws InvalidPlacementException if any of the validity checks failed.
	 */
	public Move isValidMove(String coord, Stone stone) throws InvalidPlacementException {
		Map<Integer, Character> alphabetList = chessBoard.getAlphabetList();
		if (coord.length() < 2 || coord.length() > 3) {
			throw new InvalidPlacementException("Error: coord length should be within 2 to 3.");
		}
		if (!alphabetList.containsValue(coord.charAt(0))) {
			throw new InvalidPlacementException("Error: horizontal coord is not within the board size.");
		}
		// Casting first character in the coord string to integer.
		int col = (int) (coord.charAt(0)) - 65;
		// Try parsing the rest of the coord string to integer.
		int row = -1;
		try {
			row = Integer.parseInt(coord.substring(1)) - 1;
		} catch (NumberFormatException ex) {
			throw new InvalidPlacementException("Error: vertical coord should be numeric.");
		}
		if (row >= chessBoard.getBoardSize() || row < 0) {
			throw new InvalidPlacementException(
					"Error: vertical coord should be greater than 0 and within the board size.");
		}
		if (chessBoard.getCoord(row, col) != Stone.EMPTY) {
			throw new InvalidPlacementException("Error: a stone is already in this coord, please choose another one.");
		}
		return new Move(row, col, stone);
	}

	/**
	 * A method checks whether a move is valid or not.
	 * 
	 * @param row   the row index number of the board.
	 * @param col   the column index number of the board.
	 * @param stone the stone color of current player that makes a move.
	 * @return a Move object if the coord indices pass all the validity checks.
	 * @throws InvalidPlacementException if any of the validity checks failed.
	 */
	public Move isValidMove(int row, int col, Stone stone) throws InvalidPlacementException {
		if (row < 0 || row >= chessBoard.getBoardSize()) {
			throw new InvalidPlacementException("Error: row index exceeds the board size.");
		}
		if (col < 0 || col >= chessBoard.getBoardSize()) {
			throw new InvalidPlacementException("Error: column index exceeds the board size.");
		}
		if (chessBoard.getCoord(row, col) != Stone.EMPTY) {
			throw new InvalidPlacementException("Error: location is occupied with a stone already.");
		}
		return new Move(row, col, stone);
	}

	/**
	 * Check the board spots availability.
	 * 
	 * @param player1 one the two players in the game.
	 * @param player2 the other player in the game.
	 * @return enum Continue if sum of two players' moves do not exceed total
	 *         available spots in the board, else return enum Draw to end the game.
	 */
	public Result isBoardFull(Player player1, Player player2) {
		int boardSize = chessBoard.getBoardSize();
		int totalAvailableMoves = boardSize * boardSize;
		if (player1.getNumOfMoves() + player2.getNumOfMoves() < totalAvailableMoves) {
			return Result.CONTINUE;
		} else {
			return Result.DRAW;
		}
	}

	/**
	 * A method determines the status of the game after each move. It creates an
	 * arrayList of stone counts from four directions of the move: top to bottom,
	 * left to right, top left to bottom right, top right to bottom left; then find
	 * the max count out of four directions. If max count is over the WIN_COUNT
	 * constant variable, then the game result in a draw. If max count is exactly
	 * equals WIN_COUNT, then the game has a winner, and the winner is determined
	 * based on the stone color of the current move. For all other situation (max
	 * count is less than WIN_COUNT), the game will continue.
	 * 
	 * @param move the current last move in the game.
	 * @return the result of the placing the move object on the board.
	 */
	public Result checkWinningLine(Move move) {
		Stone[][] board = chessBoard.getBoard();
		int boardSize = chessBoard.getBoardSize();
		int row = move.getRow();
		int col = move.getCol();
		Stone stone = move.getStone();

		ArrayList<Integer> directionCounts = new ArrayList<Integer>();

		// 1. Top to bottom
		/*
		 * Set current stone count to 1, this count number will reset at the beginning
		 * of each direction looping.
		 */
		int count = 1;
		// Current move towards to top.
		for (int i = 1; i <= row; i++) {
			if (board[row - i][col] == stone) {
				count++;
			} else
				break;
		}
		// Current move towards bottom.
		for (int i = 1; i < boardSize - row; i++) {
			if (board[row + i][col] == stone) {
				count++;
			} else
				break;
		}
		directionCounts.add(count);

		// 2. Left to right
		count = 1;
		// Current move towards left.
		for (int i = 1; i <= col; i++) {
			if (board[row][col - i] == stone) {
				count++;
			} else
				break;
		}
		// Current move towards right.
		for (int i = 1; i < boardSize - col; i++) {
			if (board[row][col + i] == stone) {
				count++;
			} else
				break;
		}
		directionCounts.add(count);

		// 3. Top left to bottom right
		count = 1;
		// Current move towards top left.
		for (int i = 1; i <= row && i <= col; i++) {
			if (board[row - i][col - i] == stone) {
				count++;
			} else
				break;
		}
		// Current move towards bottom right.
		for (int i = 1; i < boardSize - row && i < boardSize - col; i++) {
			if (board[row + i][col + i] == stone) {
				count++;
			} else
				break;
		}
		directionCounts.add(count);

		// 4. Top right to bottom left
		count = 1;
		// Current move towards top right.
		for (int i = 1; i <= row && i < boardSize - col; i++) {
			if (board[row - i][col + i] == stone) {
				count++;
			} else
				break;
		}
		// Current move towards bottom left.
		for (int i = 1; i < boardSize - row && i <= col; i++) {
			if (board[row + i][col - i] == stone) {
				count++;
			} else
				break;
		}
		directionCounts.add(count);

		// Find the max count from directionCounts list.
		int maxCount = Collections.max(directionCounts);

		if (maxCount > WIN_COUNT) {
			return Result.DRAW;
		} else if (maxCount == WIN_COUNT) {
			if (stone == Stone.BLACK)
				return Result.BLACK;
			else
				return Result.WHITE;
		}
		return Result.CONTINUE;
	}

	/**
	 * Testing methods in GameConfiguration class.
	 */
//    public static void main(String[] args) {
//    	GameConfiguration gc = new GameConfiguration();
//    	Board b = new Board(15);
//    	gc.setChessBoard(b);
//    	// testing isValidMove method
//    	System.out.println(gc.isValidMove("A230", Stone.BLACK));
//    	System.out.println(gc.isValidMove("Z2", Stone.BLACK));
//    	System.out.println(gc.isValidMove("A16", Stone.BLACK));
//    	System.out.println(gc.isValidMove("A0", Stone.BLACK));
//    	Move m1 = gc.isValidMove("A2", Stone.WHITE);
//    	b.addMove(m1);
//    	b.printBoard();
//    	System.out.println(gc.isValidMove("A2", Stone.BLACK));
//    	
//    	// testing checkWinningLine method
//    	System.out.println(gc.checkWinningLine(m1));
//    	Move m2 = gc.isValidMove("A3", Stone.WHITE);
//    	b.addMove(m2);
//    	System.out.println(gc.checkWinningLine(m2));
//    	Move m3 = gc.isValidMove("A4", Stone.WHITE);
//    	b.addMove(m3);
//    	System.out.println(gc.checkWinningLine(m3));
//    	Move m4 = gc.isValidMove("A5", Stone.WHITE);
//    	b.addMove(m4);
//    	System.out.println(gc.checkWinningLine(m4));
//    	Move m5 = gc.isValidMove("A6", Stone.WHITE);
//    	b.addMove(m5);
//    	System.out.println(gc.checkWinningLine(m5));
//    	b.printBoard();
//    	Move m6 = gc.isValidMove("A7", Stone.WHITE);
//    	b.addMove(m6);
//    	System.out.println(gc.checkWinningLine(m6));
//    }
}