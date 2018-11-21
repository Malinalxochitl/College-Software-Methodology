package model;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class manages search by tag data
 * @author Joel Kurian
 * @author Jinseong Lee
 */
public class TagSearch implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 6221328024591031220L;
	
	/**
	 * list of tags (arraylist for storage)
	 */
	private ObservableList<Tag> tagList;
	private ArrayList<Tag> keepTagList;
	
	/**
	 * decides disjunction/conjunction search
	 */
	private boolean orSearch;
	
	/**
	 * TagSearch constructor
	 */
	public TagSearch() {
		tagList = FXCollections.observableArrayList();
	}
	
	/**
	 * Preps lists for file operation
	 * @param write true if writing to file, false if reading
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
	 * @return the list of tags
	 */
	public ObservableList<Tag> getTags() {
		return tagList;
	}
	
	/**
	 * Adds a tag to the tag list
	 * @param name tag's name
	 * @param value tag's value
	 * @return true if tag was added, false if not
	 */
	public boolean addTag(String name, String value) {
		Tag tag = new Tag(name, value);
		
		return ListOps.addToList(tagList, tag);
	}
	
	/**
	 * Adds an existing tag to the tag list
	 * @param tag the tag to be added
	 * @return true if tag was added, false if not
	 */
	public boolean addTag(Tag tag) {
		Tag newTag = new Tag(tag);
		
		return ListOps.addToList(tagList, newTag);
	}
	
	/**
	 * @return true if search by disjunction
	 */
	public boolean getOr() {
		return orSearch;
	}
	
	/**
	 * Sets the search to disjunction (true) or conjunction (false)
	 * @param bool sets orSearch to this value
	 */
	public void setOr(boolean bool) {
		orSearch = bool;
	}
}
