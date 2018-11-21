package control;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;
import model.UserModel;

/**
 * This class controls stages and scene transitions
 * @author Joel Kurian
 * @author Jinseong Lee
 */
public class ControlBase {
	
	/**
	 * file path for data storage
	 */
	public final static String DATA_PATH = "Photos68/data.dat";
	
	/**
	 * Scene for logging in
	 */
	public static Scene loginScene = null;
	
	/**
	 * control for logging in
	 */
	public static LoginControl loginControl = null;
	
	/**
	 * stage for login and admin functionality
	 */
	public static Stage loginStage = null;
	
	/**
	 * scene for admin subsystem
	 */
	public static Scene adminScene = null;
	
	/**
	 * control for admin subsystem
	 */
	public static AdminControl adminControl = null;
	
	/**
	 * scene for user window
	 */
	public static Scene userScene = null;
	
	/**
	 * control for user window
	 */
	public static UserControl userControl = null;
	
	/**
	 * stage for user and album functionality
	 */
	public static Stage userStage = null;
	
	/**
	 * Scene for album viewing
	 */
	public static Scene albumScene = null;
	
	/**
	 * control for viewing an album
	 */
	public static AlbumControl albumControl = null;
	
	/**
	 * scene for moving a photo
	 */
	public static Scene moveScene = null;
	
	/**
	 * control for moving a photo
	 */
	public static MoveControl moveControl = null;
	
	/**
	 * scene for displaying a photo
	 */
	public static Scene displayScene = null;
	
	/**
	 * control for displaying a photo
	 */
	public static DisplayControl displayControl = null;
	
	/**
	 * scene for slideshow
	 */
	public static Scene slideScene = null;
	
	/**
	 * control for slideshow
	 */
	public static SlideControl slideControl = null;
	
	/**
	 * model for users
	 */
	private static UserModel model;
	
	/**
	 * starts the application
	 * @param primaryStage primaryStage
	 * @throws Exception FXML load error
	 */
	public static void start(Stage primaryStage) throws Exception {
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ControlBase.class.getResource("/view/Login.fxml"));
			Parent root = loader.load();
			loginScene = new Scene(root);
			loginControl = loader.getController();
		}
		
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ControlBase.class.getResource("/view/Admin.fxml"));
			Parent root = loader.load();
			adminScene = new Scene(root);
			adminControl = loader.getController();
		}

		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ControlBase.class.getResource("/view/UserMain.fxml"));
			Parent root = loader.load();
			userScene = new Scene(root);
			userControl = loader.getController();
		}
		
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ControlBase.class.getResource("/view/UserPhoto.fxml"));
			Parent root = loader.load();
			albumScene = new Scene(root);
			albumControl = loader.getController();
		}
		
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ControlBase.class.getResource("/view/MovePhoto.fxml"));
			Parent root = loader.load();
			moveScene = new Scene(root);
			moveControl = loader.getController();
		}
		
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ControlBase.class.getResource("/view/PhotoDisplay.fxml"));
			Parent root = loader.load();
			displayScene = new Scene(root);
			displayControl = loader.getController();
		}
		
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ControlBase.class.getResource("/view/PhotoSlideshow.fxml"));
			Parent root = loader.load();
			slideScene = new Scene(root);
			slideControl = loader.getController();
		}
		
		loginStage = primaryStage;
		userStage = new Stage();
		
		loginStage.setResizable(false);
		userStage.setResizable(false);
		
		toLogin();
	}
	
	/**
	 * gets the user model from the data file, or creates a new one
	 * @return user model for the application
	 */
	public static UserModel getModel() {
		if (model == null) {
			try {
				FileInputStream inFile = new FileInputStream(DATA_PATH);
				ObjectInputStream inObj = new ObjectInputStream(inFile);
				model = (UserModel) inObj.readObject();
				model.filePrep(false);
				inObj.close();
				inFile.close();
			} catch (IOException | ClassNotFoundException i) {
				//i.printStackTrace();
				model = null;
			}
		
			if (model == null) {
				model = new UserModel();
				model.addUser("admin");
				model.addUser("stock");
			
				model.setCurrUser(model.getUser("stock"));
				User stockUser = model.getCurrUser();
				Album stockAlbum = stockUser.addAlbum("stock_images");
			
				stockAlbum.addPhoto(Photo.makePhoto("Photos68/stock/stock1.jpg", null));
				stockAlbum.addPhoto(Photo.makePhoto("Photos68/stock/stock2.jpg", null));
				stockAlbum.addPhoto(Photo.makePhoto("Photos68/stock/stock3.jpg", null));
				stockAlbum.addPhoto(Photo.makePhoto("Photos68/stock/stock4.jpg", null));
				stockAlbum.addPhoto(Photo.makePhoto("Photos68/stock/stock5.jpg", null));
			}
		}
		return model;
	}
	
	/**
	 * sets up the login stage
	 */
	private static void toLogin() {
		loginStage.setScene(loginScene);
		loginControl.init();
		loginStage.show();
	}
	
	/**
	 * sets up the album stage
	 */
	private static void toUser() {
		userStage.setScene(userScene);
		userControl.init();
		userStage.show();
	}
	
	/**
	 * sets up album scene
	 */
	private static void toAlbum() {
		userStage.setScene(albumScene);
		albumControl.init();
		userStage.show();
	}
	
	/**
	 * moves from admin scene to login scene
	 */
	public static void adminToLogin() {
		toLogin();
	}
	
	/**
	 * login scene to admin scene
	 */
	public static void loginToAdmin() {
		loginStage.setScene(adminScene);
		adminControl.init();
		loginStage.show();
	}
	
	/**
	 * login scene to user scene
	 */
	public static void loginToUser() {
		loginStage.hide();
		toUser();
	}
	
	/**
	 * user scene to album scene
	 */
	public static void userToAlbum() {
		toAlbum();
	}
	
	/**
	 * user scene to login scene
	 */
	public static void userToLogin() {
		userStage.hide();
		toLogin();
	}
	
	/**
	 * album scene to user scene
	 */
	public static void albumToUser() {
		toUser();
	}
	
	/**
	 * album scene to display scene
	 */
	public static void albumToDisplay() {
		userStage.setScene(displayScene);
		displayControl.init();
		userStage.show();
	}
	
	/**
	 * album scene to slideshow scene
	 */
	public static void albumToSlide() {
		userStage.setScene(slideScene);
		slideControl.init();
		userStage.show();
	}
	
	/**
	 * album scene to move scene
	 */
	public static void albumToMove() {
		userStage.setScene(moveScene);
		moveControl.init();
		userStage.show();
	}
	
	/**
	 * Move scene to album scene
	 */
	public static void moveToAlbum() {
		toAlbum();
	}
	
	/**
	 * display scene to album scene
	 */
	public static void displayToAlbum() {
		toAlbum();
	}
	
	/**
	 * slideshow scene to album scene
	 */
	public static void slideToAlbum() {
		toAlbum();
	}
	
	/**
	 * stores model to data file
	 */
	public static void storeToFile() {
		if (model != null) {
			model.filePrep(true);
			try {
				FileOutputStream outFile = new FileOutputStream(DATA_PATH);
				ObjectOutputStream outObj = new ObjectOutputStream(outFile);
				outObj.writeObject(model);
				outObj.close();
				outFile.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}
}
