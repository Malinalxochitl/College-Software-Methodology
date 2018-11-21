package model;

/**
 * 
 * @author Joel Kurian
 * @author Jinseong Lee
 *
 */

public class Queen extends Piece {

	/**
	 * @param color		the color of the chess piece
	 */
	public Queen(boolean color) {
		super(color);
		type = "queen";
	}

	/** 
	 * @param inp		the player's input command
	 * @param board		the current chess board
	 * @return boolean
	 * @see model.Piece#ValidMove(java.lang.String, model.Tile[][])
	 * 
	 * returns true if the specified move is valid for a queen, false otherwise
	 */
	@Override
	public boolean ValidMove(String inp, Tile[][] board) {
	
		String[] args = inp.split(" ");
		char initCol = args[0].charAt(0);
		char initRow = args[0].charAt(1);
		char newCol = args[1].charAt(0);
		char newRow = args[1].charAt(1);
		
		if ((Math.abs(initCol - newCol) == Math.abs(initRow - newRow) 
				|| ((initCol == newCol && initRow != newRow) || (initCol != newCol && initRow == newRow)))
				&& !piecesBetween(initCol, initRow, newCol, newRow, board)) {
			if (board[newCol - 'a'][newRow - 49].getPiece() == null
				|| board[newCol - 'a'][newRow - 49].getPiece().white() != white()) {
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
	 * Returns the updated chess board after performing the specified move
	 */
	@Override
	public Tile[][] move(String inp, Tile[][] board) {
	
		String[] input = inp.split(" ");
		char initCol = input[0].charAt(0);
		char initRow = input[0].charAt(1);
		char newCol = input[1].charAt(0);
		char newRow = input[1].charAt(1);
		
		Piece initPiece = board[initCol - 'a'][initRow - 49].getPiece();
		
		board[newCol - 'a'][newRow - 49].setPiece(initPiece);
		board[initCol - 'a'][initRow - 49].setPiece(null);
		
		moved(); 
		
		return board;
	}

}
	