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
	
	public Board() {
		this.board = new Stone[boardSize][boardSize];
	}
	
	public Board(int boardSize) {
		if (boardSize == 9 || boardSize == 13 || boardSize == 15 || boardSize == 19) {
			this.boardSize = boardSize;
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
		System.out.print("  ");
		for (int i = 1; i <= boardSize; i++) {
			System.out.print(" " + alphabetList.get(i));
		}
		System.out.println();
		for (int r = 0; r < boardSize; r++) {
			for (int c = 0; c < boardSize; c++) {
				if (c == 0) {
					if (r < 9) {
						System.out.print(" " + (r + 1));
					} else {
						System.out.print(r + 1);
					}
				}
				if (board[r][c] == Stone.EMPTY) {
					System.out.print(" +");
				}
				if (board[r][c] == Stone.BLACK) {
					System.out.print(" ¡´");
				}
				if (board[r][c] == Stone.WHITE) {
					System.out.print(" ¡³");
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
