package application;

import java.util.ArrayList;
import java.util.Map;

import javafx.beans.binding.Bindings;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.time.Duration;
import model.*;

/**
 * A view that displays the game board and on game menu. It handles
 * board-related events such as stone placements as well as allows user to
 * select draw, undo, quit or start new game options.
 * 
 * @author Fu-Yin Lin, Justin Chua
 *
 */
public class OnGameController {
	private static final int LINE_SPACING = 40;
	private int boardSize = 15;
	private boolean blackTurn = true;
	private GomokuGUI app;
	private GameConfiguration config;
	private Timeline blackTimeline = new Timeline();
	private Timeline whiteTimeline = new Timeline();

	@FXML
	private BorderPane borderPaneArea;

	// A layout container containing both board image and grids.
	@FXML
	private StackPane paneBoardArea;

	// A layout container containing the grid game board.
	@FXML
	private Pane paneBoard;

	@FXML
	private ScrollPane scrollPaneMoveLogs;

	@FXML
	private GridPane gridPaneMoveLogs;

	@FXML
	private Label labelBlack;

	@FXML
	private Label labelWhite;

	@FXML
	private Label labelBlackTime;

	@FXML
	private Label labelWhiteTime;

	@FXML
	private Label labelBlackName;

	@FXML
	private Label labelWhiteName;

	@FXML
	private Label labelStatusMessage;

	@FXML
	private Button buttonUndo;

	@FXML
	private Button muteButton;

	@FXML
	private ImageView volumeImage;

