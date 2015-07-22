import java.util.Scanner;
import javax.swing.JTextField;



//authors: Jack Lindsey and Jack Abeel
//date: 5/1/2013
//version: 1.1
//Decription: A computer chess opponent.  Uses minimax algorithms, alpha-beta pruning, material evaluation, piece-square boards, 
//pawn, king, and rook formation evaluation, and more advanced pruning algorithms.  Designed to play with a certain time limit on the game.

public class Chessbot { //main class
	public static  boolean shouldPonder = false;
	public static void main(String args[]) {
		
		
		ChessApplication.InitializeChessApplication(800, 800);
		/*while (true) {
			//pondering is kind of buggy and not that crucial so I'm going to leave it out for now
			
			//System.out.println("swag");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (!ChessGame.isComputersTurn && shouldPonder) {
				//System.out.println("pondering");  
				try {
					ChessGame.Ponder();
				} catch (Exception e) {
					// TODO Auto-generated catch block
						e.printStackTrace();
				}
				shouldPonder = false;
				//System.out.println("swag");
				
			}
			
			
		}*/
		
	}
}
