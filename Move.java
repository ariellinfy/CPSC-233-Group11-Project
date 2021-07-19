/**
 * 
 * @author Fu-Yin Lin
 *
 */
public class Move {
	private int row;
	private int col;
	private Stone stone;
	
	public Move(int row, int col, Stone stone) {
		this.row = row;
		this.col = col;
		this.stone = stone;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public Stone getStone() {
		return stone;
	}

	public void setStone(Stone stone) {
		this.stone = stone;
	}
}