package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Result;

/**
 * 
 * @author Fu-Yin Lin
 *
 */
public class GameOverController {
	private GomokuGUI app;
	
    @FXML
    private Label labelScore;

    @FXML
    private Label labelWinning;

	void linkWithApplication(GomokuGUI app, Result result) {
		this.app = app;
		if (result == Result.BLACK) {
			labelWinning.setText("Black Wins!");
			labelScore.setText("Winner score is: " + app.getWinnerScore());
		} else if (result == Result.WHITE) {
			labelWinning.setText("White Wins!");
			labelScore.setText("Winner score is: " + app.getWinnerScore());
		} else if (result == Result.DRAW) {
			labelWinning.setText("It's a draw!");
			labelScore.setText("Game score is: " + app.getWinnerScore());
		}
	}
	
    @FXML
    private void onExit(ActionEvent event) {
    	app.exitGame();
    }

    @FXML
    private void onRestart(ActionEvent event) {
    	app.restartGame();
    	labelWinning.getScene().getWindow().hide();
    }

    @FXML
    private void initialize() {
        assert labelScore != null : "fx:id=\"labelScore\" was not injected: check your FXML file 'GameOverView.fxml'.";
        assert labelWinning != null : "fx:id=\"labelWinning\" was not injected: check your FXML file 'GameOverView.fxml'.";
    }
}
