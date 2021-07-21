public class HumanPlayer extends Player {
	private int ranking = 0;
	
	public HumanPlayer() {
        super();
    }

    public HumanPlayer(Stone stone) {
        super(stone);
    }

    public HumanPlayer(String playerName, Stone stone) {
        super(playerName, stone);
    }

    @Override
    public Move getMove(GameConfiguration currentConfig, String coord) {
        Move humanMove = currentConfig.isValidMove(coord, getPlayerColor());
        if (humanMove != null) {
            getAllValidMoves().add(humanMove);
        }
        return humanMove;
    }

    public int getRanking() {
        return ranking;
    }

    public void addRanking(int score) {
        this.ranking += score;
    }
}
