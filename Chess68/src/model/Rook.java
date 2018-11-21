package model;

/**
 * 
 * @author Joel Kurian
 * @author Jinseong Lee
 *
 */

public class Rook extends Piece {

	/**
	 * @param color		the color of the chess piece
	 */
	public Rook(boolean color) {
		super(color);
		type = "rook";
	}
	
	/** 
	 * @param inp		The player's input command
	 * @param board		The current chess board
	 * @return boolean
	 * @see model.Piece#ValidMove(java.lang.String, model.Tile[][])
	 * 
	 * Returns true if the player input is a valid move for a rook, false otherwise
	 */
	@Override
	public boolean ValidMove(String inp, Tile[][] board) {
		
		String[] input = inp.split(" ");
		char initCol = input[0].charAt(0);
		char initRow = input[0].charAt(1);
		char newCol = input[1].charAt(0);
		char newRow = input[1].charAt(1);
		
		if ( ((initCol == newCol && initRow != newRow) || (initCol != newCol && initRow == newRow)) 
				&& !piecesBetween(initCol, initRow, newCol, newRow, board)) {
			if (board[newCol - 'a'][newRow - 49].getPiece() == null) {
				return true;
			} else if (board[newCol - 'a'][newRow - 49].getPiece().white() != board[initCol - 'a'][initRow - 49].getPiece().white()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param inp		The player's input command
	 * @param board		The current chess board
	 * @return Tile[][]
	 * @see model.Piece#move(java.lang.String, model.Tile[][])
	 * 
	 * Returns the updated chess board after performing the player's indicated move
	 */
	@Override
	public Tile[][] move(String inp, Tile[][] board) {
		
		String[] input = inp.split(" ");
		char initCol = input[0].charAt(0);
		char initRow = input[0].charAt(1);
		char newCol = input[1].charAt(0);
		char newRow = input[1].charAt(1);
		
		Piece initPiece = board[initCol - 'a'][initRow - 49].getPiece();
		
		board[newCol-'a'][newRow - 49].setPiece(initPiece);
		board[initCol-'a'][initRow - 49].setPiece(null);
		moved(); 
		return board;
	}

}
	