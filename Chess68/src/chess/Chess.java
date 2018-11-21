package chess;

import java.util.Scanner;
import model.Board;

/**
 * 
 * @author Joel Kurian
 * @author Jinseong Lee
 *
 */

public class Chess {
		private static Board game;
		private static Scanner scan = new Scanner(System.in);
		private static boolean DrawCheck;
		
		/**
		 * @param args starts the program
		 */
		public static void main(String[] args){
			start();
		}
		
		/**
		 * start()
		 * 
		 * Starts the game and takes user input
		 */
		public static void start(){
			
			boolean ValidInput, ValidMove;
			String input;
			game = new Board();
			
			while (!game.Done()){
				
				game.drawBoard();
				game.askInput();
				input = scan.nextLine();
				ValidInput = ValidInput(input);
				ValidMove = true;
				
				if (ValidInput){
					ValidMove = game.ValidMove(input);
				}
				
				while(!ValidInput || !ValidMove){	
					
					if(!ValidMove){
						System.out.println("Illegal move, try again");
						System.out.println("Use the following format: fileRank fileRank");
					}
					
					game.askInput();
					input = scan.nextLine();
					ValidInput = ValidInput(input);
					ValidMove = true;
					if (ValidInput){
						ValidMove = game.ValidMove(input);
					}
				}
				
				if (input.matches("[abcdefgh][12345678] [abcdefgh][12345678] draw[?]")) {
					DrawCheck = true;
				} else {
					DrawCheck = false;
				}
				
				game.move(input);
				System.out.println();
			}
		}
		
		/**
		 * @param input		The player's input command
		 * @return boolean
		 * 
		 * Returns true if the player input a correct command, false otherwise
		 */
		public static boolean ValidInput(String input){
			
			if (input.trim().equals("draw") && DrawCheck == true){
				System.out.println("draw");
				System.exit(0);
			}
			
			if (input.trim().equals("resign")){
				if(game.whiteTurn()){
					System.out.println("Black Wins");
				}
				else {
					System.out.println("White wins");
				}
				System.exit(0);
			}
			
			return input.matches("[abcdefgh][12345678] [abcdefgh][12345678]") || input.matches("[abcdefgh][12345678] [abcdefgh][12345678] draw[?]") || input.matches("[abcdefgh][12345678] [abcdefgh][12345678] [BRNQ]");
		}
}
