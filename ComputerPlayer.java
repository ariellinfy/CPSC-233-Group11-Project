import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * 
 * @author Fu-Yin Lin
 *
 */
public class ComputerPlayer extends Player {
	private static final HashMap<Level, Integer> levelMap = new HashMap<Level, Integer>();
	private Level difficultyLevel = Level.MEDIUM;
	
	public ComputerPlayer() {
		super("AI");
		initLevelMap();
	}
	
	public ComputerPlayer(Level difficultyLevel) {
		super("AI");
		this.difficultyLevel = difficultyLevel;
		initLevelMap();
	}
	
	public ComputerPlayer(Stone color, Level difficultyLevel) {
		super("AI", color);
		this.difficultyLevel = difficultyLevel;
		initLevelMap();
	}
	
	private void initLevelMap() {
		levelMap.put(Level.EASY, 7);
		levelMap.put(Level.MEDIUM, 4);
		levelMap.put(Level.HARD, 1);
	}
	
	public Level getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(Level difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	@Override
	public Move getMove(GameConfiguration currentConfig, String coord, Stone stone) {
		Stone[][] board = currentConfig.getChessBoard().getBoard();
		int boardSize = currentConfig.getChessBoard().getBoardSize();
		
		int targetRow = -1;
		int targetCol = -1;
		Move computerMove = null;
		
		ArrayList<int[]> moveList = generateMoveList(board, boardSize);
		Random rand = new Random();
		int[] pickedMove = moveList.get(rand.nextInt(moveList.size()));
		
		targetRow = pickedMove[0];
		targetCol = pickedMove[1];
		
		if (targetRow > 0 && targetCol > 0) {
			computerMove = new Move(targetRow, targetCol, stone);
		}
		
		if (computerMove != null) {
			getAllValidMoves().add(computerMove);
		}
		
		return computerMove;
	}
	
	private ArrayList<int[]> generateMoveList(Stone[][] board, int boardSize) {
		int[][] scoreTable = calculateScores(board, boardSize);		
		ArrayList<int[]> moveList = new ArrayList<int[]>();
		for (int counter = 0; counter < levelMap.get(difficultyLevel); counter++) {
			int maxScore = 0;
			int goalRow = -1;
			int goalCol = -1;
			for (int i = 1; i <= boardSize; i++) {
				for (int j = 1; j <= boardSize; j++) {
					if (board[i][j] == Stone.EMPTY && scoreTable[i][j] > maxScore) {
						maxScore = scoreTable[i][j];
						goalRow = i;
						goalCol = j;
					}
				}
			}
			int[] moveCoord = new int[2];
			moveCoord[0] = goalRow;
			moveCoord[1] = goalCol;
			moveList.add(moveCoord);
			scoreTable[goalRow][goalCol] = -1;
		}
		return moveList;
	}
	
	/**
	 * 
	 * The computer move algorithm was adapted from Chess class line 93 to 337 in a post by ccnuacmhdu
	 * on 2018-10-20 in a CSDN blog here: https://blog.csdn.net/ccnuacmhdu/article/details/83152946
	 * Because the google translated link will automatically redirect to home page, 
	 * a translated page url will not be provided here.
	 * @param board
	 * @param boardSize
	 * @return
	 */
	private int[][] calculateScores(Stone[][] board, int boardSize) {
		int[][] score = new int[boardSize + 1][boardSize + 1];
		for (int i = 0; i <= boardSize; i++) {
			for (int j = 0; j <= boardSize; j++) {
				score[i][j] = 0;
			}
		}

		int numOfBlack = 0;
		int numOfWhite = 0;
		int tempScore = 0;
		
		//left to right (covers all horizontal tuples)
		for (int i = 1; i <= boardSize; i++) {
			for (int j = 1; j <= boardSize - 4; j++) {
				int k = j;
				while (k < j + 5) {
					if (board[i][k] == Stone.BLACK) numOfBlack++;
					else if (board[i][k] == Stone.WHITE) numOfWhite++;
					k++;
				}
				tempScore = tupleScore(numOfBlack, numOfWhite);
				for (k = j; k < j + 5; k++) {
					score[i][k] += tempScore;
				}
				numOfBlack = 0;
				numOfWhite = 0;
				tempScore = 0;
			}
		}
		
		// top to bottom (covers all vertical tuples)
		for (int i = 1; i <= boardSize; i++) {
			for (int j = 1; j <= boardSize - 4; j++) {
				int k = j;
				while (k < j + 5) {
					if (board[k][i] == Stone.BLACK) numOfBlack++;
					else if (board[k][i] == Stone.WHITE) numOfWhite++;
					k++;
				}
				tempScore = tupleScore(numOfBlack, numOfWhite);
				for (k = j; k < j + 5; k++) {
					score[k][i] += tempScore;
				}
				numOfBlack = 0;
				numOfWhite = 0;
				tempScore = 0;
			}
		}
		
		// top left to bottom right (covers all diagonal tuples in bottom left triangle)
		for (int i = 1 ; i <= boardSize - 4; i++) {
			for (int j = 1, k = i; j <= boardSize - 4 && k <= boardSize - 4; j++, k++) {
				int m = k;
				int n = j;
				while (m < k + 5 && k <= boardSize - 4) {
					if (board[m][n] == Stone.BLACK) numOfBlack++;
					else if (board[m][n] == Stone.WHITE) numOfWhite++;
					m++;
					n++;
				}
				if (m == k + 5) {
					tempScore = tupleScore(numOfBlack, numOfWhite);
					for (m = k, n = j; m < k + 5; m++, n++) {
						score[m][n] += tempScore;
					}
				}
				numOfBlack = 0;
				numOfWhite = 0;
				tempScore = 0;
			}
		}
		
		// bottom right to top left (covers all diagonal tuples in top right triangle)
		for (int i = boardSize - 1 ; i > 4; i--) {
			for (int j = boardSize, k = i; j > 4 && k > 4; j--, k--) {
				int m = k;
				int n = j;
				while (m > k - 5 && k > 4) {
					if (board[m][n] == Stone.BLACK) numOfBlack++;
					else if (board[m][n] == Stone.WHITE) numOfWhite++;
					m--;
					n--;
				}
				if (m == k - 5) {
					tempScore = tupleScore(numOfBlack, numOfWhite);
					for (m = k, n = j; m > k - 5; m--, n--) {
						score[m][n] += tempScore;
					}
				}
				numOfBlack = 0;
				numOfWhite = 0;
				tempScore = 0;
			}
		}
		
		// bottom left to top right (covers all diagonal tuples in top left triangle)
		for (int i = boardSize; i > 4; i--) {
			for (int j = 1, k = i; j <= boardSize - 4 && k > 4; j++, k--) {
				int m = k;
				int n = j;
				while (m > k - 5 && k > 4) {
					if (board[m][n] == Stone.BLACK) numOfBlack++;
					else if (board[m][n] == Stone.WHITE) numOfWhite++;
					m--;
					n++;
				}
				if (m == k - 5) {
					tempScore = tupleScore(numOfBlack, numOfWhite);
					for (m = k, n = j; m > k - 5; m--, n++) {
						score[m][n] += tempScore;
					}
				}
				numOfBlack = 0;
				numOfWhite = 0;
				tempScore = 0;
			}
		}
		
		// top right to bottom left (covers all diagonal tuples in bottom right triangle)
		for (int i = 2 ; i <= boardSize - 4; i++) {
			for (int j = boardSize, k = i; j > 4 && k <= boardSize - 4; j--, k++) {
				int m = k;
				int n = j;
				while (m < k + 5 && k <= boardSize - 4) {
					if (board[m][n] == Stone.BLACK) numOfBlack++;
					else if (board[m][n] == Stone.WHITE) numOfWhite++;
					m++;
					n--;
				}
				if (m == k + 5) {
					tempScore = tupleScore(numOfBlack, numOfWhite);
					for (m = k, n = j; m < k + 5; m++, n--) {
						score[m][n] += tempScore;
					}
				}
				numOfBlack = 0;
				numOfWhite = 0;
				tempScore = 0;
			}
		}
		
		return score;
	} 
	
	private int tupleScore(int numOfBlack, int numOfWhite) {
		if (numOfBlack > 0 && numOfWhite > 0) {
			return 0;
		}
		if (numOfBlack == 0 && numOfWhite == 0) {
			return 7;
		}
		if (numOfBlack == 1) {
			return 15;
		}
		if (numOfBlack == 2) {
			return 400;
		}
		if (numOfBlack == 3) {
			return 1800;
		}
		if (numOfBlack == 4) {
			return 100000;
		}
		if (numOfWhite == 1) {
			return 35;
		}
		if (numOfWhite == 2) {
			return 800;
		}
		if (numOfWhite == 3) {
			return 15000;
		}
		if (numOfWhite == 4) {
			return 800000;
		}
		return -1;
	}
}