package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class represents a user
 * @author Joel Kurian
 * @author Jinseong Lee
 */
public class User implements Serializable, Comparable<User> {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -4889203945610833564L;
	
	/**
	 * username
	 */
	private String name;
	
	/**
	 * list of albums (arraylist for storage)
	 */
	private ObservableList<Album> albumList;
	private ArrayList<Album> keepAlbumList;
	
	/**
	 * the current album
	 */
	private Album currAlbum;
	
	/**
	 * tags used for searching
	 */
	private TagSearch tags;
	
	/**
	 * date range for searching
	 */
	private TimeSearch times;
	
	/**
	 * User constructor
	 * @param name user's username
	 */
	public User(String name) {
		this.name = name;
		this.albumList = FXCollections.observableArrayList();
		this.keepAlbumList = null;
		this.currAlbum = null;
		this.tags = new TagSearch();
		this.times = new TimeSearch();
	}
	
	/**
	 * @return user's username
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return User's current album
	 */
	public Album getCurrAlbum() {
		return currAlbum;
	}
	
	/**
	 * Sets the current album to a new album
	 * @param newAlbum new current album
	 */
	public void setCurrAlbum(Album newAlbum) {
		currAlbum = newAlbum;
	}
	
	/**
	 * @return list of user's albums
	 */
	public ObservableList<Album> getAlbumList() {
		return albumList;
	}
	
	/**
	 * Returns a specific album
	 * @param name album name
	 * @return album with matching name
	 */
	public Album getAlbum(String name) {
		return ListOps.getItem(albumList,  name, (t,u) -> t.getName().equalsIgnoreCase(u));
	}
	
	/**
	 * Adds an album to the user
	 * @param name the album's name
	 * @return the added album, null if duplicate
	 */
	public Album addAlbum(String name) {
		Album newAlbum = new Album(name);
		boolean isCopy = IntStream.range(0, albumList.size()).anyMatch(i -> albumList.get(i).getName().equalsIgnoreCase(newAlbum.getName()));
		
		if (isCopy) {
			return null;
		} else {
			albumList.add(newAlbum);
			return newAlbum;
		}
	}
	
	/**
	 * Overwrites an album with the same name, if present
	 * @param album new album to overwrite old album
	 */
	public void overwriteAlbum(Album album) {
		IntStream.range(0, albumList.size()).filter(i -> albumList.get(i).getName().equalsIgnoreCase(album.getName())).findFirst().ifPresent(i -> albumList.remove(i));
		albumList.add(album);
	}
	
	/**
	 * deletes an album
	 * @param name name of album to be deleted
	 * @return the album deleted
	 */
	public Album deleteAlbum(String name) {
		return ListOps.deleteItem(albumList, name, (t,u) -> t.getName().equalsIgnoreCase(name));
	}
	
	/**
	 * Changes an album's name
	 * @param pos album index to be renamed
	 * @param newName new name for album
	 */
	public void changeAlbumName(int pos, String newName) {
		if (pos >= 0 && pos < albumList.size()) {
			boolean exists = IntStream.range(0, albumList.size()).filter(i -> i != pos).anyMatch(i -> albumList.get(i).getName().equalsIgnoreCase(newName));
		
			if (!exists) {
				albumList.get(pos).setName(newName);
			}
		}
	}
	
	
	/**
	 * @return current tag search parameters
	 */
	public TagSearch getTags() {
		return tags;
	}
	
	/**
	 * deletes a tag from the tag search
	 * @param pos index of tag to be deleted
	 */
	public void deleteTag(int pos) {
		ObservableList<Tag> listTags = tags.getTags();
		
		if (listTags.size() > 0 && pos >= 0 && pos < listTags.size()) {
			listTags.remove(pos);
		}
	}
	
	/**
	 * @return current range of dates for searching
	 */
	public TimeSearch getDates() {
		return times;
	}
	
	/**
	 * copies a photo into an album
	 * @param photo the photo to be copied
	 * @param name the album to be copied into
	 */
	public void copyPhoto(Photo photo, String name) {
		Photo newPhoto = new Photo(photo);
		Album album = getAlbum(name);
		album.addPhoto(newPhoto);
	}
	
	/**
	 * moves a photo from one album to another
	 * @param photo the photo to be moved
	 * @param name the album to be moved into
	 */
	public void movePhoto(Photo photo, String name) {
		Album album = getAlbum(name);
		currAlbum.deletePhoto(photo);
		album.addPhoto(photo);
	}
	
	/**
	 * @return all photos with specified tags
	 */
	public List<Photo> searchByTags() {
		List<Photo> list = new ArrayList<>();
		for (Album album: albumList) {
			if (!album.getName().equalsIgnoreCase(Album.ALBUM_SEARCH_NAME)) {
				list.addAll(album.searchTags(tags));
			}
		}
		return list;
	}
	
	/**
	 * @return all photos within time range
	 */
	public List<Photo> searchByDate() {
		List<Photo> list = new ArrayList<>();
		
		for (Album album: albumList) {
			if (!album.getName().equalsIgnoreCase(Album.ALBUM_SEARCH_NAME)) {
				list.addAll(album.searchDate(times));
			}
		}
		return list;
	}
	
	/**
	 * Preps the user object for a file operation
	 * @param write true if write operation, false if read
	 */
	public void filePrep(boolean write) {
		if (write) {
			keepAlbumList = new ArrayList<>(albumList);
			albumList = null;
			for (Album album: keepAlbumList) {
				album.filePrep(true);
			}
		} else {
			albumList = FXCollections.observableArrayList(keepAlbumList);
			keepAlbumList = null;
			for (Album album: albumList) {
				album.filePrep(false);
			}
		}
		tags.filePrep(write);
	}
	
	/**
	 * String representation of a user
	 * @return username
	 */
	public String toString() {
		return name;
	}

	/**
	 * compares the users usernames
	 * @param o The other user
	 * @return comparison of usernames
	 */
	public int compareTo(User o) {
		return name.compareToIgnoreCase(o.getName());
	}

}
