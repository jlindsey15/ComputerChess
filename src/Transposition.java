import java.util.ArrayList;
import java.util.Arrays;

public class Transposition {
	public static ArrayList<TranBoard> transpositionTable = new ArrayList<TranBoard>();
	
	public static void addBoard(TranBoard tran) {
		///transpositionTable.add(tran.);
		
	}
	
	public static TranBoard returnEval(ChessPiece[][] board) {
		for (TranBoard tran : transpositionTable) {
			if (Arrays.deepEquals(board,  tran.board)) {
				return tran;
			}
		}
		return null;
		
	}
	
}