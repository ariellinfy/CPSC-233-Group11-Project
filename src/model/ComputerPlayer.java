package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * The class that contains data and logic related to the artificial intelligence
 * of the game.
 * 
 * @author Fu-Yin Lin
 * 
 */
public class ComputerPlayer extends Player {
	private static final HashMap<Level, Integer> levelMap = new HashMap<Level, Integer>();
	private Level difficultyLevel = Level.MEDIUM;
	private int[] bestMove = new int[2];

	/**
	 * Constructor with only difficulty level defined. Set the computer player
	 * default name to be "AI".
	 * 
	 * @param difficultyLevel the difficulty level of the computer moves chosen by
	 *                        the user in this game (ie. how smart the computer
	 *                        moves).
	 */
	public ComputerPlayer(Level difficultyLevel) {
		super("AI");
		this.difficultyLevel = difficultyLevel;
		initLevelMap();
	}

	/**
	 * Constructor with both stone color and difficulty known. Set the computer
	 * player default name to be "AI".
	 * 
	 * @param color           the stone color that the computer player will be
	 *                        playing in this game.
	 * @param difficultyLevel the difficulty level of the computer moves chosen by
	 *                        the user in this game (ie. how smart the computer
	 *                        moves).
	 */
	public ComputerPlayer(Stone color, Level difficultyLevel) {
		super("AI", color);
		this.difficultyLevel = difficultyLevel;
		initLevelMap();
	}

	/**
	 * A helper method that will be called when a ComputerPlayer object is
	 * initialized. It restricts the number of possible moves that the computer can
	 * randomly select from. For example, the hardest level only has one value
	 * available in the score table and that is the one with the highest overall
	 * score.
	 */
	private void initLevelMap() {
		levelMap.put(Level.EASY, 1);
		levelMap.put(Level.MEDIUM, 2);
		levelMap.put(Level.HARD, 3);
	}

	/**
	 * Get the difficulty level of this computer player.
	 * 
	 * @return the level of difficulty of the current computer player.
	 */
	public Level getDifficultyLevel() {
		return difficultyLevel;
	}

	
	private Map<Integer, Character> alphabetList = new HashMap<Integer, Character>();
	
	/**
	 * Depending on the difficulty level set by the user, this method determines the
	 * next computer move by using both generateMoveList and calculateScores helper
	 * methods to generate a list of possible moves ranked in descending order and
	 * randomly picks a move from the list.
	 * 
	 * @param currentConfig current game configuration used to access current board
	 *                      data.
	 * @return the computer player's next move in the current game.
	 */
	@Override
	public Move getMove(GameConfiguration currentConfig) {
		Stone[][] board = currentConfig.getChessBoard().getBoard();
		this.alphabetList = currentConfig.getChessBoard().getAlphabetList();
		int depth = levelMap.get(difficultyLevel);
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		alphaBeta(board, depth, alpha, beta, getPlayerColor());
		return new Move(bestMove[0], bestMove[1], getPlayerColor());
	}

	private class Point {
		int x, y, weight;
		private Point(int x, int y, int weight) {
			this.x = x;
			this.y = y;
			this.weight = weight;
		}
		int getWeight() {
			return weight;
		}
	}

