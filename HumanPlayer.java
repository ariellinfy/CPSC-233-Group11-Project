public class HumanPlayer extends Player {
	private int ranking = 0;
	
	/**
	 * Default constructor that creates a HumanPlayer object with no variables initialized.
	 */
	public HumanPlayer() {
		/*
		 *	super() is used to invoke the default constructor (no parameters) inherited
		 *	from the parent class Player. Additionally, this method is used in a similar
		 *	fashion for all remaining constructors in the HumanPlayer class.
		 */
        super();
    }
	
	/**
	 * Constructor that creates a HumanPlayer object with instance variable
	 * "stoneColor" initialized (inherited from Player class).
	 * @param stone a Stone object containing the color of the Go stone for
	 * specified player.
	 */
    public HumanPlayer(Stone stone) {
        super(stone);
    }
    
    /**
     * Constructor that creates a HumanPlayer object with instance variables
     * "playerName" and "stoneColor" initialized.
     * @param playerName the name of the specified player entered by the user.
     * @param stone a Stone object containing the color of the Go stone for
     * specified player.
     */
    public HumanPlayer(String playerName, Stone stone) {
        super(playerName, stone);
    }
    
    /**
     * Method that validates a move made by the player and adds it to the instance variable
     * "validMoveList" of type ArrayList<Move>.
     * @param currentConfig a GameConfiguration object that contains the current game board.
     * @param coord the coordinate of the move made by the player. 
     * @return humanMove a Move object containing the coordinates and color of the stone.
     */
    @Override
    public Move getMove(GameConfiguration currentConfig, String coord) {
    	/*
    	 *	isValidMove() method is called from currentConfig to validate the move
    	 *	made by the player. If the move is not valid, a null Move object is returned.
    	 */
        Move humanMove = currentConfig.isValidMove(coord, getStoneColor());
        if (humanMove != null) {
        	//	If the move is valid, it is added to ArrayList<Move> validMoveList.
            getAllValidMoves().add(humanMove);
        }
        return humanMove;
    }
    
    /**
     * Getter method that returns the value of instance variable "ranking".
     * @return ranking the current combined score of specified player for the 
     * current session.
     */
    public int getRanking() {
        return ranking;
    }
    
    /**
     * Method used to update the value of instance variable "ranking".
     * @param score the score to be added to the specified player's total ranking.
     */
    public void addRanking(int score) {
        this.ranking += score;
    }
}
