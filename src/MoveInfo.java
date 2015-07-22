
public class MoveInfo {

	Position pos;
	ChessPiece piece;
	Move move;
	int oldColumn,oldRow;
	boolean storedCastleAllowed, storedHasCastled;
	Rook rook;
	ChessPiece oldOccupant;
	int psw,psb;
	
	public MoveInfo(Move move,ChessPiece oldOccupant,boolean storedCastleAllowed, boolean storedHasCastled, Rook rook,int oldRow,int oldColumn,int psw,int psb){
		piece = move.piece;
		pos = move.position;
		this.move = move;
		this.oldColumn = oldColumn;
		this.oldRow = oldRow;
		this.storedCastleAllowed = storedCastleAllowed;
		this.storedHasCastled = storedHasCastled;
		this.rook = rook;
		this.oldOccupant = oldOccupant;
		this.psw = psw;
		this.psb = psb;
	}
	
}
