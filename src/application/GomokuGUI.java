package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.*;

/**
 * This class is responsible for running the GUI version of the Gomoku
 * application for the user.
 * 
 * 
 * @author Fu-Yin Lin, Justin Chua
 *
 */
public class GomokuGUI extends Application {
	private Stage primaryStage;
	private GameConfiguration config = new GameConfiguration();
	private Player playerBlack;
	private Player playerWhite;
	private int winnerScore = 20;
	private MediaPlayer menuButtonSound;
	private MediaPlayer boardSound;
	private MediaPlayer victorySound;

	/**
	 * Get current game configuration.
	 * 
	 * @return config a GameConfiguration object that contains game board related
	 *         methods and variables.
	 */
	GameConfiguration getGameConfiguration() {
		return config;
	}

	/**
	 * Get current player black of the game.
	 * 
	 * @return playerBlack a Player object that contains information related to the
	 *         player of color Black (i.e. name of player, num of moves made, color
	 *         of player).
	 */
	Player getPlayerBlack() {
		return playerBlack;
	}

	/**
	 * Set player black of the game.
	 * 
	 * @param playerBlack a Player object that contains information related to the
	 *                    player of color Black (i.e. name of player, num of moves
	 *                    made, color of player).
	 */
	void setPlayerBlack(Player playerBlack) {
		this.playerBlack = playerBlack;
	}

	/**
	 * Get current player white of the game.
	 * 
	 * @return playerWhite a Player object that contains information related to the
	 *         player of color White (i.e. name of player, num of moves made, color
	 *         of player).
	 */
	Player getPlayerWhite() {
		return playerWhite;
	}

	/**
	 * Set player white of the game.
	 * 
	 * @param playerWhite a Player object that contains information related to the
	 *                    player of color White (i.e. name of player, num of moves
	 *                    made, color of player).
	 */
	void setPlayerWhite(Player playerWhite) {
		this.playerWhite = playerWhite;
	}

	/**
	 * Get the winner score of the game.
	 * 
	 * @return the score of the winning player at the end of the game.
	 */
	int getWinnerScore() {
		return winnerScore;
	}

	/**
	 * Method that initializes and loads the start menu scene.
	 */
	void startMenu() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StartMenu.fxml"));
			Parent startMenu = loader.load();
			primaryStage.setScene(new Scene(startMenu));
			primaryStage.show();
			StartMenuController startMenuController = loader.getController();
			startMenuController.linkWithApplication(this);
			primaryStage.sizeToScene(); // make sure window size matches contents
			/*
			 * FileNotFoundException and IOException is handled in these catch blocks by
			 * printing an error message in the terminal.
			 */
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Method that initializes and loads the game board scene.
	 */
	void playGame() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OnGameView.fxml"));
			Parent gameView = loader.load();
			primaryStage.setScene(new Scene(gameView));
			primaryStage.show();
			OnGameController gameViewController = loader.getController();
			// Pass chosen boardSize to OnGameController to draw the correct board.
			int boardSize = config.getChessBoard().getBoardSize();
			gameViewController.setBoardSize(boardSize);
			gameViewController.linkWithApplication(this);
			// When boardSize is 19x19, adjusts the stage size to show the whole board.
			if (boardSize > 15) {
				primaryStage.setWidth(1500);
				primaryStage.setHeight(900);
			} else {
				primaryStage.sizeToScene();
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Method that is used to update the winner's score at the end of the match
	 * dependent on which player won, and show the resulting window to user.
	 * 
	 * @param the result of the game.
	 */
	void gameOver(Result result) {
		// Update score based on the result of the game.
		if (result == Result.BLACK) {
			this.winnerScore = config.calculateScore(playerBlack, playerWhite);
		} else if (result == Result.WHITE) {
			this.winnerScore = config.calculateScore(playerWhite, playerBlack);
		} else if (result == Result.DRAW) {
			this.winnerScore = 0;
		}
		// Display separate result window.
		displayResult(result);
	}

	/**
	 * Initializes and loads another stage to display game results and ask user to
	 * end the application or start a new game.
	 * 
	 * @param result the enum result of the current game.
	 */
	private void displayResult(Result result) {
		final Stage gameOverView = new Stage();
		/*
		 * initModality() is invoked on gameOverView stage to prevent events such as
		 * user clicks from occurring on the primary stage (ie. game board) while the
		 * game overview window is displayed. Set gameOverView stage to be a child of
		 * primary stage, so that user won't be able to close the primary stage without
		 * first interact (eg. closing) with the child.
		 */
		gameOverView.initModality(Modality.APPLICATION_MODAL);
		gameOverView.initOwner(primaryStage);
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GameOverView.fxml"));
			Parent gameView = loader.load();
			gameOverView.setScene(new Scene(gameView));
			gameOverView.show();
			GameOverController gameOverController = loader.getController();
			// Pass the result of the game to the controller.
			gameOverController.linkWithApplication(this, result);
			gameOverView.sizeToScene();
			/*
			 * When user selects the x icon of the window, the app will resume back to the
			 * start menu.
			 */
			gameOverView.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					gameOverView.close();
					restartGame();
				}
			});
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Method that restarts the game by returning to the start menu.
	 */
	void restartGame() {
		startMenu();
	}

	/**
	 * Method that ends the application.
	 */
	void exitGame() {
		primaryStage.close();
		System.exit(0);
	}
	
	void playMenuSound() {
		Media audioClip = new Media(new File("src/resources/Pokemon-Button-Click.mp3").toURI().toString());
		this.menuButtonSound = new MediaPlayer(audioClip);
		menuButtonSound.play();
	}
	
	void playBoardSound() {
		Media audioClip = new Media(new File("src/resources/Mouse-Click.wav").toURI().toString());
		this.boardSound = new MediaPlayer(audioClip);
		boardSound.play();
	}
	
	void playVictorySound() {
		Media audioClip = new Media(new File("src/resources/Pokemon-Level-Up.mp3").toURI().toString());
		this.victorySound = new MediaPlayer(audioClip);
		victorySound.play();
	}

	/**
	 * The main entry point for this JavaFX application. Initializes and sets up
	 * start menu as the first scene.
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Gomoku");
		startMenu();
	}

	/**
	 * The main method that launches the GUI Gomoku application.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
