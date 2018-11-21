package model;

/**
 * 
 * @author Joel Kurian
 * @author Jinseong Lee
 *
 */

public class Board{

	private Tile[][] board;
	private boolean whiteTurn;
	
	private boolean done;
	private boolean stalemate;
	private boolean resign;
	private boolean isWhiteWinner;
	private boolean inCheck;
	private boolean isWhiteInCheck;
	private boolean isBlackInCheck;
	private boolean drawCheck;
	


	/**
	 * Initializes the chess board
	 */
	public Board(){

		int row, col;

		board = new Tile[8][8];

		//row 8
		board[0][7] = new Tile(new Rook(false),false);
		board[1][7] = new Tile(new Knight(false),true);
		board[2][7] = new Tile(new Bishop(false),false);
		board[3][7] = new Tile(new Queen(false),true);
		board[4][7] = new Tile(new King(false),false);
		board[5][7] = new Tile(new Bishop(false),true);
		board[6][7] = new Tile(new Knight(false),false);
		board[7][7] = new Tile(new Rook(false),true);

		//row 7
		row = 6;
		for (col = 0; col <= 7; col++){
			if (col % 2 == 0){
				board[col][row] = new Tile(new Pawn(false), false);
			}
			else {
				board[col][row] = new Tile(new Pawn(false), true);
			}
		}

		//rows 3-6

		for (row = 5; row >= 2; row--){
			for(col = 0; col <= 7; col++){
				if ((col % 2 == 0 && row%2 == 0) || (col % 2 != 0 && row%2 != 0)){
					board[col][row] = new Tile(false);
				}
				else{
					board[col][row] = new Tile(true);
				}
			
			}
		}

		//row 2
		row = 1; 
		for(col = 0; col <= 7; col++)
		{
			if (col % 2 == 0)
				board[col][row] = new Tile(new Pawn(true),true);
			else
				board[col][row] = new Tile(new Pawn(true),false);	
		}


		//row 1
		board[0][0] = new Tile(new Rook(true),false);
		board[1][0] = new Tile(new Knight(true),true);
		board[2][0] = new Tile(new Bishop(true),false);
		board[3][0] = new Tile(new Queen(true),true);
		board[4][0] = new Tile(new King(true),false);
		board[5][0] = new Tile(new Bishop(true),true);
		board[6][0] = new Tile(new Knight(true),false);
		board[7][0] = new Tile(new Rook(true),true);

		whiteTurn = true;

	}

	/**
	 * Draws the chess board in console
	 */
	public void drawBoard(){
		int row, column;

		for (row = 7; row >=0; row--){
			for (column = 0; column < 8; column++){

				if(board[column][row].getPiece() != null){
					System.out.print(board[column][row] + " ");
				}
				else{
					if (board[column][row].isTileBlack()) {System.out.print("   ");}
					else {System.out.print("## ");}
				}
			}
			System.out.println(" " + (row+1));
		}

		System.out.println(" a  b  c  d  e  f  g  h");
		System.out.println();
	}


	/**
	 * Asks the specified player for input depending on the player turn
	 */
	public void askInput(){
		if (whiteTurn){
			System.out.print("White's move: ");
		}
		else {
			System.out.print("Black's move: ");
		}
	}
	
	/**
	 * @param inp Player's input
	 * 
	 * Reads the player's input and moves it on the chess board
	 */
	public void move(String inp){

		String[] input = inp.split(" ");
		char initCol = input[0].charAt(0);
		char initRow = input[0].charAt(1);
		
		drawCheck = false;
		
		if (input.length == 3 && input[2].equals("draw?")) {
			drawCheck = true;
		}
			
		
		board = board[initCol-'a'][initRow-49].getPiece().move(inp, board);
		
		testCheck(board); //tests for check and checkmate
		
		changePlayer();
	}

	/**
	 * @param board		Tile Array for storing pieces and simulating a chess board
	 * 
	 * Checks whether the king is in check
	 * if so, then check for a checkmate
	 */
	private void testCheck(Tile[][] board) {
		if (kingCheck(board, !whiteTurn())) {
			inCheck = true;
			if (!testCheckMate()) {
				System.out.println("\nCheck");
			}
		} else {
			inCheck = false;
			testStalemate();
		}
		
	}

	/**
	 * Checks for a stalemate NOT IMPLEMENTED YET
	 */
	private void testStalemate() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return boolean
	 * 
	 * Returns true if the king is in checkmate, false if not
	 */
	private boolean testCheckMate() {
		char col, row;
		for (int c = 0; c <= 7; c++) {
			for (int r = 0; r <= 7; r++) {
				col = (char) (c + 'a');
				row = (char) (r + 49);
				
				if (board[c][r].getPiece() != null && board[c][r].getPiece().white() != whiteTurn()) {
					if (canMove(col, row)) {
						return false;
					}
				}
			}
		}
		
		if (whiteTurn()) {
			isWhiteWinner = true;
			System.out.println("Checkmate\nWhite wins\n");
		} else {
			isWhiteWinner = false;
			System.out.println("Checkmate\nBlack wins\n");
		}
		done = true;
		return true;
	}

