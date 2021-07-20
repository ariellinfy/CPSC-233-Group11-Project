import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * @author Fu-Yin Lin
 *
 */
public class Player {
	private String playerName;
	private Stone stoneColor;
	private int numOfMoves;
	private ArrayList<Move> validMoveList = new ArrayList<Move>();
	
	public Player() {
		this.playerName = generateName();
	}
	
	public Player(Stone color) {
		this.playerName = generateName();
		this.stoneColor = color;
	}
	
	public Player(String playerName) {
		this.playerName = playerName;
	}
	
	public Player(String playerName, Stone color) {
		this.playerName = playerName;
		this.stoneColor = color;
	}
	
	private String generateName() {
		String randomName = "";
		Random rand = new Random();
		for (int i = 0; i < 12; i++) {
			if (i < 8) {
				int letter = rand.nextInt(26);
				randomName += (char)(letter + 97);
			} else {
				int number = rand.nextInt(10);
				randomName += (char)(number + 48);
			}
		}
		return randomName;
	}
	
	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public Stone getStoneColor() {
		return stoneColor;
	}
	
	public void setStoneColor(Stone color) {
		this.stoneColor = color;
	}
	
	public int getNumOfMoves() {
		return numOfMoves;
	}
	
	public void incrementMoveCount() {
		this.numOfMoves++;
	}

	public Move getMove(GameConfiguration currentConfig, String coord, Stone stone) {
		return null;
	}
	
	public ArrayList<Move> getAllValidMoves() {
		return validMoveList;
	}
}