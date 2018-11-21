package application;

//
//Assignment 1 - Song Library
//Jin Lee (jl1795)
//Joel Kurian (jjk267)
//

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SongLib extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/view1.fxml"));
		
		Parent root;
		
		try {
			root = loader.load();
		} catch (IOException e) {
			return;
		}
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Song Library");
		primaryStage.setResizable(false);  
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
