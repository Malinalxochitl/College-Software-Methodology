package model;

/**
 * 
 * @author Joel Kurian
 * @author Jinseong Lee
 *
 */

public class Knight extends Piece {

	/**
	 * @param color		The color of the piece
	 */
	public Knight(boolean color) {
		super(color);
		type = "knight";
	}

	/** 
	 * @param inp		Player's input command
	 * @param board		The current chess board
	 * @return	boolean
	 * @see model.Piece#ValidMove(java.lang.String, model.Tile[][])
	 * 
	 * Returns true if the player input is a valid move for a knight, false otherwise
	 */
	@Override
	public boolean ValidMove(String inp, Tile[][] board) {
		String[] input = inp.split(" ");
		char initCol = input[0].charAt(0);
		char initRow = input[0].charAt(1);
		char newCol = input[1].charAt(0);
		char newRow = input[1].charAt(1);
		
		if (Math.abs(initCol - newCol) == 1 && Math.abs(initRow - newRow) == 2) { //2 vertical, 1 horizontal
			if(board[newCol - 'a'][newRow - 49].getPiece() == null //no piece or different color
					|| board[initCol - 'a'][initRow - 49].getPiece().white() != board[newCol - 'a'][newRow - 49].getPiece().white()) {
				return true;
			}
		} else if (Math.abs(initCol - newCol) == 2 && Math.abs(initRow - newRow) == 1) { //1 vertical, 2 horizontal
			if(board[newCol - 'a'][newRow - 49].getPiece() == null //no piece or different color
					|| board[initCol - 'a'][initRow - 49].getPiece().white() != board[newCol - 'a'][newRow - 49].getPiece().white()) {
				return true;
			}
		}
		return false;
	}

	/** 
	 * @param inp		The player's input command
	 * @param board		The current chess board
	 * @return	Tile[][]
	 * @see model.Piece#move(java.lang.String, model.Tile[][])
	 * 
	 * Returns an updated chess board after performing the move specified in the player's input
	 */
	@Override
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