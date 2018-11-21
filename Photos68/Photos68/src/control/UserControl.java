package control;

import java.time.LocalDate;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.Album;
import model.Photo;
import model.Tag;
import model.TagSearch;
import model.User;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;

/**
 * This class controls the user main display
 * @author Joel Kurian
 * @author Jinseong Lee
 */
public class UserControl extends ControlBase implements EventHandler<MouseEvent>, ChangeListener<Album> {
	
	/**
	 * tableview for displaying albums
	 */
	@FXML private TableView<Album> albumTable;
	
	/**
	 * Tableview columns
	 */
	@FXML private TableColumn startCol;
	@FXML private TableColumn endCol;
	@FXML private TableColumn sizeCol;
	@FXML private TableColumn nameCol;
	
	/**
	 * list of tags to search by
	 */
	@FXML private ListView<Tag> tagList;
	
	/**
	 * textfield for the tag name
	 */
	@FXML private TextField tagNameField;
	
	/**
	 * textfield for the tag value
	 */
	@FXML private TextField tagValueField;
	
	/**
	 * textfield for creating a new album
	 */
	@FXML private TextField albumNameField;
	
	/**
	 * date pickers for searching by date range
	 */
	@FXML private DatePicker startDatePicker;
	@FXML private DatePicker endDatePicker;
	
	/**
	 * checkbox for conjunction/disjunction search
	 */
	@FXML private CheckBox orCheck;
	
	@FXML
	public void initialize() {
		nameCol.setCellValueFactory(new PropertyValueFactory("name"));
		sizeCol.setCellValueFactory(new PropertyValueFactory("photoNum"));
		startCol.setCellValueFactory(new PropertyValueFactory("startDate"));
		endCol.setCellValueFactory(new PropertyValueFactory("endDate"));
		albumTable.getColumns().setAll(nameCol, sizeCol, startCol, endCol);
		
		endDatePicker.setValue(LocalDate.now());
		startDatePicker.setValue(endDatePicker.getValue().minusDays(30));
		
		albumTable.getSelectionModel().selectedItemProperty().addListener(this);
		
		albumTable.setOnMouseClicked(e -> {
			if (!e.getButton().equals(MouseButton.PRIMARY) || e.getClickCount() != 1) {
				if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2) {
					userToAlbum();
				}
			}
		});
	}
	
	public void init() {
		User user = getModel().getCurrUser();
		ObservableList<Album> albumList = user.getAlbumList();
		
		tagList.setItems(user.getTags().getTags());
		if (user.getTags().getTags().size() > 0) {
			tagList.getSelectionModel().select(0);
		}
		
		for (Album album: albumList) {
			album.setSizeAndDate();
		}
		
		albumTable.setItems(albumList);
		if(user.getAlbumList().size() > 0) {
			albumTable.getSelectionModel().select(0);
		}
		albumTable.refresh();
	}
	
	/**
	 * logs the user out
	 */
	public void logOff() {
		userToLogin();
	}
	
	/**
	 * exits the application
	 */
	public void exit() {
		Platform.exit();
	}
	
	/**
	 * renames an album
	 */
	public void renameAlbum() {
		String newName = albumNameField.getText().trim();
		
		if (newName.length() > 0) {
			User user = getModel().getCurrUser();
			int i = albumTable.getSelectionModel().getSelectedIndex();
			user.changeAlbumName(i, newName);
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please enter an album name");
			alert.showAndWait();
		}
		albumTable.refresh();
	}
	
	/**
	 * Adds an album to the user
	 */
	public void addAlbum() {
		User user = getModel().getCurrUser();
		String albumName = albumNameField.getText().trim();
		
		if (albumName.length() > 0) {
			user.addAlbum(albumName);
			albumTable.refresh();
			albumNameField.clear();
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please enter a name");
			alert.showAndWait();
		}
	}
	
	/**
	 * deletes an album from the user
	 */
	public void deleteAlbum() {
		Album album = albumTable.getSelectionModel().getSelectedItem();
		if (album != null) {
			User user = getModel().getCurrUser();
			user.deleteAlbum(album.getName());
			albumTable.refresh();
		}
	}
	
	/**
	 * Adds a search tag
	 */
	public void addSearchTag() {
		String name = tagNameField.getText().trim();
		String value = tagValueField.getText().trim();
		User user = getModel().getCurrUser();
		
		if (!(value.length() > 0 && name.length() > 0)) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("please fill out name and value fields");
			alert.showAndWait();
		} else {
			boolean succ = user.getTags().addTag(name, value);
			
			if (!succ) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("duplicate entry");
				alert.showAndWait();
			} else {
				tagList.refresh();
				tagNameField.clear();
				tagValueField.clear();
			}
		}
	}
	
	/**
	 * deletes a search tag
	 */
	public void deleteSearchTag() {
		User user = getModel().getCurrUser();
		int i = tagList.getSelectionModel().getSelectedIndex();
		
		user.deleteTag(i);
		tagList.refresh();
	}
	
	/**
	 * searches through photos by tags
	 */
	public void searchByTags() {
		User user = getModel().getCurrUser();
		TagSearch tagsearch = user.getTags();
		tagsearch.setOr(orCheck.isSelected());
		
		if (tagsearch.getTags().size() == 0) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("No search conditions");
			alert.showAndWait();
		} else {
			List<Photo> list = user.searchByTags();
			
			if (list.isEmpty()) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setContentText("No matches found");
				alert.showAndWait();
			} else {
				Album newAlbum = new Album(Album.ALBUM_SEARCH_NAME, list);
				newAlbum.setSizeAndDate();
				user.overwriteAlbum(newAlbum);
				
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setContentText("Matches can be found in a new album");
				alert.showAndWait();
				
				user.setCurrAlbum(newAlbum);
				userToAlbum();
			}
		}
	}
	
	/**
	 * searches through photos by date
	 */
	public void searchByDate() {
		User user = getModel().getCurrUser();
		
		if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Specify start and end of date range");
			alert.showAndWait();
		} else {
			user.getDates().setStart(startDatePicker.getValue());
			user.getDates().setEnd(endDatePicker.getValue());
			List<Photo> list = user.searchByDate();
			
			if (list.isEmpty()) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setContentText("No matches found");
				alert.showAndWait();
			} else {
				Album newAlbum = new Album(Album.ALBUM_SEARCH_NAME, list);
				newAlbum.setSizeAndDate();
				user.overwriteAlbum(newAlbum);
				
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setContentText("Matches can be found in a new album");
				alert.showAndWait();
				
				user.setCurrAlbum(newAlbum);
				userToAlbum();
			}
		}
	}

	/**
	 * sets the currently selected album
	 * @param newAlbum the newly selected album
	 */
	@Override
	public void changed(ObservableValue<? extends Album> arg0, Album arg1, Album newAlbum) {
		User user = getModel().getCurrUser();
		
		if (newAlbum != null) {
			user.setCurrAlbum(newAlbum);
		}
	}

	@Override
	public void handle(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
