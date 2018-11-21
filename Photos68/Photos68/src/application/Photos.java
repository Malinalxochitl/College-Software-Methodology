package application;

import control.ControlBase;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Driver class
 * @author Joel Kurian
 * @author Jinseong Lee
 */
public class Photos extends Application {

	/**
	 * @param primaryStage the primary stage of the application
	 * @throws Exception if stage does not start
	 */
	public void start(Stage primaryStage) throws Exception {
		ControlBase.start(primaryStage);
	}

	/**
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	public void stop() {
		ControlBase.storeToFile();
	}
}