package model;

/**
 * 
 * @author Joel Kurian
 * @author Jinseong Lee
 *
 */

public class Pawn extends Piece {

	private boolean enpass;
	private boolean willEnpass;
	
	/**
	 * @param color		The color of the chess piece
	 */
	public Pawn(boolean color) {
		super(color);
		type = "pawn";
		enpass = true;
		willEnpass = false;
	}

	/**
	 * @param inp		The player's input command
	 * @param board		The current chess board
	 * @return boolean
	 * @see model.Piece#ValidMove(java.lang.String, model.Tile[][])
	 * 
	 * Returns true if the specified move is valid for a pawn, false otherwise
	 */
	public boolean ValidMove(String inp, Tile[][] board) {
		String[] input = inp.split(" ");
		char initCol = input[0].charAt(0);
		char initRow = input[0].charAt(1);
		char newCol = input[1].charAt(0);
		char newRow = input[1].charAt(1);
		
		if (initCol == newCol) { //pawn going straight
			if (white() && newRow == initRow + 1 && board[newCol - 'a'][newRow - 49].getPiece() == null) {
				enpass = false;
				return true;
			} else if (white() && !isMoved() && newRow == initRow + 2 && board[newCol - 'a'][newRow - 49].getPiece() == null) {
				enpass = true;
				return true;
			} else if (!white() && newRow == initRow - 1 && board[newCol - 'a'][newRow - 49].getPiece() == null) {
				enpass = false;
				return true;
			} else if (!white() && !isMoved() && newRow == initRow - 2 && board[newCol - 'a'][newRow - 49].getPiece() == null) {
				enpass = true;
				return true;
			}
		}
		
		if (white() && newRow == initRow + 1) { //direct diagonal capture
			if (newCol == initCol + 1 
					&& board[initCol - 'a' + 1][initRow - 49 + 1].getPiece() != null 
					&& !board[initCol - 'a' + 1][initRow - 49 + 1].getPiece().white()) {
				enpass = false;
				return true;
			} else if (newCol == initCol - 1
					&& board[initCol - 'a' - 1][initRow - 49 + 1].getPiece() != null
					&& !board[initCol - 'a' - 1][initRow - 49 + 1].getPiece().white()) {
				enpass = false;
				return true;
			}
		} else if (!white() && newRow == initRow - 1){
			if (newCol == initCol + 1
					&& board[initCol - 'a' + 1][initRow - 49 - 1].getPiece() != null
					&& !board[initCol - 'a' + 1][initRow - 49 - 1].getPiece().white) {
				enpass = false;
				return true;
			} else if (newCol == initCol - 1
					&& board[initCol - 'a' - 1][initRow - 49 - 1].getPiece() != null
					&& board[initCol - 'a' - 1][initRow - 49 - 1].getPiece().white()) {
				enpass = false;
				return true;
			}
		}
		
		if (white() && initRow == '5' && newRow == '6') {
			if (newCol == initCol + 1 
					&& board[initCol - 'a' + 1][initRow - 49].getPiece() != null
					&& !board[initCol - 'a' + 1][initRow - 49].getPiece().white()
					&& canEnpass(initCol - 'a' + 1, initRow - 49, board) 
					&& board[newCol - 'a'][newRow - 49].getPiece() == null) {
				enpass = false;
				willEnpass = true;
				return true;
			} else if (newCol == initCol - 1
					&& board[initCol - 'a' - 1][initRow - 49].getPiece() != null
					&& !board[initCol - 'a' - 1][initRow - 49].getPiece().white()
					&& canEnpass(initCol - 'a' - 1, initRow - 49, board)
					&& board[newCol - 'a'][newRow - 49].getPiece() == null) {
				enpass = false;
				willEnpass = true;
				return true;
			}
		} else if (!white() && initRow == '4' && newRow == '3') {
			if (newCol == initCol + 1
					&& board[initCol - 'a' + 1][initRow - 49].getPiece() != null
					&& board[initCol - 'a' + 1][initRow - 49].getPiece().white()
					&& canEnpass(initCol - 'a' + 1, initRow - 49, board)
					&& board[newCol - 'a'][newRow - 49].getPiece() == null) {
				enpass = false;
				willEnpass = true;
				return true;
			} else if (newCol == initCol - 1
					&& board[initCol - 'a' - 1][initRow - 49].getPiece() != null
					&& board[initCol - 'a' - 1][initRow - 49].getPiece().white()
					&& canEnpass(initCol - 'a' - 1, initRow - 49, board)
					&& board[newCol - 'a'][newRow - 49].getPiece() == null) {
				enpass = false;
				willEnpass = true;
				return true;
			}
		}
		return false;
	}

