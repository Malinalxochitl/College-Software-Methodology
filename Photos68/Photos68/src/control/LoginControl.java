package control;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.User;

/**
 * This class controls the login scene
 * @author Joel Kurian
 * @author Jinseong Lee
 */
public class LoginControl extends ControlBase {
	
	/**
	 * User login field
	 */
	@FXML private TextField userField;
	
	/**
	 * initializes login page
	 */
	public void init() {
		getModel().setCurrUser(null);
		userField.clear();
	}
	
	/**
	 * logs into application
	 */
	public void login() {
		String username = userField.getText().trim();
		User user = getModel().getUser(username);
		
		if (user == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("User not found");
			alert.showAndWait();
			userField.clear();
		} else {
			getModel().setCurrUser(user);
			if (username.equalsIgnoreCase("admin")) {
				loginToAdmin();
			} else {
				loginToUser();
			}
		}
	}
	
	/**
	 * exits the application
	 */
	public void exit() {
		Platform.exit();
	}

}
