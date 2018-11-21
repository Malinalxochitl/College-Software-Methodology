package control;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import model.Album;
import model.ListOps;
import model.Photo;
import model.User;

/**
 * This class moves photos between albums
 * @author Joel Kurian
 * @author Jinseong Lee
 */
public class MoveControl extends ControlBase implements ChangeListener<Album> {
	
	/**
	 * listview for list of albums
	 */
	@FXML private ListView<Album> albumListView;
	
	/**
	 * checkbox for choosing between copy/move
	 */
	@FXML private CheckBox copyCheck;
	
	/**
	 * initializes scene
	 */
	public void init() {
		copyCheck.setSelected(false);
	}
	
	/**
	 * initializes control
	 */
	public void initialize() {
		User user = getModel().getCurrUser();
		Album album = user.getCurrAlbum();
		ObservableList<Album> albumList = user.getAlbumList();
		
		//ListOps.filterList(albumList, album, (t, u) -> !t.getName().equalsIgnoreCase(u.getName()));
		
		albumListView.setItems(albumList);
		albumListView.getSelectionModel().selectedItemProperty().addListener(this);
		
		if (user.getAlbumList().size() > 0) {
			albumListView.getSelectionModel().select(0);
		}
	}
	
	/**
	 * chooses between moving and copying to an album
	 */
	public void chooseMove() {
		Photo photo = getModel().getCurrUser().getCurrAlbum().getCurrPhoto();
		if (photo != null) {
			if (copyCheck.isSelected()) {
				copyPhoto();
			} else {
				movePhoto();
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setContentText("No photo selected");
			alert.showAndWait();
		}
		returnToAlbum();
	}
	
	
	/**
	 * copies current photo to selected album
	 */
	private void copyPhoto() {
		User user = getModel().getCurrUser();
		Photo photo = user.getCurrAlbum().getCurrPhoto();
		String albumName = albumListView.getSelectionModel().getSelectedItem().getName();
		
		user.copyPhoto(photo, albumName);
	}
	
	/**
	 * moves current photo to selected album
	 */
	private void movePhoto() {
		User user = getModel().getCurrUser();
		Album oldAlbum = user.getCurrAlbum();
		Photo photo = oldAlbum.getCurrPhoto();
		String newAlbum = albumListView.getSelectionModel().getSelectedItem().getName();
		
		user.movePhoto(photo, newAlbum);
	}
	
	/**
	 * returns to the current album
	 */
	public void returnToAlbum() {
		moveToAlbum();
	}

	@Override
	public void changed(ObservableValue<? extends Album> observable, Album oldValue, Album newValue) {
		// TODO Auto-generated method stub
		
	}
}