	/**
	 * Set boardSize based on user selected size in the start menu.
	 * 
	 * @param boardSize the specified board size selected by the user.
	 */
	void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}

	/**
	 * Method used to link this controller with the GomokuGUI. It also initializes
	 * all the settings from configuration data.
	 * 
	 * @param app the instance of GomokuGUI when running the program.
	 */
	void linkWithApplication(GomokuGUI app) {
		this.app = app;
		this.config = app.getGameConfiguration();
		// Update image for background music button.
		if (app.getBackgroundPlayer().getStatus() == MediaPlayer.Status.STOPPED) {
			volumeImage.setImage(new Image(new File("src/resources/Volume-Muted-Icon.png").toURI().toString()));
		} else {
			volumeImage.setImage(new Image(new File("src/resources/Volume-Icon.png").toURI().toString()));
		}
		// Update player labels with player names.
		labelBlackName.setText(app.getPlayerBlack().getPlayerName());
		labelWhiteName.setText(app.getPlayerWhite().getPlayerName());
		// Set welcome message
		labelStatusMessage.setText("Welcome, black's turn!");
		// Enable undo button by user choice.
		if (config.getUndo()) {
			buttonUndo.setDisable(false);
		}
		// Draw the game board.
		drawBoard();
		// Setup timelines for both players.
		setupCountdownTimer(blackTimeline, labelBlackTime);
		setupCountdownTimer(whiteTimeline, labelWhiteTime);
		// Start black timer.
		blackTimerContinue();
		// If playerBlack is of type ComputerPlayer, it will place first move.
		if (app.getPlayerBlack() instanceof ComputerPlayer) {
			firstMove();
		}
	}

	/**
	 * Setup a countdown timer based on user selected game time. Referenced from:
	 * https://asgteach.com/2011/10/javafx-animation-and-binding-simple-countdown-timer-2/
	 * https://www.coder.work/article/5519239
	 * 
	 * @param timeline the timeline to be setup with a new 1 second keyframe.
	 * @param label    the label to be binded with the timeLeft variable.
	 */
	private void setupCountdownTimer(Timeline timeline, Label label) {
		ObjectProperty<Duration> timeLeft = new SimpleObjectProperty<>();
		timeLeft.set(Duration.ofMinutes(config.getGameTime()));
		// Time label is binded to actual time left
		label.textProperty()
				.bind(Bindings.createStringBinding(() -> getTimeStringFromDuration(timeLeft.get()), timeLeft));
		/*
		 * Set the timeline with a keyframe so that every second it will update to
		 * reflect the correct timeLeft variable.
		 */
		timeline.getKeyFrames().add(new KeyFrame(javafx.util.Duration.seconds(1), e -> updateTimer(timeLeft)));
		timeline.setCycleCount(Timeline.INDEFINITE);
	}

	/**
	 * The onFinished function that will be invoked every one second to decrement
	 * the time by 1 second. If timeLeft reaches zero, the timeline will stop
	 * invoking its keyframes and app will show the termination view.
	 * 
	 * @param timeLeft the wrapped Duration that reflects the actual time left for a
	 *                 player.
	 */
	private void updateTimer(ObjectProperty<Duration> timeLeft) {
		timeLeft.set(timeLeft.get().minusSeconds(1));
		if (timeLeft.get().isZero()) {
			if (blackTurn) {
				blackTimeline.stop();
				app.gameOver(Result.WHITE);
			} else {
				whiteTimeline.stop();
				app.gameOver(Result.BLACK);
			}
		}
	}

	/**
	 * This method translates timeLeft duration into readable string representation.
	 * 
	 * @param duration the Duration object that contains information about the time
	 *                 left for a player.
	 * @return a time string representation of the duration.
	 */
	private String getTimeStringFromDuration(Duration duration) {
		int min = (int) (duration.getSeconds() / 60);
		int sec = (int) (duration.getSeconds() % 60);
		String time = sec < 10 ? min + ":0" + sec : min + ":" + sec;
		return time;
	}

	/**
	 * A generic method that will play/continue the provided timeline.
	 * 
	 * @param timeline the timeline to be played.
	 */
	private void continueTimer(Timeline timeline) {
		timeline.play();
	}

	/**
	 * A generic method that will stop/pause the provided timeline.
	 * 
	 * @param timeline the timeline to be stop.
	 */
	private void pauseTimer(Timeline timeline) {
		timeline.stop();
	}

	/**
	 * This method is invoked when it's black's turn, where black timer will
	 * continue and white timer will be paused.
	 */
	private void blackTimerContinue() {
		continueTimer(blackTimeline);
		pauseTimer(whiteTimeline);
	}

	/**
	 * This method is invoked when it's white's turn, where white timer will
	 * continue and black timer will be paused.
	 */
	private void whiteTimerContinue() {
		continueTimer(whiteTimeline);
		pauseTimer(blackTimeline);
	}

	/**
	 * Stop and clear all keyframes in a timeline.
	 * 
	 * @param timeline the timeline to be stop and cleared.
	 */
	private void clearTimer(Timeline timeline) {
		timeline.stop();
		timeline.getKeyFrames().clear();
	}

	/**
	 * Stop and clear both black and white timers, also unbind labels (it is due the
	 * stop method is an asynchronous call, unbinding will force the animation to
	 * stop changing immediately).
	 */
	private void stopAllTimers() {
		labelBlackTime.textProperty().unbind();
		labelWhiteTime.textProperty().unbind();
		clearTimer(blackTimeline);
		clearTimer(whiteTimeline);
	}

	/**
	 * Method that is responsible for drawing the grid game board in the OnGame
	 * scene.
	 */
	private void drawBoard() {
		int boardLength = LINE_SPACING * (boardSize - 1);
		/*
		 * Manually set the board size, so the pane can fit different board size
		 * dynamically.
		 */
		paneBoard.setMinSize(boardLength, boardLength);
		paneBoard.setPrefSize(boardLength, boardLength);
		paneBoard.setMaxSize(boardLength, boardLength);
		// Draw horizontal/vertical lines to pane to produce the grid game board.
		for (int i = 0; i < boardSize; i++) {
			Line hLine = new Line(10, LINE_SPACING * i + 10, boardLength + 10, LINE_SPACING * i + 10);
			Line vLine = new Line(LINE_SPACING * i + 10, 10, LINE_SPACING * i + 10, boardLength + 10);
			paneBoard.getChildren().add(hLine);
			paneBoard.getChildren().add(vLine);
		}
		// Add decoration to board: board image, coords and dots.
		Rectangle rectangle = new Rectangle(0, 0, boardLength + 75, boardLength + 75);
		rectangle.setArcWidth(15);
		rectangle.setArcHeight(15);
		Image imgBoard = new Image("file:src/resources/Game-Board.jpg");
		ImagePattern pattern = new ImagePattern(imgBoard);
		rectangle.setFill(pattern);
		rectangle.setEffect(new DropShadow(20, Color.BLACK));
		paneBoardArea.getChildren().add(rectangle);
		paneBoard.toFront(); // Bring grids to the front.
		drawCoord(); // Draw board coordinates.
		drawPoint(); // Draw board dots.
	}

	/**
	 * Draw the horizontal and vertical coordinates to the pane.
	 */
	private void drawCoord() {
		Map<Integer, Character> alphabetList = config.getChessBoard().getAlphabetList();
		for (int i = 0; i < boardSize; i++) {
			Text textX = new Text();
			textX.setFont(new Font(16));
			textX.setTextAlignment(TextAlignment.CENTER);
			textX.setWrappingWidth(20);
			textX.setText(alphabetList.get(i + 1).toString());
			textX.setX(i * LINE_SPACING);
			textX.setY(-5);
			Text textY = new Text();
			textY.setFont(new Font(16));
			textY.setTextAlignment(TextAlignment.CENTER);
			textY.setWrappingWidth(40);
			textY.setText(String.valueOf(i + 1));
			textY.setX(-34);
			textY.setY(16 + (i * LINE_SPACING));
			paneBoard.getChildren().addAll(textX, textY);
		}
	}

	/**
	 * Draw a series of dots on the game board. These dots are meant to mimic the 5
	 * or 9 dots commonly found on traditional Gomoku game boards.
	 */
	private void drawPoint() {
		/*
		 * The location of these dots differs depending on the board size selected by
		 * the user.
		 */
		int top = 3 * LINE_SPACING + 10;
		int center = Math.round(boardSize / 2) * LINE_SPACING + 10;
		int bottom = (boardSize - 4) * LINE_SPACING + 10;
		if (boardSize < 13) {
			top = 2 * LINE_SPACING + 10;
			bottom = (boardSize - 3) * LINE_SPACING + 10;
		}
		/*
		 * Regardless of board size, a center dot surrounded by 4 dots diagonally is
		 * drawn on the board.
		 */
		Circle circle1 = new Circle(top, top, 4);
		Circle circle2 = new Circle(bottom, top, 4);
		Circle circle3 = new Circle(center, center, 4);
		Circle circle4 = new Circle(top, bottom, 4);
		Circle circle5 = new Circle(bottom, bottom, 4);
		paneBoard.getChildren().addAll(circle1, circle2, circle3, circle4, circle5);
		/*
		 * For board sizes larger than 15, 4 additional diagonal dots are drawn on the
		 * board.
		 */
		if (boardSize > 15) {
			Circle circle6 = new Circle(center, top, 4);
			Circle circle7 = new Circle(top, center, 4);
			Circle circle8 = new Circle(bottom, center, 4);
			Circle circle9 = new Circle(center, bottom, 4);
			paneBoard.getChildren().addAll(circle6, circle7, circle8, circle9);
		}
	}

	/**
	 * Update the pane with the provided move.
	 * 
	 * @param move the latest move to add to the OnGameView.
	 */
	private void updatePane(Move move) {
		drawStone(move);
		addLog(move);
		scrollPaneMoveLogs.setVvalue(1D);
	}

	/**
	 * Draw the stone on the game board whenever a stone is placed by the
	 * user/computer.
	 * 
	 * @param move a Move object containing the coordinates of where the stone is
	 *             placed.
	 */
	private void drawStone(Move move) {
		// To calculate the x and y distance of the stone with respective to the pane.
		int y = move.getRow() * LINE_SPACING + 10;
		int x = move.getCol() * LINE_SPACING + 10;
		Circle circle = new Circle(x, y, 17.5);
		// Set stone color based on player placed move.
		if (move.getStone() == Stone.BLACK) {
			circle.setStroke(Color.BLACK);
			circle.setFill(Color.BLACK);
		} else {
			circle.setStroke(Color.WHITE);
			circle.setFill(Color.WHITE);
		}
		// Add shadow for added aesthetics.
		DropShadow ds = new DropShadow();
		ds.setOffsetX(2.0);
		ds.setOffsetY(2.0);
		circle.setEffect(ds);
		paneBoard.getChildren().add(circle);
		// Board sound is played before the end of the method.
		app.playBoardSound();
	}

	/**
	 * Remove the last stone drawn to the pane.
	 * 
	 * @param move the undo move.
	 */
	private void removeStone(Move move) {
		paneBoard.getChildren().remove(paneBoard.getChildren().size() - 1);
	}

	/**
	 * Translate the latest move into its corresponding coordinate and add to the
	 * move log in the OnGameView.
	 * 
	 * @param move the latest move to be added to the move log.
	 */
	private void addLog(Move move) {
		Map<Integer, Character> alphabetList = config.getChessBoard().getAlphabetList();
		int row = move.getRow();
		int col = move.getCol();
		int count = app.getPlayerBlack().getNumOfMoves();
		Label numOfMoves = new Label(String.valueOf(count));
		String coord = String.valueOf(alphabetList.get(col + 1)) + (row + 1);
		Label moveLog = new Label(coord);
		if (move.getStone() == Stone.BLACK) {
			gridPaneMoveLogs.add(numOfMoves, 0, count);
			gridPaneMoveLogs.add(moveLog, 1, count);
			gridPaneMoveLogs.getRowConstraints().add(new RowConstraints());
			gridPaneMoveLogs.getRowConstraints().get(count).setMinHeight(30);
			gridPaneMoveLogs.getRowConstraints().get(count).setPrefHeight(30);
			gridPaneMoveLogs.getRowConstraints().get(count).setVgrow(Priority.SOMETIMES);
		} else {
			gridPaneMoveLogs.add(moveLog, 2, count);
		}
	}

	/**
	 * Remove corresponding logs when player undo moves. The latest black move,
	 * latest white move and move count will be remove together in one undo
	 * selection.
	 */
	private void removeLogs() {
		if (blackTurn) {
			int count = app.getPlayerWhite().getNumOfMoves();
			removeNode(count + 1, 2);
			removeNode(count + 1, 1);
			removeNode(count + 1, 0);
		} else {
			int count = app.getPlayerBlack().getNumOfMoves();
			removeNode(count + 1, 1);
			removeNode(count, 2);
			removeNode(count + 1, 0);
		}
	}

	/**
	 * A helper method that will find the corresponding node for the undo move and
	 * remove it from the log.
	 * 
	 * @param row    the row index of the log to be removed.
	 * @param column the column index of the log to be removed.
	 */
	private void removeNode(int row, int column) {
		ObservableList<Node> childrens = gridPaneMoveLogs.getChildren();
		for (Node node : childrens) {
			if (node instanceof Label && GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
				Label coord = (Label) node;
				gridPaneMoveLogs.getChildren().remove(coord);
				return;
			}
		}
	}

	/**
	 * Method that handles the location of mouse clicks made by the user, and passes
	 * these coordinates to stone placement/move related methods.
	 * 
	 * @param e a MouseEvent object that is invoked whenever a mouse click occurs.
	 */
	@FXML
	private void onNextMove(MouseEvent e) {
		int boardLength = LINE_SPACING * (boardSize - 1);
		double x = e.getX();
		double y = e.getY();
		/*
		 * If condition checks to see if the mouse click was valid (i.e. clicked within
		 * the game board). If not, the method is exited using return.
		 */
		if (x < 0 || x > boardLength + 20 || y < 0 || y > boardLength + 20) {
			return;
		}
		/*
		 * The mouse click is rounded to the nearest intersection at which a stone can
		 * be placed.
		 */
		int col = (int) Math.round(x / LINE_SPACING);
		int row = (int) Math.round(y / LINE_SPACING);
		try {
			if (blackTurn) {
				nextBlackMove(row, col, app.getPlayerBlack());
			} else {
				nextWhiteMove(row, col, app.getPlayerWhite());
			}
		} catch (InvalidPlacementException ex) {
			// Update the message label to show user the error message.
			labelStatusMessage.setText(ex.getMessage());
			labelStatusMessage.setTextFill(Color.RED);
		}
	}

	/**
	 * This method is invoked only when the computer is selected to go first by the
	 * user.
	 */
	private void firstMove() {
		nextAIMove(app.getPlayerBlack(), app.getPlayerWhite());
		whiteTimerContinue();
	}

	/**
	 * Method that is responsible for updating the board and move-related instance
	 * variables with the new move made by the Computer.
	 * 
	 * @param nextPlayer    a Player object of the next player.
	 * @param currentPlayer a Player object of the current player.
	 */
	private void nextAIMove(Player nextPlayer, Player currentPlayer) {
		// Checks if "nextPlayer" is of type ComputerPlayer.
		if (nextPlayer instanceof ComputerPlayer) {
			Move move = nextPlayer.getMove(config);
			placeMove(move, nextPlayer, currentPlayer);
			if (nextPlayer.getPlayerColor() == Stone.BLACK) {
				whiteTimerContinue();
			} else {
				blackTimerContinue();
			}
		}
	}

	/**
	 * Method used to handle the next move made by player Black.
	 * 
	 * @param row         the row index of the stone.
	 * @param col         the column index of the stone.
	 * @param playerBlack a Player object for player Black, containing related info
	 *                    such as num of moves/stone color.
	 * @throws InvalidPlacementException if the move is invalid.
	 */
	private void nextBlackMove(int row, int col, Player playerBlack) throws InvalidPlacementException {
		Move blackMove = playerBlack.getMove(config, row, col);
		Player opponent = app.getPlayerWhite();
		if (blackMove != null) {
			placeMove(blackMove, playerBlack, opponent);
			whiteTimerContinue();
			nextAIMove(opponent, playerBlack);
		} else {
			throw new InvalidPlacementException("Invalid move, please try again");
		}
	}

	/**
	 * Method that is used to handle the next move made by player White.
	 * 
	 * @param row         the row index of the stone.
	 * @param col         the column index of the stone.
	 * @param playerWhite a Player object for player White, containing related info
	 *                    such as num of moves/stone color.
	 * @throws InvalidPlacementException if the move is invalid.
	 */
	private void nextWhiteMove(int row, int col, Player playerWhite) throws InvalidPlacementException {
		Move whiteMove = playerWhite.getMove(config, row, col);
		Player opponent = app.getPlayerBlack();
		if (whiteMove != null) {
			placeMove(whiteMove, playerWhite, opponent);
			blackTimerContinue();
			nextAIMove(opponent, playerWhite);
		} else {
			throw new InvalidPlacementException("Invalid move, please try again");
		}
	}

	/**
	 * Method that is responsible for updating the board and move-related instance
	 * variables with the new move made by the user.
	 * 
	 * @param move          a Move object containing information
	 * @param currentPlayer a Player object of the current player.
	 * @param opponent      a Player object of the opposing player.
	 */
	private void placeMove(Move move, Player currentPlayer, Player opponent) {
		labelStatusMessage.setText("");
		// Update current player move data.
		currentPlayer.getAllValidMoves().add(move);
		currentPlayer.incrementMoveCount();
		// Update backend board in the central game configuration with the new move.
		config.updateBoard(move);
		// Update game scene with the new move.
		updatePane(move);
		// Winning lines are checked, as well as if the board is full.
		Result roundResult = config.checkWinningLine(move);
		Result checkNumOfMoves = config.isBoardFull(currentPlayer, opponent);
		/*
		 * Only if "Result.CONTINUE" is returned from both checkWinningLine() and
		 * isBoardFull(), the game continues and it becomes the other player's turn.
		 */
		if (roundResult == Result.CONTINUE && checkNumOfMoves == Result.CONTINUE) {
			blackTurn = !blackTurn;
			signalTurn();
		} else if (checkNumOfMoves == Result.DRAW) {
			// If the board has no empty spot, the game will end with a draw result.
			stopAllTimers();
			app.gameOver(checkNumOfMoves);
		} else {
			/*
			 * For all the other cases (ie. if round result is not an enum Continue), the
			 * game will reach the end with the round result.
			 */
			stopAllTimers();
			app.gameOver(roundResult);
		}
	}

	/**
	 * Update message label and scene styling when switching player to indicate next
	 * player's turn.
	 */
	private void signalTurn() {
		String msg = null;
		if (blackTurn) {
			msg = "Black's Turn";
			onTurnStyle(labelBlack, labelBlackName, labelBlackTime);
			resetStyle(labelWhite, labelWhiteName, labelWhiteTime);
		} else {
			msg = "White's Turn";
			onTurnStyle(labelWhite, labelWhiteName, labelWhiteTime);
			resetStyle(labelBlack, labelBlackName, labelBlackTime);
		}
		labelStatusMessage.setText(msg);
		labelStatusMessage.setTextFill(Color.BLACK);
	}

	/**
	 * A helper method to update styling for current player's labels.
	 * 
	 * @param label label to show player color.
	 * @param name  label to show player name.
	 * @param time  label to show player time left.
	 */
	private void onTurnStyle(Label label, Label name, Label time) {
		label.setStyle("-fx-font-weight: bold; -fx-background-color: black; -fx-font-size: 16px");
		name.setStyle(
				"-fx-font-weight: bold; -fx-background-color: white; -fx-border-color: grey; -fx-border-radius: 2px; -fx-font-size: 18px");
		time.setStyle("-fx-font-weight: bold; -fx-background-color: transparent; -fx-font-size: 18px");
	}

	/**
	 * A helper method to reset player's label styling to original.
	 * 
	 * @param label label to show player color.
	 * @param name  label to show player name.
	 * @param time  label to show player time left.
	 */
	private void resetStyle(Label label, Label name, Label time) {
		label.setStyle("-fx-background-color: black; -fx-font-size: 14px");
		name.setStyle(
				"-fx-background-color: white; -fx-border-color: grey; -fx-border-radius: 2px; -fx-font-size: 18px");
		time.setStyle("-fx-background-color: transparent; -fx-font-size: 18px");
	}

	/**
	 * Method that will undo the last two moves and update all corresponding
	 * variables when "Undo" button is clicked.
	 * 
	 * @param event an action event invokes when user clicked on "Undo" button.
	 */
	@FXML
	private void onUndo(ActionEvent event) {
		Player playerBlack = app.getPlayerBlack();
		Player playerWhite = app.getPlayerWhite();
		try {
			// Update all related variables except ones in GUI when undo is requested.
			ArrayList<Move> undoMoves = config.undoMove(blackTurn, playerBlack, playerWhite);
			for (Move move : undoMoves) {
				removeStone(move); // Remove stone in the OnGameView.
			}
			removeLogs(); // Remove logs in the OnGameView.
		} catch (InvalidUndoException e) {
			labelStatusMessage.setText(e.getMessage());
			labelStatusMessage.setTextFill(Color.RED);
		}
	}

	/**
	 * The game will result in draw once a player clicks on the "Draw" button. The
	 * game will stop and show the game over window.
	 * 
	 * @param event an action event invokes when user clicked on "Draw" button.
	 */
	@FXML
	private void onDraw(ActionEvent event) {
		stopAllTimers();
		app.gameOver(Result.DRAW);
	}

	/**
	 * Method that exits the game by terminating the application.
	 * 
	 * @param event an action event invokes whenever user clicked the "Quit" button.
	 */
	@FXML
	private void onExitGame(ActionEvent event) {
		stopAllTimers();
		app.exitGame();
	}

	/**
	 * Method that restarts the game by returning the user to the start menu.
	 * 
	 * @param event an action event invokes whenever user clicked the "Start New
	 *              Game" button.
	 */
	@FXML
	private void onRestart(ActionEvent event) {
		stopAllTimers();
		app.restartGame();
	}

	/**
	 * Method that toggles the background music on and off based on user choice.
	 * 
	 * @param event an action event invokes when user clicked on "Volume" button.
	 */
	@FXML
	private void onMute(ActionEvent event) {
		app.playMenuSound();
		if (app.getBackgroundPlayer().getStatus() == MediaPlayer.Status.STOPPED) {
			volumeImage.setImage(new Image(new File("src/resources/Volume-Icon.png").toURI().toString()));
			app.playBackgroundMusic();
		} else {
			volumeImage.setImage(new Image(new File("src/resources/Volume-Muted-Icon.png").toURI().toString()));
			app.stopBackgroundMusic();
		}
	}

	/**
	 * Initialize method is called once to implement this controller when the
	 * OnGameView.fxml file is completely loaded. Background image is also updated
	 * accordingly.
	 */
	@FXML
	private void initialize() {
		assert labelBlackName != null
				: "fx:id=\"labelBlackName\" was not injected: check your FXML file 'OnGameView.fxml'.";
		assert paneBoard != null : "fx:id=\"paneBoard\" was not injected: check your FXML file 'OnGameView.fxml'.";
		assert labelBlackTime != null
				: "fx:id=\"labelBlackTime\" was not injected: check your FXML file 'OnGameView.fxml'.";
		assert labelWhiteTime != null
				: "fx:id=\"labelWhiteTime\" was not injected: check your FXML file 'OnGameView.fxml'.";
		assert scrollPaneMoveLogs != null
				: "fx:id=\"scrollPaneMoveLogs\" was not injected: check your FXML file 'OnGameView.fxml'.";
		assert paneBoardArea != null
				: "fx:id=\"paneBoardArea\" was not injected: check your FXML file 'OnGameView.fxml'.";
		assert labelBlack != null : "fx:id=\"labelBlack\" was not injected: check your FXML file 'OnGameView.fxml'.";
		assert buttonUndo != null : "fx:id=\"buttonUndo\" was not injected: check your FXML file 'OnGameView.fxml'.";
		assert labelStatusMessage != null
				: "fx:id=\"labelStatusMessage\" was not injected: check your FXML file 'OnGameView.fxml'.";
		assert labelWhite != null : "fx:id=\"labelWhite\" was not injected: check your FXML file 'OnGameView.fxml'.";
		assert gridPaneMoveLogs != null
				: "fx:id=\"gridPaneMoveLogs\" was not injected: check your FXML file 'OnGameView.fxml'.";
		assert labelWhiteName != null
				: "fx:id=\"labelWhiteName\" was not injected: check your FXML file 'OnGameView.fxml'.";
		// Set scene background image.
		Image backgroundImage = new Image("file:src/resources/Light-Wood-Background.png");
		BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true);
		Background background = new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bSize));
		borderPaneArea.setBackground(background);
	}
}