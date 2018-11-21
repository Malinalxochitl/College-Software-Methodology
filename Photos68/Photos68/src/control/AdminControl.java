package control;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.User;
import model.UserModel;

/**
 * This class controls the admin scene
 * @author Joel Kurian
 * @author Jinseong Lee
 */
public class AdminControl extends ControlBase implements ChangeListener<User> {

	/**
	 * list of application users
	 */
	@FXML private ListView<User> userList;
	
	/**
	 * textfield of user to be added
	 */
	@FXML private TextField userField;
	
	@FXML 
	public void initialize() {
		UserModel users = getModel();
		
		userList.setItems(users.getUserList());
		userList.getSelectionModel().selectedItemProperty().addListener(this);
		if (users.getUserList().size() > 0) {
			userList.getSelectionModel().select(0);
		}
	}
	
	/**
	 * preps admin scene for viewing
	 */
	public void init() {
		userField.clear();
	}
	
	/**
	 * exits the application
	 */
	public void exit() {
		Platform.exit();
	}
	
	/**
	 * deletes a user
	 */
	public void deleteUser() {
		UserModel users = getModel();
		int pos = userList.getSelectionModel().getSelectedIndex();
		
		if (pos >= 0) {
			users.deleteUser(pos);
			userList.refresh();
		}
	}
	
	/**
	 * adds a user to the application
	 */
	public void addUser() {
		String name = userField.getText().trim();
		UserModel users = getModel();
		
		if (name.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please type a name for the new user");
			alert.showAndWait();
		} else {
			User user = users.getUser(name);
			
			if (user != null) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("This user already exists");
				alert.showAndWait();
			} else {
				users.addUser(name);
				userField.clear();
			}
		}
	}
	
	/**
	 * logs out of the application
	 */
	public void logOff() {
		adminToLogin();
	}
	
	@Override
	public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
		// TODO Auto-generated method stub
		
	}

}
