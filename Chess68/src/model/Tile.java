package model;

/**
 * 
 * @author Joel Kurian
 * @author Jinseong Lee
 *
 */

public class Tile implements Cloneable{
	
	private Piece currentPiece;
	private boolean isBlack;
	
	/**
	 * @param piece		The current piece occupying the tile
	 * @param color		The color of the tile
	 * 
	 * note that the tile keeps track of whether it is black,
	 * while the pieces keep track of whether they are white
	 */
	public Tile(Piece piece, boolean color) {
		currentPiece = piece;
		isBlack = color;
	}
	
	/**
	 * @param color		the color of the tile
	 */
	public Tile(boolean color) {
		this(null, color);
	}
	
	/**
	 * @param piece		A chess piece
	 * 
	 * sets the specified chess piece onto this tile
	 */
	public void setPiece (Piece piece) {
		currentPiece = piece;
	}
	
	/**
	 * @return the current chess piece
	 */
	public Piece getPiece() {
		return currentPiece;
	}
	
	/**
	 * @return true if the tile is black, false otherwise
	 */
	public boolean isTileBlack() {
		return isBlack;
	}
	
	/**
	 * @return String
	 * @see java.lang.Object#toString()
	 * returns the string form of the current piece
	 */
	public String toString() {
		return currentPiece.toString();
	}
	
	/** 
	 * @return Object
	 * @throws CloneNotSupportedException if object does not implement the cloneable interface
	 * @see java.lang.Object#clone()
	 * 
	 * returns a clone of the object
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
