module GomokuGame {
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.media;
	
	opens application to javafx.graphics, javafx.fxml;
}