package application;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import model.*;

/**
 * 
 * @author Fu-Yin Lin
 *
 */
public class StartMenuController {
	private GomokuGUI app;
	private GameConfiguration config;
	
    @FXML
    private ToggleGroup opponentGroup;
    
    @FXML
    private ToggleGroup difficultyGroup;
    
    @FXML
    private ToggleGroup userColorGroup;
    
    @FXML
    private ToggleGroup boardSizeGroup;
    
    @FXML
    private HBox hBoxDifficulty;
    
    void linkWithApplication(GomokuGUI app) {
    	this.app = app;
    	this.config = app.getGameConfiguration();
    }
    
    @FXML
    private void onExitGame(ActionEvent event) {
    	app.exitGame();
    }

    @FXML
    private void onStartGame(ActionEvent event) {
    	Board board = new Board((Integer) boardSizeGroup.getSelectedToggle().getUserData());
    	config.setChessBoard(board);
    	Player opponent = new ComputerPlayer(Level.MEDIUM);
    	if (opponentGroup.getSelectedToggle().getUserData().toString().equals("Computer")) {
    		String difficultyLevel = difficultyGroup.getSelectedToggle().getUserData().toString();
    		if (difficultyLevel.equals("Hard")) {
    			opponent = new ComputerPlayer(Level.HARD);
    		} else if (difficultyLevel.equals("Easy")) {
    			opponent = new ComputerPlayer(Level.EASY);
    		}
    	} else {
    		opponent = new HumanPlayer();
    	}
    	if (userColorGroup.getSelectedToggle().getUserData().toString().equals("Black")) {
    		app.setPlayerBlack(new HumanPlayer(Stone.BLACK));
    		opponent.setPlayerColor(Stone.WHITE);
    		app.setPlayerWhite(opponent);
    	} else {
    		app.setPlayerWhite(new HumanPlayer(Stone.WHITE));
    		opponent.setPlayerColor(Stone.BLACK);
    		app.setPlayerBlack(opponent);
    	}
    	app.playGame();
    }
    
    private void setToggleButton(ToggleGroup toggleGroup) {
    	if (!toggleGroup.equals(boardSizeGroup)) {
    		for (Toggle toggle : toggleGroup.getToggles()) {
        		toggle.setUserData(((ToggleButton)toggle).getText());
        	}
    	} else {
    		for (Toggle toggle : toggleGroup.getToggles()) {
    			int size = Integer.parseInt(((ToggleButton)toggle).getText().split("x")[0]);
        		toggle.setUserData(size);
        	}
    	}
    }

    private void initToggleListener(ToggleGroup toggleGroup) {
    	setToggleButton(toggleGroup);
    	toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
        	@Override
        	public void changed(ObservableValue<? extends Toggle> observable, final Toggle oldValue, final Toggle newValue) {
        		if (newValue == null) {
        			Platform.runLater(new Runnable() {
        				public void run() {
        					toggleGroup.selectToggle(oldValue);
        				}
        			});
        		}
        		if (opponentGroup.getSelectedToggle().getUserData().toString().equals("Human")) {
        			hBoxDifficulty.setDisable(true);
            	} else {
            		hBoxDifficulty.setDisable(false);
            	}
        	}
        });
    }
    
    @FXML
    private void initialize() {
        assert boardSizeGroup != null : "fx:id=\"boardSizeGroup\" was not injected: check your FXML file 'StartMenu.fxml'.";
        assert userColorGroup != null : "fx:id=\"userColorGroup\" was not injected: check your FXML file 'StartMenu.fxml'.";
        assert opponentGroup != null : "fx:id=\"opponentGroup\" was not injected: check your FXML file 'StartMenu.fxml'.";
        assert difficultyGroup != null : "fx:id=\"difficultyGroup\" was not injected: check your FXML file 'StartMenu.fxml'.";
        initToggleListener(opponentGroup);
        initToggleListener(difficultyGroup);
        initToggleListener(userColorGroup);
        initToggleListener(boardSizeGroup);
    }
}