	private int alphaBeta(Stone[][] board, int depth, int alpha, int beta, Stone color) {
		int score = evaluate(board, color);
		if (depth <= 0 || Math.abs(score) >= 5000000) {
			return score;
		}
		System.out.println("new alpha beta, " + score + ", " + depth + ", " + color);
		int value = color == Stone.BLACK ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		ArrayList<Point> availablePoints = sortedAvailablePoints(board);
//		System.out.print(availablePoints.size() + ", ");
//		for (Point point : availablePoints) {
//			Stone opponentColor = color == Stone.BLACK? Stone.WHITE : Stone.BLACK;
//			board[point.x][point.y] = color;
//			score = -alphaBeta(board, depth - 1, -beta, -alpha, opponentColor);
//			System.out.println(String.valueOf(alphabetList.get(point.y + 1)) + (point.x + 1) + ", " + score);
//			board[point.x][point.y] = Stone.EMPTY;
//			if (score > alpha) {
//				alpha = score;
//				bestMove[0] = point.x;
//				bestMove[1] = point.y;
//				if (alpha >= beta) {
//					break;
//				}
//			}
//		}
//		return alpha;
		if (color == Stone.BLACK) {
			for (Point point : availablePoints) {
				int i = point.x;
				int j = point.y;
				board[i][j] = Stone.BLACK;
				score = alphaBeta(board, depth - 1, alpha, beta, Stone.WHITE);
				if (score > value) {
					value = score;
					bestMove[0] = i;
					bestMove[1] = j;
				}
				board[i][j] = Stone.EMPTY;
				alpha = Math.max(alpha, score);
				if (alpha >= beta) {
					break;
				}
				value = alpha;
			}
		} else if (color == Stone.WHITE) {
			for (Point point : availablePoints) {
				int i = point.x;
				int j = point.y;
				board[i][j] = Stone.WHITE;
				score = alphaBeta(board, depth - 1, alpha, beta, Stone.BLACK);
				if (score < value) {
					value = score;
					bestMove[0] = i;
					bestMove[1] = j;
				}
				board[i][j] = Stone.EMPTY;
				beta = Math.min(beta, score);
				if (beta <= alpha) {
					break;
				}
				value = beta;
			}
		}
		return value;
		// every empty and nearby sorted point
		// evaluate
		// return max
	}

	private ArrayList<Point> sortedAvailablePoints(Stone[][] board) {
		ArrayList<Point> availablePoints = emptyPoints(board);
		Collections.sort(availablePoints, Comparator.comparing(Point::getWeight).reversed());
//		for (Point point : availablePoints) {
//			System.out.println(point.x + ", " + point.y + ": " + point.weight);
//		}
		return availablePoints;
	}

