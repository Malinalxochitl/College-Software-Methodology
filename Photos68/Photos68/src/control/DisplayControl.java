package control;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import model.Photo;
import model.Tag;

/**
 * Control for displaying a photo
 * @author Joel Kurian
 * @author Jinseong Lee
 */
public class DisplayControl extends ControlBase {
	
	/**
	 * textarea for photo caption
	 */
	@FXML private TextArea captionField;
	
	/**
	 * list of photo's tags
	 */
	@FXML private ListView<Tag> tagList;
	
	/**
	 * photo's date
	 */
	@FXML private Label photoDate;
	
	/**
	 * pane for displaying photo
	 */
	@FXML private BorderPane photoPane;
	
	/**
	 * button to cancel caption change
	 */
	@FXML private Button cancelButton;
	
	/**
	 * textfield for tag name
	 */
	@FXML private TextField tagNameField;
	
	/**
	 * textfield for tag value
	 */
	@FXML private TextField tagValueField;
	
	/**
	 * initializes the scene
	 */
	public void init() {
		Photo photo = getModel().getCurrUser().getCurrAlbum().getCurrPhoto();
		
		photoPane.setCenter(photo.getImageNode(null));
		tagList.setItems(photo.getTags());
		captionField.setText(photo.getCaption());
		photoDate.setText(photo.getLocalDate());
	}
	
	/**
	 * initializes the control
	 */
	public void initialize() {

	}
	
	/**
	 * adds a tag to the photo
	 */
	public void addTag() {
		String name = tagNameField.getText().trim();
		String value = tagValueField.getText().trim();
		
		if (!(name.length() > 0 && value.length() > 0)) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("name or value field not filled in");
			alert.showAndWait();
		} else {
			Photo photo = getModel().getCurrUser().getCurrAlbum().getCurrPhoto();
			boolean succ = photo.addTag(name, value);
			
			if (!succ) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("Duplicate tag");
				alert.showAndWait();
			} else {
				tagList.refresh();
				tagNameField.clear();
				tagValueField.clear();
			}
		}
	}
	
	/**
	 * deletes a photo's tag
	 */
	public void deleteTag() {
		Photo photo = getModel().getCurrUser().getCurrAlbum().getCurrPhoto();
		int pos = tagList.getSelectionModel().getSelectedIndex();
		
		photo.deleteTag(pos);
		tagList.refresh();
	}
	
	/**
	 * returns to album
	 */
	public void toAlbum() {
		displayToAlbum();
	}
	
	/**
	 * changes the photo caption
	 */
	public void editCaption() {
		String newCaption = captionField.getText().trim();
		Photo photo = getModel().getCurrUser().getCurrAlbum().getCurrPhoto();
		
		if (newCaption.length() == 0) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Enter a valid caption");
			alert.showAndWait();
			captionField.setText(photo.getCaption());
		} else {
			photo.setCaption(newCaption);
		}
		cancelButton.setDisable(true);
	}
	
	/**
	 * cancels the caption edit
	 */
	public void cancelEdit() {
		Photo photo = getModel().getCurrUser().getCurrAlbum().getCurrPhoto();
		captionField.setText(photo.getCaption());
		cancelButton.setDisable(true);
	}
	
	/**
	 * detects changes in text area
	 */
	public void edited() {
		cancelButton.setDisable(false);
	}

}
