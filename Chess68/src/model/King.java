package model;

/**
 * 
 * @author Joel Kurian
 * @author Jinseong Lee
 *
 */

public class King extends Piece {
	
	/**
	 * @param color		The color of the chess piece
	 */
	public King(boolean color) {
		super(color);
		type = "king";
	}

	/** 
	 * @param inp		Player's input command
	 * @param board		The current chess board
	 * @return boolean
	 * @see model.Piece#ValidMove(java.lang.String, model.Tile[][])
	 * 
	 * Returns true if the input is a valid move for a king, false otherwise
	 */
	public boolean ValidMove(String inp, Tile[][] board) {
		String input[] = inp.split(" ");
		char initCol = input[0].charAt(0);
		char initRow = input[0].charAt(1);
		char newCol = input[1].charAt(0);
		char newRow = input[1].charAt(1);
		
		if(initCol == newCol && Math.abs(initRow - newRow) == 1
			&& ((board[newCol - 'a'][newRow - 49]).getPiece() == null || board[newCol - 'a'][newRow - 49].getPiece().white != white)) {
			return true;
		} else if (initRow == newRow && Math.abs(initCol - newCol) == 1 
				&& ((board[newCol - 'a'][newRow - 49].getPiece() == null || board[newCol - 'a'][newRow - 49].getPiece().white != white))) {
			return true;
		} else if (Math.abs(initCol - newCol) == 1 && Math.abs(initRow - newRow) == 1
				&& (board[newCol - 'a'][newRow - 49].getPiece() == null || board[newCol - 'a'][newRow - 49].getPiece().white != white)) {
			return true;
		} else if (!isMoved) {
			if (newCol == 'g' && (newRow - 49 == 0 || newRow - 49 == 7)
					&& board[7][newRow - 49].getPiece() instanceof Rook
					&& !board[7][newRow - 49].getPiece().isMoved()
					&& board[7][newRow - 49].getPiece().white == white
					&& !piecesBetween(initCol, initRow, 'h', newRow, board)) {
				return true;
			} else if (newCol == 'c' && (newRow - 49 == 0 || newRow - 49 == 7)
					&& board[0][newRow - 49].getPiece() instanceof Rook
					&& !board[0][newRow - 49].getPiece().isMoved()
					&& board[0][newRow - 49].getPiece().white == white
					&& !piecesBetween(initCol, initRow, 'a', newRow, board)) {
				return true;
			}
		}
		return false;
	}

	/** (non-Javadoc)
	 * @param inp		The Player's input command
	 * @param board		The current chess board
	 * @return Tile[][]
	 * @see model.Piece#move(java.lang.String, model.Tile[][])
	 * 
	 * Returns the updated chess board after doing the move specified in the input
	 */
	public Tile[][] move(String inp, Tile[][] board) {
		String[] input = inp.split(" ");
		char initCol = input[0].charAt(0);
		char initRow = input[0].charAt(1);
		char newCol = input[1].charAt(0);
		char newRow = input[1].charAt(1);
		
		if(!isMoved() && newCol == 'g' && (newRow - 49 == 0 || newRow - 49 == 7)
				&& board[7][newRow - 49].getPiece() instanceof Rook
				&& !board[7][newRow - 49].getPiece().isMoved()
				&& board[7][newRow - 49].getPiece().white == white
				&& !piecesBetween(initCol, initRow, 'h', newRow, board)) {
			board = castle(initCol, initRow, 'h', newRow, board);
			
		} else if (!isMoved() && newCol == 'c' && (newRow - 49 == 0 || newRow - 49 == 7)
					&& board[0][newRow - 49].getPiece() instanceof Rook
					&& !board[0][newRow - 49].getPiece().isMoved()
					&& board[0][newRow - 49].getPiece().white == white
					&& !piecesBetween(initCol, initRow, 'a', newRow, board)) {
			board = castle(initCol, initRow, 'a', newRow, board);
			
		} else {
			Piece piece = board[initCol - 'a'][initRow - 49].getPiece();
			board[newCol - 'a'][newRow - 49].setPiece(piece);
			board[initCol - 'a'][initRow - 49].setPiece(null);
		}
		moved();
		return board;
	}

	/**
	 * @param kingCol		The column that the king occupies
	 * @param kingRow		The row that the king occupies
	 * @param rookCol		The column that the rook occupies
	 * @param rookRow		The row that the rook occupies
	 * @param board			The current chess board
	 * @return	Tile[][]
	 * 
	 * Returns the chess board after doing a castle maneuver with the specified pieces
	 */
	private Tile[][] castle(char kingCol, char kingRow, char rookCol, char rookRow, Tile[][] board) {
		Piece king = board[kingCol - 'a'][kingRow - 49].getPiece();
		Piece rook = board[rookCol - 'a'][rookRow - 49].getPiece();
		
		if (rookCol == 'h') {
			board[kingCol - 'a' + 2][kingRow - 49].setPiece(king);
			board[rookCol - 'a' - 2][rookRow - 49].setPiece(rook);
			board[kingCol - 'a'][kingRow - 49].setPiece(null);
			board[rookCol - 'a'][rookRow - 49].setPiece(null);
		} else if (rookCol == 'a') {
			board[kingCol - 'a' - 2][kingRow - 49].setPiece(king);
			board[rookCol - 'a' + 3][rookCol - 49].setPiece(rook);
			board[kingCol - 'a'][kingRow - 49].setPiece(null);
			board[rookCol - 'a'][rookRow - 49].setPiece(null);
		}
		return board;
	}
}