	private ArrayList<Point> emptyPoints(Stone[][] board) {
		ArrayList<Point> emptyPoints = new ArrayList<Point>();
		int[][] pointWeights = initScoreTable(board.length);
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == Stone.EMPTY && nearBy(board, i, j, 1)) {
					Point point = new Point(i, j, pointWeights[i][j]);
					emptyPoints.add(point);
				}
			}
		}
		return emptyPoints;
	}

	private boolean nearBy(Stone[][] board, int x, int y, int radius) {
		int boardBoundary = board.length - 1;
		int startX = Integer.max(x - radius, 0), endX = Integer.min(x + radius, boardBoundary);
		int startY = Integer.max(y - radius, 0), endY = Integer.min(y + radius, boardBoundary);
		for (int i = startX; i <= endX; i++) {
			for (int j = startY; j <= endY; j++) {
				if (board[i][j] != Stone.EMPTY) {
					return true;
				}
			}
		}
		return false;
	}

	private int evaluate(Stone[][] board, Stone playerColor) {
		int[][] pointWeights = initScoreTable(board.length);
		int myScore = 0;
		int opponentScore = 0;
		Stone opponentColor = playerColor == Stone.BLACK ? Stone.WHITE : Stone.BLACK;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == playerColor) {
					myScore += evaluateSinglePoint(board, i, j, playerColor, pointWeights[i][j]);
				} else if (board[i][j] == opponentColor) {
					opponentScore += evaluateSinglePoint(board, i, j, opponentColor, pointWeights[i][j]);
				}
			}
		}
		return (10 * myScore) - opponentScore;
		// int score for ai
		// int score for enemy
		// go through all points in the board (exclude empty)
		// if my point, evaluate and add to my array
		// else evaluate and add to enemy array
		// return my score - enemy score
	}

	private int evaluateSinglePoint(Stone[][] board, int x, int y, Stone color, int weight) {
		int[][] DIRECTION = { { 0, -1, 0, 1 }, { -1, 0, 1, 0 }, { -1, -1, 1, 1 }, { -1, 1, 1, -1 } };
		int score = 0;
		ArrayList<ChessType> typesInDirections = new ArrayList<ChessType>();
		for (int dir = 0; dir < 4; dir++) {
			String line = "";
			int rStart = Integer.max(x + DIRECTION[dir][0] * 4, 0);
			int cStart = Integer.max(y + DIRECTION[dir][1] * 4, 0);
			int rDir = DIRECTION[dir][2];
			int cDir = DIRECTION[dir][3];
			int rEnd = Integer.min(x + rDir, board.length - 1);
			int cEnd = Integer.min(y + cDir, board.length - 1);
			int r = rStart;
			int c = cStart;
			while (r <= rEnd && c <= cEnd) {
				// if point == color, gives 1
				// if point == empty, gives 0
				// else gives 2
				if (board[r][c] == color)
					line += 1;
				else if (board[r][c] == Stone.EMPTY)
					line += 0;
				else
					line += 2;
				r += rDir;
				c += cDir;
			}
			ChessType[] allTypes = { ChessType.WIN5, ChessType.LIVE4, ChessType.SLEEP4, ChessType.LIVE3,
					ChessType.JUMPLIVE3, ChessType.SLEEP3, ChessType.LIVE2, ChessType.SLEEP2 };
			for (ChessType type : allTypes) {
				if (type == checkType(line)) {
					typesInDirections.add(type);
				}
			}
		}
		// Sum all type scores
		for (ChessType type : typesInDirections) {
			score += getScore(type);
		}
		return score * weight;
	}

	/**
	 * Initiate a 2D array with initial score, higher score is given toward the
	 * center of the board. The array size is the same size as the board of the
	 * game.
	 * 
	 * @param boardSize the size of the board.
	 * @return a score table with initial score based on each coord's position with
	 *         respective to the board border.
	 */
	private int[][] initScoreTable(int boardSize) {
		int[][] scoreTable = new int[boardSize][boardSize];
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				int mid = boardSize / 2;
				int min = 0;
				int maxIdx = boardSize - 1;
				if (i <= mid && j <= mid) {
					min = i < j ? i : j;
				} else if (i > mid && j <= mid) {
					min = maxIdx - i < j ? maxIdx - i : j;
				} else if (i <= mid && j > mid) {
					min = i < maxIdx - j ? i : maxIdx - j;
				} else {
					min = maxIdx - i < maxIdx - j ? maxIdx - i : maxIdx - j;
				}
				scoreTable[i][j] = min;
			}
		}
		return scoreTable;
	}

	enum ChessType {
		WIN5, LIVE4, SLEEP4, LIVE3, JUMPLIVE3, SLEEP3, LIVE2, SLEEP2, NONE
	}

	private ChessType checkType(String target) {
		ChessType type = ChessType.NONE;
		switch (target) {
		case "11111":
			type = ChessType.WIN5;
			break;
		case "011110":
			type = ChessType.LIVE4;
			break;
		case "211110":
		case "011112":
		case "11011":
		case "10111":
		case "11101":
			type = ChessType.SLEEP4;
			break;
		case "011100":
		case "001110":
			type = ChessType.LIVE3;
			break;
		case "011010":
		case "010110":
			type = ChessType.JUMPLIVE3;
			break;
		case "211100":
		case "001112":
		case "210110":
		case "011012":
		case "211010":
		case "010112":
		case "10101":
		case "10011":
		case "11001":
			type = ChessType.SLEEP3;
			break;
		case "001100":
		case "001010":
		case "010100":
		case "010010":
			type = ChessType.LIVE2;
			break;
		case "211000":
		case "000112":
		case "210100":
		case "001012":
		case "210010":
		case "010012":
		case "201100":
		case "001102":
		case "200110":
		case "011002":
		case "201010":
		case "010102":
		case "10001":
			type = ChessType.SLEEP2;
			break;
		}
		return type;
	}

	private int getScore(ChessType type) {
		int score = 0;
		switch (type) {
		case WIN5:
			score = 5000000;
			break;
		case LIVE4:
			score = 100000;
			break;
		case SLEEP4:
			score = 10000;
			break;
		case LIVE3:
			score = 8000;
			break;
		case JUMPLIVE3:
			score = 7000;
			break;
		case SLEEP3:
			score = 500;
			break;
		case LIVE2:
			score = 50;
			break;
		case SLEEP2:
			score = 10;
			break;
		case NONE:
		default:
			break;
		}
		return score;
	}
}