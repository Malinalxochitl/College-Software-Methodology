package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

/**
 * This class represents a photo album
 * @author Joel Kurian
 * @author Jinseong Lee
 */
public class Album implements Comparable<Album>, Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 9014399198008204517L;
	
	/**
	 * name of the album generated when searching
	 */
	public static String ALBUM_SEARCH_NAME = " Search Results";
	
	/**
	 * Album's name (String is for storage)
	 */
	private SimpleStringProperty name;
	private String keepName;
	
	/**
	 * earliest picture in the album
	 */
	private SimpleStringProperty startDate;
	
	/**
	 * latest picture in the album
	 */
	private SimpleStringProperty endDate;
	
	/**
	 * number of photos in the album
	 */
	private SimpleIntegerProperty photoNum;
	
	/**
	 * current photo index
	 */
	private int currIndex;
	
	/**
	 * List of the photos in the album
	 */
	private List<Photo> photos;
	
	/**
	 * Album constructor
	 * @param name album's name
	 */
	public Album(String name) {
		this.name = new SimpleStringProperty(name);
		this.photoNum = new SimpleIntegerProperty(0);
		this.startDate = new SimpleStringProperty("???");
		this.endDate = new SimpleStringProperty("???");
		photos = new ArrayList<>();
		currIndex = -1;
	}
	
	/**
	 * Album constructor with photos
	 * @param name album's name
	 * @param newList list of photos
	 */
	public Album(String name, List<Photo> newList) {
		this(name);
		for(Photo photo: newList) {
			Photo newPhoto = new Photo(photo);
			photos.add(newPhoto);
		}
		if (photos.size() > 0) {
			currIndex = 0;
		}
	}

	/**
	 * @param o album to compare to
	 */
	public int compareTo(Album o) {
		return getName().compareToIgnoreCase(o.getName());
	}
	
	public String toString() {
		return getName();
	}
	
	/**
	 * Preps the album for a file operation
	 * @param write true if writing, false if reading
	 */
	public void filePrep(boolean write) {
		if (write) {
			keepName = getName();
			name = null;
			photoNum = null;
			startDate = null;
			endDate = null;
		} else {
			name = new SimpleStringProperty(keepName);
			photoNum = new SimpleIntegerProperty();
			startDate = new SimpleStringProperty();
			endDate = new SimpleStringProperty();
			keepName = null;
		}
		
		for (Photo photo: photos) {
			photo.filePrep(write);
		}
	}
	
	/**
	 * @return album's name
	 */
	public String getName() {
		return name.get();
	}
	
	/**
	 * sets the album's name
	 * @param newName album's new name
	 */
	public void setName(String newName) {
		name.set(newName);
	}
	
	/**
	 * @return list of photos in the album
	 */
	public List<Photo> getPhotoList() {
		return photos;
	}
	
	public int getSize() {
		return photos.size();
	}
	
	/**
	 * @return date of earliest photo in the album
	 */
	public String getStartDate() {
		return startDate.get();
	}
	
	/**
	 * Sets the date of the album's earliest photo
	 * @param newStart new date for earliest photo
	 */
	public void setStartDate(String newStart) {
		startDate.set(newStart);
	}
	
	/**
	 * @return date of latest photo in the album
	 */
	public String getEndDate() {
		return endDate.get();
	}
	
	/**
	 * Sets the date of the latest photo in the album
	 * @param newEnd new date for latest photo
	 */
	public void setEndDate(String newEnd) {
		endDate.set(newEnd);
	}
	
	/**
	 * @return number of photos in the album
	 */
	public int getPhotoNum() {
		return photoNum.get();
	}
	
	/**
	 * sets how many photos the album has
	 * @param newNum new number of album photos
	 */
	public void setPhotoNum(int newNum) {
		photoNum.set(newNum);
	}
	
	/**
	 * @return current photo's index
	 */
	public int getCurrIndex() {
		return checkCurrIndex();
	}
	
	/**
	 * Sets the index of the current photo
	 * @param newIndex new index for the current photo
	 * @return new index for the current photo
	 */
	public int setCurrIndex(int newIndex) {
		currIndex = newIndex;
		return checkCurrIndex();
	}
	
	/**
	 * makes sure the current index is a valid one
	 * @return a valid current index
	 */
	private int checkCurrIndex() {
		if (photos.size() == 0) {
			currIndex = -1;
		} else if (currIndex > photos.size() - 1) {
			currIndex = photos.size() - 1;
		} else if (currIndex < 0) {
			currIndex = 0;
		}
		return currIndex;
	}
	
	/**
	 * @return photo in the current index
	 */
	public Photo getCurrPhoto() {
		checkCurrIndex();
		if (currIndex >= 0) {
			return photos.get(currIndex);
		} else {
			return null;
		}
	}
	
	/**
	 * Adds a photo to the album
	 * @param photo the photo to be added
	 */
	public void addPhoto(Photo photo) {
		photos.add(photo);
		currIndex = photos.size() - 1;
	}
	
	/**
	 * Adds a photo into a specified index
	 * @param oldPhoto photo where the new photo will be inserted
	 * @param newPhoto photo to add
	 * @return index of the new photo
	 */
	public int addPhoto(Photo oldPhoto, Photo newPhoto) {
		int index;
		if (oldPhoto != null) {
			index = IntStream.range(0, photos.size()).filter(i -> oldPhoto == photos.get(i)).findFirst().orElse(-1);
		} else {
			index = photos.size();
		}
		photos.add(index, newPhoto);
		return index;
	}
	
	/**
	 * deletes photo from the album
	 * @param photo the photo to be deleted
	 * @return index of deleted photo, -1 if nothing deleted
	 */
	public int deletePhoto(Photo photo) {
		for (int i = 0; i < photos.size(); i++) {
			if (photo == photos.get(i)) {
				photos.remove(i);
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * returns the photo in the provided index
	 * @param pos index of photo
	 * @return photo in album at index
	 */
	public Photo getPhoto(int pos) {
		if (pos < photos.size() && pos >= 0) {
			return photos.get(pos);
		} else {
			return null;
		}
	}
	
	/**
	 * searches through photos based on tags
	 * @param tags the tags to be looked for
	 * @return list of photos matching tags
	 */
	public List<Photo> searchTags(TagSearch tags) {
		BiPredicate<Photo, TagSearch> pred = (t, k) -> ListOps.search(t.getTags(), k.getTags(), (c, p) -> c.compareTo(p) == 0);
		
		if (!tags.getOr() || tags.getTags().size() < 2) {
			return ListOps.filterList(photos, tags, pred);
		} else {
			TagSearch tag1 = new TagSearch();
			TagSearch tag2 = new TagSearch();
			ObservableList<Tag> tempTags = tags.getTags();
			
			tag1.addTag(tempTags.get(0));
			tag2.addTag(tempTags.get(1));
			
			List<Photo> list1 = ListOps.filterList(photos, tag1, pred);
			List<Photo> list2 = ListOps.filterList(photos, tag2, pred);
			
			for(Photo photo: list1) {
				ListOps.addToList(list2, photo);
			}
			return list2;
		}
	}
	
	/**
	 * Searches through photos by date
	 * @param times the time range to look for
	 * @return list of filtered photos
	 */
	public List<Photo> searchDate(TimeSearch times) {
		BiPredicate<Photo,TimeSearch> pred = Photo::withinRange;
		return ListOps.filterList(photos, times, pred);
	}
	
	/**
	 * finds index of provided photo
	 * @param photo Photo who's index is to be found
	 * @return index of photo, -1 if it isn't in the album
	 */
	public int getIndex(Photo photo) {
		return IntStream.range(0, photos.size()).filter(o -> photo == photos.get(o)).findFirst().orElse(-1);
	}
	
	/**
	 * Refreshes the size and earliest/latest dates in the album
	 */
	public void setSizeAndDate() {
		if (photos.size() == 0) {
			setPhotoNum(0);
			setStartDate("???");
			setEndDate("???");
			return;
		}
		long min = photos.get(0).getDate();
		long max = photos.get(0).getDate();
		int count = 0;
		for (Photo photo: photos) {
			if (photo.getDate() > max) {
				max = photo.getDate();
			} else if (photo.getDate() < min) {
				min = photo.getDate();
			}
			count++;
		}
		setPhotoNum(count);
		setEndDate(Photo.dateToLocal(max));
		setStartDate(Photo.dateToLocal(min));
	}
}