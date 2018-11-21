package control;

import java.io.File;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;

/**
 * This class controls the album display
 * @author Joel Kurian
 * @author Jinseong Lee
 */
public class AlbumControl extends ControlBase implements EventHandler<MouseEvent> {

	/**
	 * Style of photo not selected
	 */
	static final private String STYLE_DESELECT = "-fx-border-color: white;\n" +
									  "-fx-border-style: solid;\n" +
									  "-fx-border-width: 3;\n";
	
	/**
	 * style of selected photo
	 */
	static final private String STYLE_SELECT = "-fx-border-color: blue;\n" +
			  						   "-fx-border-style: solid;\n" +
			  						   "-fx-border-width: 3;\n";
	
	/**
	 * Tile for photo thumbnail
	 */
	@FXML private TilePane tile;
	
	/**
	 * Label for album's name
	 */
	@FXML private Label albumName;
	
	/**
	 * list of thumbnail nodes
	 */
	private ObservableList<Node> nodeList;
	
	public AlbumControl() {
		AlbumControl control = this;
	}
	
	/**
	 * initializes the controller
	 */
	@FXML
	public void initialize() {
		nodeList = tile.getChildren();
	}
	
	/**
	 * initialize the album display
	 */
	public void init() {
		User user = getModel().getCurrUser();
		Album album = user.getCurrAlbum();
		nodeList.clear();
		albumName.setText(album.getName());
		
		List<Photo> photoList = album.getPhotoList();
		for (Photo photo: photoList) {
			BorderPane wrapper = photo.getThumbView(this, STYLE_DESELECT);
			nodeList.add(wrapper);
		}
		
		int pos = album.getCurrIndex();
		setCurrPhoto(pos);
	}
	
	/**
	 * Adds a photo to the album
	 */
	public void addPhoto() {
		User user = getModel().getCurrUser();
		Album album = user.getCurrAlbum();
		FileChooser chooser = new FileChooser();
		
		File photoFile = chooser.showOpenDialog(new Stage());
		
		if (photoFile != null) {
			
			Photo photo = Photo.makePhoto(photoFile.getAbsolutePath(), photoFile);
			int pos = album.addPhoto(null, photo);
			BorderPane wrapper = null;
			
			if (photo != null) {
				wrapper = photo.getThumbView(this, STYLE_DESELECT);
			}
			
			nodeList.add(pos, wrapper);
			setCurrPhoto(pos);
		}
	}
	
	/**
	 * deletes a photo from the album
	 */
	public void deletePhoto() {
		User user = getModel().getCurrUser();
		Album album = user.getCurrAlbum();
		Photo photo = album.getCurrPhoto();
		
		if (photo != null) {
			int pos = album.deletePhoto(photo);
			nodeList.remove(pos);
		
			pos = album.getCurrIndex();
			setCurrPhoto(pos);
		} else {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setContentText("No photo selected");
			alert.showAndWait();
		}
	}
	
	/**
	 * helps select the new current photo
	 * @param newPos index of new current photo
	 */
	public void setCurrPhoto(int newPos) {
		User user = getModel().getCurrUser();
		Album album = user.getCurrAlbum();
		int oldPos = album.getCurrIndex();
		newPos = album.setCurrIndex(newPos);
		
		if (newPos >= 0 && nodeList.size() > 0) {
			if (oldPos == -1) {
				BorderPane currWrapper = (BorderPane) nodeList.get(newPos);
				
				currWrapper.setStyle(STYLE_SELECT);
			} else if (newPos != oldPos) {
				BorderPane currWrapper = (BorderPane) nodeList.get(newPos);
				BorderPane oldWrapper = (BorderPane) nodeList.get(oldPos);
				
				currWrapper.setStyle(STYLE_SELECT);
				oldWrapper.setStyle(STYLE_DESELECT);
			} else {
				BorderPane currWrapper = (BorderPane) nodeList.get(newPos);
				
				currWrapper.setStyle(STYLE_SELECT);
			}
		}
	}
	
	/**
	 * Selects provided photo as current photo
	 * @param photo photo to be selected
	 */
	public void setCurrPhoto(Photo photo) {
		User user = getModel().getCurrUser();
		Album album = user.getCurrAlbum();
		int index = album.getIndex(photo);
		
		setCurrPhoto(index);
	}
	
	/**
	 * manages mouse events
	 * @param e MouseEvent object
	 */
	@Override
	public void handle(MouseEvent e) {
		Object object = e.getSource();
		if (object instanceof ImageView) {
			ImageView image = (ImageView) object;
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				if (e.getClickCount() == 1) {
					Photo photo = (Photo)image.getUserData();
					setCurrPhoto(photo);
				} else if (e.getClickCount() == 2) {
					Photo photo = (Photo)image.getUserData();
					setCurrPhoto(photo);
					albumToDisplay();
				}
			}
		}
		e.consume();
	}
	
	/**
	 * returns to the album list
	 */
	public void returnToAlbums() {
		albumToUser();
	}
	
	/**
	 * exits the application
	 */
	public void exit() {
		Platform.exit();
	}
	
	/**
	 * opens the display for moving the selected photo
	 */
	public void movePhoto() {
		albumToMove();
	}
	
	/**
	 * opens slideshow display of album
	 */
	public void slideshow() {
		albumToSlide();
	}
}


