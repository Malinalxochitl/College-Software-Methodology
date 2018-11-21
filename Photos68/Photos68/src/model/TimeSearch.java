package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Search paramaters for a search by date
 * @author Joel Kurian
 * @author Jinseong Lee
 */
public class TimeSearch implements Serializable{

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -6550615284411564064L;
	
	/**
	 * Start date of range
	 */
	private LocalDate start;
	
	/**
	 * end date of range
	 */
	private LocalDate end;
	
	/**
	 * TimeSearch constructor
	 * @param start the start of the range
	 * @param end the end of the range
	 */
	public TimeSearch(LocalDate start, LocalDate end) {
		this.start = start;
		this.end = end;
	}
	
	/**
	 * TimeSearch default constructor
	 */
	public TimeSearch() {
		this.start = null;
		this.end = null;
	}

	/**
	 * @return start date of the range
	 */
	public LocalDate getStart() {
		return start;
	}
	
	/**
	 * changes the start date
	 * @param newStart new start date
	 */
	public void setStart(LocalDate newStart) {
		this.start = newStart;
	}
	
	/**
	 * @return the end of the range
	 */
	public LocalDate getEnd() {
		return end;
	}
	
	/**
	 * changes the end date
	 * @param newEnd new end date
	 */
	public void setEnd(LocalDate newEnd) {
		this.end = newEnd;
	}
}
