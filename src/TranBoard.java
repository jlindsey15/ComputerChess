
public class TranBoard {
	public ChessPiece[][] board;
	public int evaluation;
	public int depth;
	public String type;
	
	public TranBoard(ChessPiece[][] pieces, int eval, int depth, String type) {
		board = pieces;
		evaluation = eval;
		this.depth = depth;
		this.type = type;
		
	}

}
