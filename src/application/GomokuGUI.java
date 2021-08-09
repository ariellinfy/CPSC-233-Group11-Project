package application;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
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

	/**
	 * Getter method that returns the instance variable "config".
	 * 
	 * @return config a GameConfiguration object that contains game board related
	 *         methods and variables.
	 */
	GameConfiguration getGameConfiguration() {
		return config;
	}

	/**
	 * Getter method that returns the instance variable "playerBlack".
	 * 
	 * @return playerBlack a Player object that contains information related to the
	 *         player (i.e. name of player, num of moves made, color of player).
	 */
	Player getPlayerBlack() {
		return playerBlack;
	}

	/**
	 * Setter method used to update the value of instance variable "playerBlack".
	 * 
	 * @param playerBlack a Player object that contains information related to the
	 *                    player of color Black (i.e. name of player, num of moves
	 *                    made, color of player).
	 */
	void setPlayerBlack(Player playerBlack) {
		this.playerBlack = playerBlack;
	}

	/**
	 * Getter method that returns the instance variable "playerWhite".
	 * 
	 * @return playerWhite a Player object that contains information related to the
	 *         player of color White (i.e. name of player, num of moves made, color
	 *         of player).
	 */
	Player getPlayerWhite() {
		return playerWhite;
	}

	/**
	 * Setter method used to update the value of instance variable "playerBlack".
	 * 
	 * @param playerWhite a Player object that contains information related to the
	 *                    player of color White (i.e. name of player, num of moves
	 *                    made, color of player).
	 */
	void setPlayerWhite(Player playerWhite) {
		this.playerWhite = playerWhite;
	}

	/**
	 * Getter method that returns the instance variable "winnerScore".
	 * 
	 * @return winnerScore the score of the winning player at the end of the game.
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
			/*
			 * StartMenuController is linked with the StartMenu scene using the
			 * linkWithApplication() method.
			 */
			StartMenuController startMenuController = loader.getController();
			startMenuController.linkWithApplication(this);
			/*
			 * sizeToScene() is used to match the size of the window with the contents (i.e.
			 * widgets) of the window.
			 */
			primaryStage.sizeToScene();
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
			/*
			 * The board size in OnGameController is set using current data of the config
			 * object.
			 */
			int boardSize = config.getChessBoard().getBoardSize();
			gameViewController.setBoardSize(boardSize);
			gameViewController.linkWithApplication(this);
			if (boardSize > 15) {
				primaryStage.setWidth(1450);
				primaryStage.setHeight(875);
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
		/*
		 * Series of if conditions checks if black has won, white has won, or a draw has
		 * occurred and updates "winnerScore" accordingly using calculateScore() method.
		 */
		if (result == Result.BLACK) {
			this.winnerScore = config.calculateScore(playerBlack, playerWhite);
		} else if (result == Result.WHITE) {
			this.winnerScore = config.calculateScore(playerWhite, playerBlack);
		} else if (result == Result.DRAW) {
			this.winnerScore = 0;
		}
		/*
		 * At the end of this method, displayResult() is invoked to load the game
		 * overview window at the end of the game.
		 */
		displayResult(result);
	}

	/**
	 * Initializes and loads another stage to display game results and ask user to
	 * end the application or start a new game.
	 * 
	 * @param result the enum result of the current game.
	 */
	private void displayResult(Result result) {
		final Stage gameOverview = new Stage();
		/*
		 * initModality() is invoked on gameOverview stage to prevent events such as
		 * user clicks from occurring on the primary stage (ie. game board) while the
		 * game overview window is displayed. Set gameOverview stage to be a child of
		 * primary stage, so that user won't be able to close the primary stage without
		 * first interact (eg. closing) with the child.
		 */
		gameOverview.initModality(Modality.APPLICATION_MODAL);
		gameOverview.initOwner(primaryStage);
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GameOverView.fxml"));
			Parent gameView = loader.load();
			gameOverview.setScene(new Scene(gameView));
			gameOverview.show();
			GameOverController gameOverController = loader.getController();
			// Pass the result of the game to the controller.
			gameOverController.linkWithApplication(this, result);
			gameOverview.sizeToScene();
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
		// startMenu() is invoked to return to the start menu.
		startMenu();
	}

	/**
	 * Method that ends the application.
	 */
	void exitGame() {
		System.exit(0);
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
