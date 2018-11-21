package model;

/**
 * 
 * @author Joel Kurian
 * @author Jinseong Lee
 *
 */

public abstract class Piece implements Cloneable {
	
	boolean white;
	String type;
	boolean isMoved;

	/**
	 * @param color the color of the piece
	 */
	public Piece (boolean color){
		white = color;
		type = null;
		isMoved = false;
	}

	/**
	 * @param input		The player's input command
	 * @param board		The current chess board
	 * @return boolean
	 * 
	 * returns true if the move is valid for the piece, false otherwise
	 */
	public abstract boolean ValidMove(String input, Tile[][] board);

	/**
	 * @param input		the player's input command
	 * @param board		the current chess board
	 * @return Tile[][]
	 * 
	 * returns the updated chess board after performing the specified move
	 */
	public abstract Tile[][] move(String input, Tile[][] board);

	/** 
	 * @see java.lang.Object#clone()
	 * 
	 * creates a clone of the chess piece
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * @return boolean
	 * 
	 * returns true if the piece is white, false if it is black
	 */
	public boolean white(){
		return white;
	}

	/**
	 * @return boolean
	 * 
	 * returns true if the piece has been moved, false if it has not
	 */
	public boolean isMoved(){
		return isMoved;
	}

	/**
	 * Let the piece know that it has been moved
	 */
	public void moved(){
		isMoved = true;
	}

	/**
	 * @param b		true if it's been moved, false if not
	 * 
	 * helper method for manually setting if a piece has been moved or not
	 */
	public void isMovedHelper(boolean b){
		isMoved = b;
	}

	/**
	 * @param col		column of piece to test a check
	 * @param row		row of peice ot test a check
	 * @param board		the current chess board
	 * @return boolean	returns true if the check is a valid move, false otherwise
	 */
	public boolean testCheck (char col, char row, Tile[][] board){
		String c = col + "", r = row + "";
		String testCheckInput = c + r + " " + EnemyKingPosition(board, white());
		return ValidMove(testCheckInput, board);
	}

	/**
	 * @param board		the current chess board
	 * @param white		the color of the king to not search for
	 * @return String
	 * 
	 * Returns the location of the other player's king, or null if it was not found
	 */
	public String EnemyKingPosition(Tile[][] board, boolean white){
		String pos = "";
		char col, row;
		
		for (int c = 0; c <= 7 ; c++){
			for (int r = 0; r <= 7; r++){
				if (board[c][r].getPiece() != null && board[c][r].getPiece().white() != white && board[c][r].getPiece() instanceof King){
					col = (char)(c + 'a');
					row = (char)(r + 49);
					pos = "" + col + row;
					return pos;
				}
			}

		}
		return null;
	}


	/**
	 * @return boolean
	 * 
	 * returns true if a piece can en passant, false otherwise
	 * will always return false, except for pawns who override this method
	 */
	public boolean Enpassant(){
		return false;
	}

	/**
	 * @return boolean
	 * 
	 * returns true if a piece can be en passanted, false otherwise
	 * will always return false, except for pawns who override this method
	 */
	public boolean getEnpassant(){
		return false;
	}
	
	/** (non-Javadoc)
	 * @return String
	 * @see java.lang.Object#toString()
	 * 
	 * Returns a string representation of a piece
	 */
	public String toString() {
		String result;
		
		if (white) {
			result = "w";
		} else {
			result = "b";
		}
		
		switch(type) {
			case "king":
				result += "K";
				break;
			case "queen":
				result += "Q";
				break;
			case "pawn":
				result += "p";
				break;
			case "knight":
				result += "N";
				break;
			case "rook":
				result += "R";
				break;
			case "bishop":
				result += "B";
				break;
		}
		return result;
	}
	
	/**
	 * @param initCol		the initial column to be checked
	 * @param initRow		the initial row to be checked
	 * @param newCol		the last column to be checked
	 * @param newRow		the last row to be checked
	 * @param board			the current chess board
	 * @return boolean
	 * 
	 * Returns true if there are any pieces between the initial position and the final position, false otherwise
	 */
	public boolean piecesBetween(char initCol, char initRow, char newCol, char newRow, Tile[][] board) {
		int c, r, colDir = initCol - newCol, rowDir = initRow - newRow;
		if (initRow == newRow && initCol != newCol) { //left/right
			if (initCol < newCol) { //right
				for (c = initCol - 'a' + 1; c < newCol - 'a'; c++) {
					if (board[c][initRow-49].getPiece() != null) {
						return true;
					}
				}
			} else if (initCol > newCol) { //left
				for (c = initCol - 'a' - 1; c > newCol - 'a'; c--) {
					if (board[c][initRow-49].getPiece() != null) {
						return true;
					}
				}
			}
		} else if (initCol == newCol && initRow != newRow) { //up/down
			if (initRow < newRow) { //up
				for (r = initRow - 49 + 1; r < newRow - 49; r++) {
					if (board[initCol - 'a'][r].getPiece() != null) {
						return true;
					}
				}
			} else if (initRow > newRow) { //down
				for (r = initRow - 49 - 1; r > newRow - 49; r--) {
					if (board[initCol - 'a'][r].getPiece() != null) {
						return true;
					}
				}
			}
		} else if (Math.abs(rowDir) == Math.abs(colDir)) { //diagonal
			if (colDir < 0 && rowDir < 0) { //up/right
				r = initRow - 49 + 1;
				c = initCol - 'a' + 1;
				while (c < newCol - 'a') {
					if (board[c][r].getPiece() != null) {
						return true;
					}
					r++;
					c++;
				}
			} else if (colDir < 0 && rowDir > 0) { //down/right
				r = initRow - 49 - 1;
				c = initCol - 'a' + 1;
				while (c < newCol - 'a') {
					if (board[c][r].getPiece() != null) {
						return true;
					}
					r--;
					c++;
				}
			} else if (colDir > 0 && rowDir < 0) { //up/left
				r = initRow - 49 + 1;
				c = initCol - 'a' - 1;
				while (c < newCol - 'a') {
					if (board[c][r].getPiece() != null) {
						return true;
					}
					r++;
					c--;
				}
			} else if (colDir > 0 && rowDir > 0) { //down/left
				r = initRow - 49 - 1;
				c = initCol - 'a' - 1;
				while (c < newCol - 'a') {
					if (board[c][r].getPiece() != null) {
						return true;
					}
					r--;
					c--;
				}
			}
		}
		return false; //nonlinear movement
	}
}