	/**
	 * @param initCol		Initial column of chess piece
	 * @param initRow		Initial row of chess piece
	 * @return boolean
	 * 
	 * Returns true if the chess piece in the specified Tile can move, false otherwise
	 */
	private boolean canMove(char initCol, char initRow) {
		char newCol, newRow;
		String result = "";
		String input = initCol + "" + initRow + "";
		
		for (int c = 0; c <= 7; c++) {
			for (int r = 0; r <= 7; r++) {
				if (board[c][r].getPiece() instanceof King) {
					continue;
				}
				newCol = (char) (c + 'a');
				newRow = (char) (r + 49);
				result = newCol + "" + newRow + "";
				
				if (board[initCol - 'a'][initRow - 49].getPiece().ValidMove(input + " " + result, board)) {
					Tile[][] copy = makeCopy(board);
					copy = copy[initCol - 'a'][initRow - 49].getPiece().move(input + " " + result, copy);
					if (!kingCheck(copy, !whiteTurn) && !kingCheck(copy, whiteTurn)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Switches the player turn
	 */
	public void changePlayer(){
		whiteTurn = !whiteTurn;
	}
	 /**
	 * @return the current player turn
	 */
	public boolean whiteTurn() {
		 return whiteTurn;
	 }

	/**
	 * @param inp		The current player's input
	 * @return boolean
	 * 
	 * returns true if the inputed command is a valid move, returns false otherwise
	 */
	public boolean ValidMove(String inp) {
		String[] input = inp.split(" ");
		char initCol = input[0].charAt(0);
		char initRow = input[0].charAt(1);
		char newCol = input[1].charAt(0);
		char newRow = input[1].charAt(1);
		boolean valid;
		
		if ((initCol == newCol && initRow == newRow)
				|| board[initCol - 'a'][initRow - 49].getPiece() == null
				|| board[initCol - 'a'][initRow - 49].getPiece().white != whiteTurn()
				|| board[newCol - 'a'][newRow - 49].getPiece() instanceof King) {
			return false;
		} else if (input.length == 3 && !(board[initCol - 'a'][initRow - 49].getPiece() instanceof Pawn)) {
			return false;
		} else {
			valid = board[initCol - 'a'][initRow - 49].getPiece().ValidMove(inp, board);
			if (valid) {
				Tile[][] copy = makeCopy(board);
				boolean moved = board[initCol - 'a'][initRow - 49].getPiece().isMoved();
				copy = copy[initCol - 'a'][initRow - 49].getPiece().move(inp, copy);
				board[initCol - 'a'][initRow - 49].getPiece().isMovedHelper(moved);
				return !kingCheck(copy, whiteTurn());
			}
		}
		return valid;
	}

	/**
	 * @param copy		clone of the current chess board for testing moves
	 * @param white		the color of the king to be tested
	 * @return boolean
	 * 
	 * Returns true if the king is in check, false otherwise
	 */
	private boolean kingCheck(Tile[][] copy, boolean white) {
		String kingPos = findKing(copy, white);
		char initCol, initRow;
		String input = "", testinput = "";
		for (int c = 0; c <= 7; c++) {
			for(int r = 0; r <= 7; r++) {
				initCol = (char) (c + 'a');
				initRow = (char) (r + 49);
				
				input = initCol + "" + initRow + "";
				testinput = input + " " + kingPos;
				
				if (copy[c][r].getPiece() != null
						&& copy[c][r].getPiece().white() != white
						&& !kingPos.equals(input) 
						&& copy[c][r].getPiece().ValidMove(testinput, copy)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param copy		clone of the current board for testing purposes
	 * @param white		color of the king to be searched for
	 * @return String
	 * 
	 * Returns the location of the specified king in the chess board
	 */
	private String findKing(Tile[][] copy, boolean white) {
		char col, row;
		for (int c = 0; c <= 7; c++) {
			for (int r = 0; r <= 7; r++) {
				if (copy[c][r].getPiece() != null
						&& copy[c][r].getPiece().white() == white
						&& copy[c][r].getPiece() instanceof King) {
					col = (char) (c + 'a');
					row = (char) (r + 49);
					return col + "" + row + "";
				}
			}
		}
		return null;
	}

	/**
	 * @param board		The chess board
	 * @return Tile[][]
	 * 
	 * Returns a clone of the current chess board
	 */
	private Tile[][] makeCopy(Tile[][] board) {
		Tile[][] copy = new Tile[8][8];
		
		for (int c = 0; c <= 7; c++) {
			for (int r = 0; r <= 7; r++) {
				try {
					copy[c][r] = (Tile) board[c][r].clone();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		}
		return copy;
	}

	/**
	 * @return boolean
	 * 
	 * Access method for checking whether the game is done
	 */
	public boolean Done() {
		return done;
	}
}