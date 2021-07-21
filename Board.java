import java.util.HashMap;
import java.util.Map;

/**
 * Class that manages and creates the game board for the Gomoku application.
 * @author Justin Chua
 *
 */

public class Board {
	
	private Map<Integer, Character> alphabetList = new HashMap<Integer, Character>();
	private Stone[][] board;
	//	boardSize is set to 15 by default.
	private int boardSize = 15;
	
	public Board(int boardSize) {
		if (boardSize == 9 || boardSize == 13 || boardSize == 15 || boardSize == 19) {
			this.boardSize = boardSize;
		} else {
			System.out.println("Invalid board size. The board size was set to 15 by default.");
		}
		this.board = new Stone[boardSize][boardSize];
	}
	
	public void initBoard() {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				this.board[i][j] = Stone.EMPTY;
			}
			alphabetList.put(i + 1, (char)(i + 65));
		}
	}
	
	public void printBoard() {
		System.out.print("   ");
		for (int i = 0; i < boardSize; i++) {
			System.out.print((char)(i + 65) + " ");
		}
		System.out.println();
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				//	This if statement prints the number index for the y-axis.			
				if (j == 0) {
					if (i < 9) {
						System.out.print(" " + (i + 1) + " ");
					} else {
						System.out.print((i + 1) + " ");
					}
				}
				if (board[i][j] == Stone.EMPTY) {
					System.out.print("+ ");
				} else if (board[i][j] == Stone.BLACK) {
					System.out.print("● ");
				} else if (board[i][j] == Stone.WHITE) {
					System.out.print("○ ");
				}
			}
			System.out.println();
		}
	}
	
	public Stone[][] getBoard() {
		return board;
	}
	
	public int getBoardSize() {
		return boardSize;
	}
	
	public Stone getCoord(int row, int col) {
		return board[row][col];
	}
	
	public void setCoord(int row, int col, Stone stone) {
		this.board[row][col] = stone;
	}
	
	public void addMove(Move move) {
		setCoord(move.getRow(), move.getCol(), move.getStone());
	}
	
	public Map<Integer, Character> getAlphabetList() {
		return alphabetList;
	}
	
	public static void main(String[] args) {
        Board board = new Board(15);
        board.initBoard();
        board.printBoard();
    }
	
}
