package model;

import java.io.Serializable;

/**
 * This class represents a tag for a photo
 * @author Joel Kurian
 * @author Jinseong Lee
 */
public class Tag implements Serializable, Comparable<Tag>{

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 7339287298662742472L;
	
	/**
	 * tag's name
	 */
	private String name;
	
	/**
	 * tag's value
	 */
	private String value;
	
	/**
	 * tag constructor
	 * @param name tag's name
	 * @param value tag's value
	 */
	public Tag(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * makes a copy of other tag
	 * @param other the other tag
	 */
	public Tag(Tag other) {
		name = other.name;
		value = other.value;
	}
	
	/**
	 * @return String version of tag
	 */
	public String toString() {
		return name + "=" + value;
	}
	
	/**
	 * checks whether tags are equal
	 * @param o the other tag
	 * @return 0 if tags are equal, 1 or -1 otherwise
	 */
	public int compareTo(Tag o) {
		int result = name.compareToIgnoreCase(o.name);
		if (result == 0) {
			return value.compareToIgnoreCase(o.value);
		} else {
			return result;
		}
	}

}
