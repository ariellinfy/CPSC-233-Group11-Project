package model;

import java.util.ArrayList;
import java.util.Random;

/**
 * An abstract parent class for HumanPlayer and ComputerPlayer classes.
 * 
 * @author Fu-Yin Lin
 * 
 */
public abstract class Player {
	private String playerName;
	private Stone playerColor;
	private int numOfMoves = 0;
	private ArrayList<Move> validMoveList = new ArrayList<Move>();

	/**
	 * Default constructor when player data is unknown, the player will be randomly
	 * assigned a string of literals as playerName.
	 */
	public Player() {
		this.playerName = generateName();
	}

	/**
	 * Constructor with only player color provided, a random string of literals will
	 * be assigned as playerName.
	 * 
	 * @param color the stone color that the player will be playing in this game.
	 */
	public Player(Stone color) {
		this.playerName = generateName();
		this.playerColor = color;
	}

	/**
	 * Constructor with only the player name provided.
	 * 
	 * @param playerName user-defined player name, any string is considered a valid
	 *                   parameter.
	 */
	public Player(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * Constructor with both player name and color known.
	 * 
	 * @param playerName user-defined player name, any string is considered a valid
	 *                   parameter.
	 * @param color      the stone color that the player will be playing in this
	 *                   game.
	 */
	public Player(String playerName, Stone color) {
		this.playerName = playerName;
		this.playerColor = color;
	}

	/**
	 * A helper method that uses StringBuilder and Random class to generate a string
	 * of length 12.
	 * 
	 * @return a string of length 12, where first 8 characters are randomly chosen
	 *         from lowercase alphabets in the ASCII table, and the last 4
	 *         characters are randomly selected from numeric numbers in the ASCII
	 *         table.
	 */
	private String generateName() {
		StringBuilder randomName = new StringBuilder();
		Random rand = new Random();
		for (int i = 0; i < 12; i++) {
			if (i < 8) {
				int letter = rand.nextInt(26);
				randomName.append((char) (letter + 97));
			} else {
				int number = rand.nextInt(10);
				randomName.append((char) (number + 48));
			}
		}
		return randomName.toString();
	}

	/**
	 * Get the name of this player.
	 * 
	 * @return the name of current player.
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Set the name of this player.
	 * 
	 * @param playerName user-defined player name, any string is considered a valid
	 *                   parameter.
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * Get the stone color of this player.
	 * 
	 * @return Stone color of the current player.
	 */
	public Stone getPlayerColor() {
		return playerColor;
	}

	/**
	 * Set the stone color of this player.
	 * 
	 * @param color the stone color that the player will be playing in this game.
	 */
	public void setPlayerColor(Stone color) {
		this.playerColor = color;
	}

	/**
	 * Get the current total number of valid moves of this player.
	 * 
	 * @return the current number of valid moves that the player has taken up until
	 *         this point in the game.
	 */
	public int getNumOfMoves() {
		return numOfMoves;
	}

	/**
	 * Every time a player makes a valid move, the total number of moves of this
	 * player will increment by one.
	 */
	public void incrementMoveCount() {
		this.numOfMoves++;
	}

	/**
	 * This method will be overridden by subclass.
	 * 
	 * @param currentConfig current game configuration.
	 * @param coord         a string representation of the targeting location of a
	 *                      move.
	 * @return null when player type (ComputerPlayer or HumanPlayer) is not
	 *         specified.
	 */
	public Move getMove(GameConfiguration currentConfig, String coord) {
		return null;
	}

	/**
	 * This method will be overridden by subclass. Overloading the previous getMove
	 * method.
	 * 
	 * @param currentConfig current game configuration.
	 * @param row           the row index number of the board.
	 * @param col           the column index number of the board.
	 * @return null when player type (ComputerPlayer or HumanPlayer) is not
	 *         specified.
	 */
	public Move getMove(GameConfiguration currentConfig, int row, int col) {
		return null;
	}

	/**
	 * This method will be overridden by subclass. Overloading the previous getMove
	 * method.
	 * 
	 * @param currentConfig current game configuration.
	 * @return null when player type (ComputerPlayer or HumanPlayer) is not
	 *         specified.
	 */
	public Move getMove(GameConfiguration currentConfig) {
		return null;
	}

	/**
	 * Get a list of all valid moves of this player.
	 * 
	 * @return a list that includes all valid moves taken by this player at this
	 *         time of the game.
	 */
	public ArrayList<Move> getAllValidMoves() {
		return validMoveList;
	}

	/**
	 * Customize toString method by overriding the default method inherited from
	 * Object class.
	 * 
	 * @return a string representation of this player.
	 */
	@Override
	public String toString() {
		StringBuilder playerInfo = new StringBuilder();
		playerInfo.append("Player name: ");
		playerInfo.append(playerName);
		playerInfo.append("; Color: ");
		playerInfo.append(playerColor);
		playerInfo.append("; Number of moves: ");
		playerInfo.append(numOfMoves);
		return playerInfo.toString();
	}

	/**
	 * Testing constructors and methods in Player class
	 */
//	public static void main(String[] args) {
//		Player p1 = new Player();
//		System.out.println(p1);
//		Player p2 = new Player(Stone.BLACK);
//		System.out.println(p2);
//		Player p3 = new Player("abc123");
//		System.out.println(p3);
//		Player p4 = new Player("xyz789", Stone.WHITE);
//		System.out.println(p4);
//		System.out.println(p1.getPlayerName());
//		p1.setPlayerName("dfg456");
//		System.out.println(p1.getPlayerName());
//		System.out.println(p3.getPlayerColor());
//		p3.setPlayerColor(Stone.WHITE);
//		System.out.println(p3.getPlayerColor());
//		System.out.println(p2.getNumOfMoves());
//		p2.incrementMoveCount();
//		System.out.println(p2.getNumOfMoves());
//		System.out.println(p4.getAllValidMoves());
//		Move tempMove = new Move(2, 8, Stone.BLACK);
//		p4.getAllValidMoves().add(tempMove);
//		p4.getAllValidMoves().add(tempMove);
//		System.out.println(p4.getAllValidMoves());
//	}
}