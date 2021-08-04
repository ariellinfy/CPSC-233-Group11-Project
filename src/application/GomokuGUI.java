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

public class GomokuGUI extends Application {
	private Stage primaryStage;
	private GameConfiguration config = new GameConfiguration();
	private Player playerBlack;
	private Player playerWhite;
	private int winnerScore = 20;
	
	GameConfiguration getGameConfiguration() {
		return config;
	}
	
	Player getPlayerBlack() {
		return playerBlack;
	}
	
	void setPlayerBlack(Player playerBlack) {
		this.playerBlack = playerBlack;
	}
	
	Player getPlayerWhite() {
		return playerWhite;
	}
	
	void setPlayerWhite(Player playerWhite) {
		this.playerWhite = playerWhite;
	}
	
	int getWinnerScore() {
		return winnerScore;
	}
	
	private void startMenu() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StartMenu.fxml"));
			Parent startMenu = loader.load();
			primaryStage.setScene(new Scene(startMenu));
			primaryStage.show();
			StartMenuController startMenuController = loader.getController();
			startMenuController.linkWithApplication(this);
			primaryStage.sizeToScene();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	void playGame() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OnGameView.fxml"));
			Parent gameView = loader.load();
			primaryStage.setScene(new Scene(gameView));
			primaryStage.show();
			OnGameController gameViewController = loader.getController();
			gameViewController.setBoardSize(config.getChessBoard().getBoardSize());
			gameViewController.linkWithApplication(this);
			primaryStage.sizeToScene();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	void gameOver(Result result) {
		if (result == Result.BLACK) {
			this.winnerScore = config.calculateScore(playerBlack, playerWhite);

		} else if (result == Result.WHITE) {
			this.winnerScore = config.calculateScore(playerWhite, playerBlack);

		} else if (result == Result.DRAW) {
			this.winnerScore = 0;
		}
		displayResult(result);
	}
	
	private void displayResult(Result result) {
		final Stage resultMessage = new Stage();
		resultMessage.initModality(Modality.APPLICATION_MODAL);
		resultMessage.initOwner(primaryStage);
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GameOverView.fxml"));
			Parent gameView = loader.load();
			resultMessage.setScene(new Scene(gameView));
			resultMessage.show();
			GameOverController gameOverController = loader.getController();
			gameOverController.linkWithApplication(this, result);
			resultMessage.sizeToScene();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	void restartGame() {
		startMenu();
	}
	
	void exitGame() {
		System.exit(0);
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Gomoku");
		startMenu();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
