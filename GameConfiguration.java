import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * 
 * @author Fu-Yin Lin
 *
 */
public class GameConfiguration {
	private static final int WIN_COUNT = 5;
    private Board chessBoard;
    
    public GameConfiguration() {
    }

	public Board getChessBoard() {
		return chessBoard;
	}

	public void setChessBoard(Board board) {
		this.chessBoard = board;
		chessBoard.initBoard();
	}
	
	public Move isValidMove(String coord, Stone stone) {
		Map<Integer, Character> alphabetList = chessBoard.getAlphabetList();
		Move move;
		if (coord.length() < 2 || coord.length() > 3) {
			System.out.println("invalid coord");
			move = null;
		}
		int row = Integer.parseInt(coord.substring(1));
		if (!alphabetList.containsValue(coord.charAt(0)) || row >= chessBoard.getBoardSize()) {
			System.out.println("coord(s) exceed board size");
			move = null;
		}
		int col = (int)(coord.charAt(0)) - 64;
		if (chessBoard.getCoord(row, col) == Stone.INDEX) {
			System.out.println("please enter valid coord");
			move = null;
		}
		if (chessBoard.getCoord(row, col) != Stone.EMPTY) {
			System.out.println("current coord is not empty");
			move = null;
		}
		move = new Move(row, col, stone);
		return move;
	}
    
    public Result checkWin(int row, int col, Stone stone) {
    	Stone[][] board = chessBoard.getBoard();
    	int boardSize = chessBoard.getBoardSize();
    	ArrayList<Integer> directionScores = new ArrayList<Integer>();
    	// top, bottom
    	int count = 1;
    	for (int i = 1; i < row; i++) {
    		if (board[row - i][col] == stone) {
    			count++;
    		} else break;
    	}
    	for (int i = 1; i <= boardSize - row; i++) {
    		if (board[row + i][col] == stone) {
    			count++;
    		} else break;
    	}
    	directionScores.add(count);
    	count = 1;
    	// left, right
    	for (int i = 1; i < col; i++) {
    		if (board[row][col - i] == stone) {
    			count++;
    		} else break;
    	}
    	for (int i = 1; i <= boardSize - col; i++) {
    		if (board[row][col + i] == stone) {
    			count++;
    		} else break;
    	}
    	directionScores.add(count);
    	count = 1;
    	// top left, bottom right
    	for (int i = 1; i < row && i < col; i++) {
    		if (board[row - i][col - i] == stone) {
    			count++;
    		} else break;
    	}
    	for (int i = 1; i <= boardSize - row && i <= boardSize - col; i++) {
    		if (board[row + i][col + i] == stone) {
    			count++;
    		} else break;
    	}
    	directionScores.add(count);
    	count = 1;
    	// top right, bottom left
    	for (int i = 1; i < row && i <= boardSize - col; i++) {
    		if (board[row - i][col + i] == stone) {
    			count++;
    		} else break;
    	}
    	for (int i = 1; i <= boardSize - row && i < col; i++) {
    		if (board[row + i][col - i] == stone) {
    			count++;
    		} else break;
    	}
    	directionScores.add(count);
    	int max = Collections.max(directionScores);
    	if (max > WIN_COUNT) {
    		return Result.DRAW;
    	} else if (max == WIN_COUNT) {
    		if (stone == Stone.BLACK) return Result.BLACK;
    		else return Result.WHITE;
    	}
    	return Result.CONTINUE;
    }
}