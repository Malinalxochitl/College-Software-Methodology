package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * This class represents a photo
 * @author Joel Kurian
 * @author Jinseong Lee
 *
 */
public class Photo implements Serializable, Comparable<Photo> {
	
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 6955723612371190680L;
	
	/**
	 * photo filename
	 */
	private String fileName;
	
	/**
	 * thumbnail filename 
	 */
	private String thumbFileName;
	
	/**
	 * photo's caption
	 */
	private String caption;
	
	/**
	 * photo's date
	 */
	private long date;
	
	/**
	 * photo's tags (arraylist for serial storage)
	 */
	private ObservableList<Tag> tagList;
	private ArrayList<Tag> keepTagList;

	/**
	 * thumbnail specifications
	 */
	private static final int THUMB_HEIGHT = 120;
	private static final int THUMB_WIDTH = 120;
	private static final String THUMB_FORMAT = "png";
	private static final String THUMB_PATH = "Photos68/thumbnails";
	
	/**
	 * create thumbnails folder
	 */
	static { new File(THUMB_PATH).mkdir(); }
	
	/**
	 * Clears the lists depending on whether we are reading or writing to the data file
	 * @param write true if writing to file, false otherwise
	 */
	public void filePrep(boolean write) {
		if (write) {
			keepTagList = new ArrayList<>(tagList);
			tagList = null;
		} else {
			tagList = FXCollections.observableArrayList(keepTagList);
			keepTagList = null;
		}
	}
	
	/**
	 * @param fileName photo filename
	 * @param thumbFileName thumbnail filename 
	 * @param caption photo's caption
	 * @param date photo's date
	 */
	private Photo(String fileName, String thumbFileName, String caption, long date) {
		this.fileName = fileName;
		this.thumbFileName = thumbFileName;
		this.caption = caption;
		this.date = date;
		tagList = FXCollections.observableArrayList();
	}
	
