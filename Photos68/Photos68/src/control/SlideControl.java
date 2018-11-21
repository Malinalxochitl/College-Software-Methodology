package control;

import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import model.Album;

/**
 * control for displaying a slideshow
 * @author Joel Kurian
 * @author Jinseong Lee
 */
public class SlideControl extends ControlBase{

	/**
	 * Pagination for slideshow of photos
	 */
	@FXML private Pagination pagination;
	
	/**
	 * initializes slideshow control
	 */
	@FXML
	public void initialize() {
		pagination.setPageFactory(index -> {
			Album album = getModel().getCurrUser().getCurrAlbum();
			
			if (album.getSize() > 0) {
				return album.getPhoto(index).getImageNode(null);
			} else {
				return null;
			}
		});
	}
	
	/**
	 * initializes the scene
	 */
	public void init() {
		Album album = getModel().getCurrUser().getCurrAlbum();
		pagination.setPageCount(album.getSize());
		if (album.getSize() == 0) {
			pagination.setVisible(false);
		} else {
			pagination.setVisible(true);
		}
	}
	
	/**
	 * returns to the album
	 */
	public void returnToAlbum() {
		slideToAlbum();
	}
}
