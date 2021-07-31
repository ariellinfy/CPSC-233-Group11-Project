module GomokuGame {
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.controls;
	
	opens application to javafx.graphics, javafx.fxml;
}