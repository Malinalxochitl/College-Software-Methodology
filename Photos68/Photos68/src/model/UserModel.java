package model;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 * model of all the users of the application
 * @author Joel Kurian
 * @author Jinseong Lee
 */
public class UserModel implements Serializable{

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -901543077081413325L;
	
	/**
	 * list of users (arraylist for storage)
	 */
	private ObservableList<User> userList;
	private ArrayList<User> keepUserList;
	
	/**
	 * the current user
	 */
	private User currUser;
	
	/**
	 * constructor
	 */
	public UserModel() {
		userList = FXCollections.observableArrayList();
		keepUserList = null;
		currUser = null;
	}
	
	/**
	 * @return the current user
	 */
	public User getCurrUser() {
		return currUser;
	}
	
	/**
	 * Changes the current user
	 * @param user the user to change to
	 */
	public void setCurrUser(User user) {
		currUser = user;
	}
	
	/**
	 * gets a user by name
	 * @param name name of the user to be gotten
	 * @return user with specified name
	 */
	public User getUser(String name) {
		return userList.stream().filter(user -> user.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}
	
	/**
	 * adds a user to the userlist
	 * @param name user to add
	 */
	public void addUser(String name) {
		User user = new User(name);
		ListOps.addToList(userList, user);
	}
	
	/**
	 * deletes a user by name
	 * @param name name of user to be deleted
	 * @return the deleted user
	 */
	public User deleteUser(String name) {
		if (name.equalsIgnoreCase("admin")) {
			return null;
		} else {
			return ListOps.deleteItem(userList, name, (t,u) -> t.getName().equalsIgnoreCase(u));
		}
	}
	
	/**
	 * deletes a user by position
	 * @param pos position of user to be deleted
	 */
	public void deleteUser(int pos) {
		if (pos >= 0 && pos < userList.size()) {
			if (userList.get(pos).getName().equalsIgnoreCase("admin")) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("Cannot delete User: Admin");
				alert.showAndWait();
			} else {
				userList.remove(pos);
			}
		}
	}
	
	/**
	 * @return the list of users
	 */
	public ObservableList<User> getUserList() {
		return userList;
	}
	
	/**
	 * Prepping for file operation
	 * @param write true if writing to disk, false if reading
	 */
	public void filePrep(boolean write) {
		if (write) {
			keepUserList = new ArrayList<>(userList);
			userList = null;
			
			for (User user: keepUserList) {
				user.filePrep(true);
			}
		} else {
			userList = FXCollections.observableArrayList(keepUserList);
			keepUserList = null;
			
			for (User user: userList) {
				user.filePrep(false);
			}
		}
	}

}
