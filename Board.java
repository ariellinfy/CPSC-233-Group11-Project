/**
 * Class that manages and creates the game board for the Gomoku application.
 * @author Justin Chua
 *
 */

public class Board {
	
	private String[][] board;
	
	public Board() {
		//	Board size is set to 15x15 by default.
		board = new String[15][15];
	}
	
	public void setBoard(int size) {
		this.board = new String[size][size];
	}
	
	public String[][] getBoard() {
		return this.board;
	}
	
	public void printBoard() {
		/*
		 * Simple for loop that iterates through each row and column in the board 2D array,
		 * and prints each element to the terminal window.
		 */
		for (int i = 0; i < this.board.length; i++) {
			System.out.println();
			for (int j = 0; j < this.board[0].length; j++) {
				System.out.print(this.board[i][j]);
			}
		}
	}
}