	/**
	 * clones another photo onto this photo
	 * @param photo photo to copy
	 */
	public Photo(Photo photo) {
		this.fileName = photo.fileName;
		this.thumbFileName = photo.thumbFileName;
		this.caption = photo.caption;
		this.date = photo.date;
		
		tagList = FXCollections.observableArrayList();
		for (Tag i: photo.tagList) {
			tagList.add(new Tag(i));
		}
		
		try {
			Files.copy(new File(photo.getThumbFileName()).toPath(), new File(getThumbFileName()).toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	/**
	 * creates a photo object
	 * @param name the photo's filename
	 * @param file the file object for the photo
	 * @return new photo object
	 */
	public static Photo makePhoto(String name, File file) {
		if (file == null) {
			file = new File(name);
		}
		long fileDate = file.lastModified();
		String fileName = file.getName();
		if (fileName.indexOf('.') > 0) {
			fileName = fileName.substring(0, fileName.indexOf('.'));
		}
		String thumbnail = String.valueOf(UUID.randomUUID());
		
		boolean success = makeThumbNail(name, getThumbFileName(thumbnail), THUMB_WIDTH, THUMB_HEIGHT, THUMB_FORMAT);
		
		if (success) {
			return new Photo(name, thumbnail, fileName, fileDate);
		} else {
			return null;
		}
	}
	
	/**
	 * creates a thumbnail for a photo
	 * @param filename photo's filename
	 * @param thumbfilename thumbnail's filename
	 * @param width thumbnail's width
	 * @param height thumbnail's height
	 * @param format thumbnail's format
	 * @return true if thumbnail is created, false otherwise
	 */
	private static boolean makeThumbNail(String filename, String thumbfilename, int width, int height, String format) {
		Image image = null;
		try {
			image = new Image(new FileInputStream(filename), width, height, true, true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if (image != null) {
			BufferedImage buffimage = SwingFXUtils.fromFXImage(image, null);
			try {
				FileOutputStream fs = new FileOutputStream(thumbfilename);
				ImageIO.write(buffimage, format, fs);
				fs.close();
			} catch (IOException e) {
				//e.printStackTrace();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * checks whether this photo is within a date range
	 * @param dates conditional containing the range of dates
	 * @return true if date of this photo is within range
	 */
	public boolean withinRange(TimeSearch dates) {
		ZoneId timezone = ZoneId.systemDefault();
		LocalDate startDate = dates.getStart();
		LocalDate endDate = dates.getEnd().plusDays(1);
		
		long start = startDate.atStartOfDay(timezone).toEpochSecond();
		long end = endDate.atStartOfDay(timezone).toEpochSecond();
		
		Date photoDate = new Date(this.getDate());
		LocalDateTime thisDate = photoDate.toInstant().atZone(timezone).toLocalDateTime();
		long currentTime = thisDate.atZone(timezone).toEpochSecond();
		
		if (currentTime >= start && currentTime < end) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * creates a pane for displaying an image thumbnail within an album
	 * @param handler mouse event handler
	 * @param style selection styling
	 * @return thumbnail selection view
	 */
	public BorderPane getThumbView(EventHandler<MouseEvent> handler, String style) {
		Image image = new Image("File:" + getThumbFileName(), 0, 0, false, false);
		ImageView view = new ImageView(image);
		view.setFitHeight(THUMB_HEIGHT);
		view.setFitWidth(THUMB_WIDTH);
		view.setOnMouseClicked(handler);
		view.setUserData(this);
		
		VBox group = new VBox(4);
		group.getChildren().addAll(view, new Label(getCaption()), new Label(getLocalDate()));
		BorderPane wrap = new BorderPane(group);
		wrap.setStyle(style);
		
		return wrap;
	}
	
	/**
	 * returns an image configured for viewing
	 * @param handler event handler for mouse events
	 * @return image node to display
	 */
	public Node getImageNode(EventHandler<MouseEvent> handler) {
		ImageView view;
		Image image = null;
		
		try {
			image = new Image(new FileInputStream(getFileName()));
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		view = new ImageView();
		view.setFitWidth(318);
		view.setFitHeight(272);
		view.setSmooth(true);
		view.setPreserveRatio(true);
		view.setImage(image);
		view.setOnMouseClicked(handler);
		
		return view;
	}

	/**
	 * @return list of photo's tags
	 */
	public ObservableList<Tag> getTags() {
		return tagList;
	}
	
	/**
	 * Adds a tag to the photo
	 * @param name tag's name
	 * @param value tag's value
	 * @return true if added, false if not
	 */
	public boolean addTag(String name, String value) {
		Tag tag = new Tag(name, value);
		return ListOps.addToList(tagList, tag);
	}
	
	/**
	 * Deletes a tag based on value
	 * @param name name of tag
	 * @param value value of tag
	 * @return the deleted tag
	 */
	public Tag deleteTag(String name, String value) {
		Tag tag = new Tag(name, value);
		return ListOps.deleteItem(tagList, tag, (t,u)->t.compareTo(u) == 0);
	}
	
	/**
	 * deletes a tag based on index
	 * @param i index to be deleted
	 */
	public void deleteTag(int i) {
		if (i >= 0 && i < tagList.size()) {
			tagList.remove(i);
		}
	}
	
	/**
	 * @param name the thumbnail's name
	 * @return filename for the provided thumbnail
	 */
	public static String getThumbFileName(String name) {
		return THUMB_PATH + "/" + name + "." + THUMB_FORMAT;
	}
	
	/**
	 * 
	 * @return this photo's thumbnail filename
	 */
	private String getThumbFileName() {
		return getThumbFileName(thumbFileName);
	}
	
	/**
	 * 
	 * @return file name of the photo
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * changes the photo's filename
	 * @param fileName filename to be set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * 
	 * @return photo's caption
	 */
	public String getCaption() {
		return caption;
	}
	
	/**
	 * changes the photo's caption
	 * @param caption the caption to be set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * 
	 * @return date of the photo
	 */
	public long getDate() {
		return date;
	}
	
	/**
	 * 
	 * @return date of this photo converted to string
	 */
	public String getLocalDate() {
		return dateToLocal(date);
	}

	/**
	 * converts the long date into a readable string
	 * @param time the date to be converted
	 * @return date in String format
	 */
	public static String dateToLocal(long time) {
		LocalDateTime datetime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return datetime.format(format);
	}

	@Override
	public int compareTo(Photo o) {
		int filedif = this.getFileName().compareTo(o.getFileName());
		if (filedif != 0) {
			return filedif;
		} else {
			int captiondif = this.getCaption().compareTo(o.getCaption());
			if (captiondif != 0) {
				return captiondif;
			} else {
				return 0;
			}
		}
	}
}