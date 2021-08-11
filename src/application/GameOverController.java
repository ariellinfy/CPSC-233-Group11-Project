package application;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Result;

/**
 * A view to display game result.
 * 
 * @author Fu-Yin Lin
 *
 */
public class GameOverController {
	private GomokuGUI app;

	@FXML
	private ImageView winningImage;

	@FXML
	private Label labelScore;

	@FXML
	private Button buttonRestart;

	/**
	 * Link with main controller and updates labels with game result.
	 * 
	 * @param app    the instance of GomokuGUI when running the program.
	 * @param result the result of the game.
	 */
	void linkWithApplication(GomokuGUI app, Result result) {
		this.app = app;
		app.playVictorySound();
		if (result == Result.BLACK) {
			winningImage.setImage(new Image(new File("src/resources/Black-Wins-Graphic.png").toURI().toString()));
			labelScore.setText("Winner score is: " + app.getWinnerScore());
		} else if (result == Result.WHITE) {
			winningImage.setImage(new Image(new File("src/resources/White-Wins-Graphic.png").toURI().toString()));
			labelScore.setText("Winner score is: " + app.getWinnerScore());
		} else if (result == Result.DRAW) {
			winningImage.setImage(new Image(new File("src/resources/Draw-Graphic.png").toURI().toString()));
			labelScore.setText("Game score is: " + app.getWinnerScore());
		}
	}

	/**
	 * Method that ends the application when "End" button is clicked.
	 * 
	 * @param event an action event invokes when user clicked on "End" button.
	 */
	@FXML
	private void onExit(ActionEvent event) {
		app.exitGame();
	}

	/**
	 * Method that returns to the start menu to start a new game when "Start New
	 * Game" button is clicked.
	 * 
	 * @param event an action event invokes when user clicked on "Start New Game"
	 *              button.
	 */
	@FXML
	private void onRestart(ActionEvent event) {
		app.playMenuSound();
		app.restartGame();
		Stage stage = (Stage) buttonRestart.getScene().getWindow();
		stage.close();
	}

	/**
	 * Initialize method that is invoked once to set up this controller once the
	 * GameOverView.fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		assert labelScore != null : "fx:id=\"labelScore\" was not injected: check your FXML file 'GameOverView.fxml'.";
		assert winningImage != null
				: "fx:id=\"winningImage\" was not injected: check your FXML file 'GameOverView.fxml'.";
	}
}
