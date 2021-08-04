package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import model.*;

public class OnGameController {
	private static final int LINE_SPACING = 40;
	private int boardSize = 15;
	private boolean blackTurn = true;
	private GomokuGUI app;
	private GameConfiguration config;

	@FXML
	private Pane paneBoard;

	@FXML
	private StackPane paneBoardArea;

	void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
		drawBoard();
	}

	void linkWithApplication(GomokuGUI app) {
		this.app = app;
		this.config = app.getGameConfiguration();
		if (app.getPlayerBlack() instanceof ComputerPlayer) {
			firstMove();
		}
	}

	private void drawBoard() {
		int boardLength = LINE_SPACING * (boardSize - 1);
		paneBoard.setMinSize(boardLength, boardLength);
		paneBoard.setPrefSize(boardLength, boardLength);
		paneBoard.setMaxSize(boardLength, boardLength);
		for (int i = 0; i < boardSize; i++) {
			Line hLine = new Line(0, LINE_SPACING * i, boardLength, LINE_SPACING * i);
			Line vLine = new Line(LINE_SPACING * i, 0, LINE_SPACING * i, boardLength);
			paneBoard.getChildren().add(hLine);
			paneBoard.getChildren().add(vLine);
		}
		drawPoint();
	}

	private void drawPoint() {
		int top = 3 * LINE_SPACING;
		int center = Math.round(boardSize / 2) * LINE_SPACING;
		int bottom = (boardSize - 4) * LINE_SPACING;
		if (boardSize < 13) {
			top = 2 * LINE_SPACING;
			bottom = (boardSize - 3) * LINE_SPACING;
		}
		Circle circle1 = new Circle(top, top, 4);
		Circle circle2 = new Circle(bottom, top, 4);
		Circle circle3 = new Circle(center, center, 4);
		Circle circle4 = new Circle(top, bottom, 4);
		Circle circle5 = new Circle(bottom, bottom, 4);
		paneBoard.getChildren().addAll(circle1, circle2, circle3, circle4, circle5);
		if (boardSize > 15) {
			Circle circle6 = new Circle(center, top, 4);
			Circle circle7 = new Circle(top, center, 4);
			Circle circle8 = new Circle(bottom, center, 4);
			Circle circle9 = new Circle(center, bottom, 4);
			paneBoard.getChildren().addAll(circle6, circle7, circle8, circle9);
		}
	}
	
	private void drawStone(Move move) {
		int row = move.getRow() * LINE_SPACING;
		int col = move.getCol() * LINE_SPACING;
		Circle circle = new Circle(row, col, 17.5);
		if (move.getStone() == Stone.BLACK) {
			circle.setStroke(Color.BLACK);
			circle.setFill(Color.BLACK);
		} else {
			circle.setStroke(Color.WHITE);
			circle.setFill(Color.WHITE);
		}
		DropShadow ds = new DropShadow();
		ds.setOffsetX(2.0);
		ds.setOffsetY(2.0);
		circle.setEffect(ds);
		paneBoard.getChildren().add(circle);
	}
	
	@FXML
	private void onNextMove(MouseEvent e) {
		int boardLength = LINE_SPACING * (boardSize - 1);
		double x = e.getX();
		double y = e.getY();
		if (x < 0 || x > boardLength + 20 || y < 0 || y > boardLength + 20) {
			return;
		}
		int xIndex = (int) Math.round(x / LINE_SPACING);
		int yIndex = (int) Math.round(y / LINE_SPACING);
		System.out.println(xIndex + ", " + yIndex);
		try {
			if (blackTurn) {
				nextBlackMove(xIndex, yIndex, app.getPlayerBlack());
			} else {
				nextWhiteMove(xIndex, yIndex, app.getPlayerWhite());
			}
		} catch (InvalidPlacementException ex) {
			
		}
	}
	
	private void firstMove() {
		nextAIMove(app.getPlayerBlack());
	}
	
	private void nextAIMove(Player nextPlayer) {
		if (nextPlayer instanceof ComputerPlayer) {
			Move move = nextPlayer.getMove(config);
			nextPlayer.getAllValidMoves().add(move);
			nextPlayer.incrementMoveCount();
			placeMove(move, nextPlayer);
		}
	}

	private void nextBlackMove(int x, int y, Player playerBlack) throws InvalidPlacementException {
		Move blackMove = playerBlack.getMove(config, x, y);
		if (blackMove != null) {
			placeMove(blackMove, playerBlack);
			nextAIMove(app.getPlayerWhite());
		} else {
			throw new InvalidPlacementException("Invalid move, please try again");
		}
	}

	private void nextWhiteMove(int x, int y, Player playerWhite) throws InvalidPlacementException {
		Move whiteMove = playerWhite.getMove(config, x, y);
		if (whiteMove != null) {
			placeMove(whiteMove, playerWhite);
			nextAIMove(app.getPlayerBlack());
		} else {
			throw new InvalidPlacementException("Invalid move, please try again");
		}
	}
	
	private void placeMove(Move move, Player currentPlayer) {
		currentPlayer.getAllValidMoves().add(move);
		currentPlayer.incrementMoveCount();
		config.updateBoard(move);
		drawStone(move);
		Result roundResult = config.checkWinningLine(move);
		if (roundResult == Result.CONTINUE) {
			blackTurn = !blackTurn;
		} else {
			app.gameOver(roundResult);
		}
	}

	@FXML
	private void onExitGame(ActionEvent event) {
		app.exitGame();
	}

	@FXML
	private void onRestart(ActionEvent event) {
		app.restartGame();
	}

	@FXML
	private void initialize() {
		assert paneBoard != null : "fx:id=\"paneBoard\" was not injected: check your FXML file 'GameView.fxml'.";
		assert paneBoardArea != null
				: "fx:id=\"paneBoardArea\" was not injected: check your FXML file 'GameView.fxml'.";
	}
}
