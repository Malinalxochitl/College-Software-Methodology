package model;

/**
 * 
 * @author Joel Kurian
 * @author Jinseong Lee
 *
 */

public class Bishop extends Piece{

	/**
	 * @param color		The color of the chess piece
	 */
	public Bishop(boolean color) {
		super(color);
		type = "bishop";
	}
	
	/** 
	 * @param inp		player's input command
	 * @param board		Array of tiles used as the chess board
	 * @return boolean
	 * @see model.Piece#ValidMove(java.lang.String, model.Tile[][])
	 * 
	 * Returns true if the player's input is a valid move, false otherwise
	 */
	public boolean ValidMove(String inp, Tile[][] board) {
		
		String[] input = inp.split(" ");
		char initCol = input[0].charAt(0);
		char initRow = input[0].charAt(1);
		char newCol = input[1].charAt(0);
		char newRow = input[1].charAt(1);
		
		if (Math.abs(initCol - newCol) == Math.abs(initRow - newRow) && !piecesBetween(initCol, initRow, newCol, newRow, board)) {
			if (board[newCol - 'a'][newRow - 49].getPiece() == null) {
				return true;
			} else if (board[newCol - 'a'][newRow - 49].getPiece().white() != board[initCol-'a'][initRow-49].getPiece().white()) {
				return true;
			}
		}
		return false;
	}

	/** 
	 * @param inp		player's input command
	 * @param board		Array of tiles used as the chess board
	 * @return Tile[][]
	 * @see model.Piece#move(java.lang.String, model.Tile[][])
	 * 
	 * Returns the updated chess board after moving the chess piece specified in the input
	 */
	public Tile[][] move(String inp, Tile[][] board) {
		
		String[] input = inp.split(" ");
		char initCol = input[0].charAt(0);
		char initRow = input[0].charAt(1);
		char newCol = input[1].charAt(0);
		char newRow = input[1].charAt(1);
		
		Piece piece = board[initCol - 'a'][initRow - 49].getPiece();
		
		board[newCol - 'a'][newRow - 49].setPiece(piece);
		board[initCol - 'a'][initRow - 49].setPiece(null);
		
		moved();
		
		return board;
	}


}