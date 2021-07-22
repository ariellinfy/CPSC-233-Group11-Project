/**
 * The class contains data needed for a single move in a game. Setter methods
 * are not provided in the class, since a Move object is not allowed to be
 * mutated after initialization.
 * 
 * @author Fu-Yin Lin
 * 
 */
public class Move {
	private int row;
	private int col;
	private Stone stone;

	/**
	 * Initialize a Move object with its position and type on the board.
	 * 
	 * @param row   the row index number of the board.
	 * @param col   the column index number of the board.
	 * @param stone the stone color of this move (either black or white).
	 */
	public Move(int row, int col, Stone stone) {
		this.row = row;
		this.col = col;
		this.stone = stone;
	}

	/**
	 * Get row index of this move.
	 * 
	 * @return the move's index number on the vertical axis of the board.
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Get column index of this move.
	 * 
	 * @return the move's index number on the horizontal axis of the board.
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Get stone color of this move.
	 * 
	 * @return the move's Stone type on the board.
	 */
	public Stone getStone() {
		return stone;
	}

	/**
	 * Customize toString method by overriding the default method inherited from
	 * Object class.
	 * 
	 * @return a string representation of this move.
	 */
	@Override
	public String toString() {
		StringBuilder moveInfo = new StringBuilder();
		moveInfo.append("Row: ");
		moveInfo.append(row);
		moveInfo.append("; Column: ");
		moveInfo.append(col);
		moveInfo.append("; Color: ");
		moveInfo.append(stone);
		return moveInfo.toString();
	}

	/**
	 * Testing constructor and getter methods in Move class.
	 */
//	public static void main(String[] args) {
//		Move m1 = new Move(1, 2, Stone.BLACK);
//		System.out.println(m1);
//		System.out.println(m1.getRow());
//		System.out.println(m1.getCol());
//		System.out.println(m1.getStone());
//	}
}