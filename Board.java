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
	//	BoardSize is set to 15x15 by default.
	private int boardSize = 15;
	
	/**
	 * Default constructor that takes in no parameters, and initializes the instance variable "board".
	 */
	public Board() {
		//	"board" is initialized to a 2D stone array of default size (15x15).
		this.board = new Stone[boardSize][boardSize];
	}
	
	/**
	 * Constructor that takes in "boardSize" as a parameter, and initializes the instance variables "board"
	 * and "boardSize".
	 * @param boardSize the size of the game board.
	 */
	public Board(int boardSize) {
		/*
		 * If condition checks that "boardSize" is equal to one of four board size options offered
		 * before updating the instance variable "boardSize".
		 */
		if (boardSize == 9 || boardSize == 13 || boardSize == 15 || boardSize == 19) {
			this.boardSize = boardSize;
		}
		this.board = new Stone[boardSize][boardSize];
	}
	
	/**
	 * Method that is used to initialize the game board, as well as the instance variable "alphabetList"
	 * (containing the index for the game board).
	 */
	public void initBoard() {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				//	"board" is filled with the enum constant "Stone.EMPTY".	
				this.board[i][j] = Stone.EMPTY;
			}
			/*
			 * "alphabetList" forms the index (coordinate system) to be used for the game board.
			 * The put method is used to insert a integer and character tuple each time the outer for loop is executed.
			 * Thus, the length of "alphabetList" will always be equal to "boardSize", and will always go in ascending
			 * order beginning from (1, 'A'). The key/value is incremented by one in each for loop execution, so (2, 'B')
			 * would be put into "alphabetList" following (1, 'A'), etc.
			 */
			alphabetList.put(i + 1, (char)(i + 65));
		}
	}
	
	/**
	 * Method that is responsible for printing out the game board in the terminal.
	 */
	public void printBoard() {
		//	Beginning for loop iterates through "alphabetList" to print out index for x-axis.		
		System.out.print("  ");
		for (int i = 1; i <= boardSize; i++) {
			System.out.print(" " + alphabetList.get(i));
		}
		System.out.println();
		/* 
		 * Nested for loop iterates through "board" and prints out each element, creating
		 * the game board in the terminal.	
		 */
		for (int r = 0; r < boardSize; r++) {
			for (int c = 0; c < boardSize; c++) {
				/* 
				 * At the beginning of each new line (i.e. when "c" is equal to zero), the index for the y-axis
				 * is printed first.
				 */
				if (c == 0) {
					/* 
					 * If "r" is less than 9, we must add additional whitespace when printing the index to avoid
					 * the game board becoming staggered.
					 */
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
	
	/**
	 * Getter method for instance variable "board".
	 * @return board the current game board.
	 */
	public Stone[][] getBoard() {
		return board;
	}
	
	/**
	 * Getter method for instance variable "boardSize".
	 * @return boardSize the current size of the board.
	 */
	public int getBoardSize() {
		return boardSize;
	}
	
	/**
	 * Getter method for a specified element in "board".
	 * @param row the y-coordinate of the stone.
	 * @param col the x-coordinate of the stone.
	 * @return the Stone enum value of specified coordinate in "board".
	 */
	public Stone getCoord(int row, int col) {
		return board[row][col];
	}
	
	/**
	 * Setter method that updates a specified element in "board".
	 * @param row the y-coordinate of the Stone.
	 * @param col the x-coordinate of the Stone.
	 * @param stone a Stone object containing the color of the stone.
	 */
	public void setCoord(int row, int col, Stone stone) {
		this.board[row][col] = stone;
	}
	
	/**
	 * Method that adds a move to the current game board.
	 * @param move a move Object containing the coordinates and color of the stone.
	 */
	public void addMove(Move move) {
		setCoord(move.getRow(), move.getCol(), move.getStone());
	}
	
	/**
	 * Getter method for the instance variable "alphabetList".
	 * @return alphabetList a HashMap object containing the index of the current game board.
	 */
	public Map<Integer, Character> getAlphabetList() {
		return alphabetList;
	}
	
	/**
	 * Main method that is used to test the functionality of the Board class.
	 * @param args arguments passed to the main method.
	 */
//	public static void main(String[] args) {
//        Board board = new Board(15);
//        board.initBoard();
//        board.printBoard();
//    }
}
