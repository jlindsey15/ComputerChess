import java.util.ArrayList;

public class Undo {

	public static void undo(){
		System.out.println("Attempting Undo");
		//try{
		if(lastBlackMoves.size()==0)
			return;
		undoHalf(lastBlackMoves.get(0));
		lastBlackMoves.remove(0);
		undoHalf(lastWhiteMoves.get(0));
		lastWhiteMoves.remove(0);
		//} catch(Exception e){}//#YOLO
		ChessApplication.UpdateDisplay();
		//ChessBoard.printBoard();
	}
	
	public static void undoHalf(MoveInfo m){
		if (m == null)
			return;
		int oldColumn = m.oldColumn,oldRow = m.oldRow;
		Position pos = m.pos;
		boolean storedCastleAllowed = m.storedCastleAllowed;
		Move move = m.move;
		ChessPiece piece = m.piece;
		Rook rook = m.rook;
		ChessPiece oldOccupant = m.oldOccupant;
		if (move.isCastle) {
			ChessBoard.undoCastle(piece, rook);
		}
		else {
			//more undoing:
			//System.out.println(oldColumn+" "+oldRow);
			//System.out.println(piece.getColumn()+"   "+piece.getRow());
			ChessBoard.move(piece,  new Position(oldColumn, oldRow));
			piece.setPosition(new Position(oldColumn,oldRow));
			//System.out.println(piece.getColumn()+"   "+piece.getRow());
			ChessBoard.setChessPiece(pos.column,  pos.row,  oldOccupant);
			if (piece instanceof Pawn) {
				((Pawn) piece).hasMoved = storedCastleAllowed;//Ignore funky naming
			}
			if (piece instanceof Rook) {
				((Rook)(piece)).castleAllowed = storedCastleAllowed;

			}
			else if (piece instanceof King) {
				((King)(piece)).castleAllowed = storedCastleAllowed;
			}
		}
		m.piece.player.hasCastled = m.storedHasCastled;
		PieceSquare.whiteScore = m.psw;
		PieceSquare.blackScore = m.psb;
		System.out.println("whitescore " + PieceSquare.whiteScore + "blackscore " + PieceSquare.blackScore);
		m = null;
	}
	
	public static ArrayList<MoveInfo> lastBlackMoves = new ArrayList<MoveInfo>();
	public static ArrayList<MoveInfo> lastWhiteMoves = new ArrayList<MoveInfo>();
	
	
}