	/**
	 * @param col		Column of the piece to en passant
	 * @param row		Row of the piece to en passant
	 * @param board		The current chess board
	 * @return boolean
	 * 
	 * Returns true if the specified piece can perform an en passant on a pawn, false otherwise
	 */
	private boolean canEnpass(int col, int row, Tile[][] board) {
		
		boolean enpassable = false;
		boolean white = board[col][row].getPiece().white();
		
		if (white && row > 0 && col < 7
				&& board[col + 1][row].getPiece() != null
				&& !board[col + 1][row].getPiece().white()) {
			enpassable = true;
		} else if (white && row > 0 && col > 0
				&& board[col - 1][row].getPiece() != null
				&& !board[col - 1][row].getPiece().white()) {
			enpassable = true;
		} else if (white && row < 7 && col < 7
				&& board[col + 1][row].getPiece() != null
				&& !board[col + 1][row].getPiece().white()) {
			enpassable = true;
		} else if (white && row < 7 && col > 0
				&& board[col - 1][row].getPiece() != null
				&& !board[col - 1][row].getPiece().white()) {
			enpassable = true;
		}
		
		return board[col][row].getPiece().getEnpassant() && enpassable;
	}

	/**
	 * @param inp		The player's input command
	 * @param board		The current chess board
	 * @return Tile[][]
	 * @see model.Piece#move(java.lang.String, model.Tile[][])
	 * 
	 * returns the updated chess board after performing the move specified in the player input
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
		
		if (newCol != initCol && willEnpass) {
			if (white() && newRow == initRow + 1) {
				if (newCol == initCol + 1) {
					board[initCol - 'a' + 1][initRow - 49].setPiece(null);
				} else if (newCol == initCol - 1) {
					board[initCol - 'a' - 1][initRow - 49].setPiece(null);
				}
			} else if (!white() && newRow == initRow - 1) {
				if (newCol == initCol + 1) {
					board[initCol - 'a' + 1][initRow - 49].setPiece(null);
				} else if (newCol == initCol - 1) {
					board[initCol - 'a' - 1][initRow - 49].setPiece(null);
				}
			}
		}
		
		if (white() && newRow == '8') {
			promotion(inp, newCol, newRow, board, white());
		} else if (!white() && newRow == '1') {
			promotion(inp, newCol, newRow, board, white());
		}
		
		moved();
		
		return board;
	}
	
	/**
	 * @return boolean
	 * @see model.Piece#getEnpassant()
	 * 
	 * returns true if the piece can be en passanted, false otherwise
	 */
	public boolean getEnpassant() {
		return enpass;
	}

	/**
	 * @param inp		the player's input command
	 * @param col		the pawn's column
	 * @param row		the pawn's row
	 * @param board		the current chess board
	 * @param color		the pawn's color
	 * 
	 * promotes the pawn to the specified piece.
	 * the pawn is promoted to queen by default
	 */
	private void promotion(String inp, char col, char row, Tile[][] board, boolean color) {
		String[] input = inp.split(" ");
		if (input.length == 3) {
			if (input[2] == "B") {
				board[col - 'a'][row - 49].setPiece(new Bishop(color));
			} else if (input[2] == "Q") {
				board[col - 'a'][row - 49].setPiece(new Queen(color));
			} else if (input[2] == "N") {
				board[col - 'a'][row - 49].setPiece(new Knight(color));
			} else if (input[2] == "R") {
				board[col - 'a'][row - 49].setPiece(new Rook(color));
			} else {
				board[col - 'a'][row - 49].setPiece(new Queen(color));
			}
		} else {
			board[col - 'a'][row - 49].setPiece(new Queen(color));
		}
	}

}
	