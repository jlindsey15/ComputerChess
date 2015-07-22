import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Openings {
	 
	public static String address = "openings.jlo";
	public static ArrayList<String> sequences = new ArrayList<String>();
	
	public static void initiate(){
		
		Scanner file = null;
		
		file = new Scanner(Openings.class.getResourceAsStream(address));
			
		
		
		while(file.hasNext())
			sequences.add(file.nextLine());
		file.close();
		System.out.println("Tablebase Load Succeeded");
		
	}
	
	public static Move query(){
		ArrayList<String> possible = new ArrayList<String>();
		String game = getAlgebraic();
		for(int n = 0; n < sequences.size(); n++){
			if(sequences.get(n).contains(game)){
				 possible.add((new Scanner(sequences.get(n).substring(game.length()))).next());
			}
		}
		if(possible.size()==0){
			System.out.println("Houston, we have a problem");
			return null;
		}
		System.out.println("Can has tablebaseburger");
		//System.out.println(possible.get(0));
		//return getMove(possible.get(0));
		return getMove(possible.get(new Random().nextInt(possible.size())));
	}
	
	public static Move getMove(String move){//Convert algebraic to move 
		System.out.println("The next move is"+move);
		ArrayList<ChessPiece> possible = new ArrayList<ChessPiece>();
		Player player = ChessGame.currentPlayer;
		if(move.contains("Bishop")){
			for (ChessPiece n : player.myBishops){
				possible.add(n);
			}
		}
		else if(move.contains("King")){
			for (ChessPiece n : player.myKings){
				possible.add(n);
			}
		}
		else if(move.contains("Knight")){
			for (ChessPiece n : player.myKnights){
				possible.add(n);
			}
		}
		else if(move.contains("Queen")){
			for (ChessPiece n : player.myQueens){
				possible.add(n);
			}
		}
		else if(move.contains("Rook")){
			for (ChessPiece n : player.myRooks){
				possible.add(n);
			}
		}
		else{
			for (ChessPiece n : player.myPawns){
				possible.add(n);
			}
		}
		Position correct = null;
		System.out.println("Roflcopter "+possible.size());
		ArrayList<ChessPiece> actuallyPossible = new ArrayList<ChessPiece>();
		String letters = "abcdefgh";
		for(ChessPiece n : possible){
			for(Position m : n.possibleMoves()){
				int col,row;
				col = m.column;
				row = m.row;
				String check = letters.charAt(col)+Integer.toString(row+1);
				System.out.println("Checking "+move+" for piece "+(n.isOnWhiteTeam?"White ":"Black ")+possible.indexOf(n)+" to "+check);
				if(move.contains(check)){
					actuallyPossible.add(n);
					correct = m;
				}
			}
		}
		System.out.println("Lolzers "+actuallyPossible.size());
		if(actuallyPossible.size()==1){
			Move out = new Move(correct,actuallyPossible.get(0));
			out.moveinfo = new MoveInfo(out,actuallyPossible.get(0),false, false, null, 0, 0, 0, 0);
			System.out.println("Suggesting "+actuallyPossible.get(0)+" to "+correct.column+", "+correct.row);
			return out;
		}
		return null;
	}
	
	public static String getAlgebraic(){
		String out = "";
		int count = 1;
		try {
		out+=(getMoveAlgebraic(Undo.lastWhiteMoves.get(Undo.lastWhiteMoves.size()-1),count));
		for(int n = Undo.lastBlackMoves.size()-1;n >=0; n--){
			out+=(getMoveAlgebraic(Undo.lastBlackMoves.get(n),0));
			count++;
			out+=(getMoveAlgebraic(Undo.lastWhiteMoves.get(n),count));
		}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			return out;
		}
		System.out.println(out);
		return out;
	}
	
	public static String getMoveAlgebraic(MoveInfo carlos, int count){
		if(carlos.move.isCastle){
			if(carlos.rook.getColumn()==0){
				return "O-O ";
			}
			if(carlos.rook.getColumn()==7){
				return "O-O-O ";
			}
		}
		String out = "";
		String letters = "abcdefgh";
		if(count!=0)
			out+=(Integer.toString(count))+".";
		if(carlos.piece instanceof Pawn){//I AM PROBABLY SCREWING STUFF UP <<<<<<<<<
			//if(carlos.oldOccupant!=null)
				//out+=letters.charAt(carlos.oldColumn);
		}
		else if(carlos.piece instanceof Bishop){
			out+="Bishop";
		}
		else if(carlos.piece instanceof King){
			out+="King";
		}
		else if(carlos.piece instanceof Knight){
			out+="Knight";
		}
		else if(carlos.piece instanceof Queen){
			out+="Queen";
		}
		else if(carlos.piece instanceof Rook){
			out+="Rook";
		}
		out+=letters.charAt(carlos.move.position.column);
		out+=Integer.toString(carlos.move.position.row+1);
		out+=" ";
		return out;
	}
	
}
