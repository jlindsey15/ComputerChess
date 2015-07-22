import java.util.ArrayList;


public class Pawn extends ChessPiece {
	private static final String pawnSpriteFilenameWhite = "ChessPiecesImages/PawnWhite.png"; //image file location
	private static final String pawnSpriteFilenameNonWhite = "ChessPiecesImages/PawnBlack.png";
	public boolean hasMoved = false;

	public Pawn(int theColumn, int theRow, boolean whiteTeam) { //constructor
		super(theColumn, theRow, whiteTeam, (whiteTeam) ? pawnSpriteFilenameWhite : pawnSpriteFilenameNonWhite);
		weight = Player.BISHOP_WEIGHT;
	
		
	}
	public int[][] getBlackPS() { //returns black pawn piece square table
		return PieceSquare.blackPawn;
	}
	public ArrayList<Position> possibleSwagMoves() {
		ArrayList<Position> returned = new ArrayList<Position>();
		int upOrDown;
		if (isOnWhiteTeam) { //white pawns move up
			upOrDown = 1;
		}
		else { //black pawns move down
			upOrDown = -1;
		}
		
		if (!hasMoved) { //forward 2
			Position toBeAdded = new Position(getColumn(), getRow() + 2 * upOrDown);
			if (toBeAdded.isValid() && !ChessBoard.isOccupied(getColumn(), getRow() + upOrDown) && !ChessBoard.isOccupied(getColumn(), getRow() + (2 * upOrDown))) {
				returned.add(new Position(getColumn(), getRow() + 2 * upOrDown));
			}
		}
		Position toBeAdded = new Position(getColumn(), getRow() + upOrDown);
		if (toBeAdded.isValid() && !ChessBoard.isOccupied(getColumn(), getRow() + upOrDown)) { //space in front, unless occupied
			returned.add(new Position(getColumn(), getRow() + upOrDown));
		}
		toBeAdded = new Position(getColumn() + 1, getRow() + upOrDown);
		if (toBeAdded.isValid() && ChessBoard.isOccupied(getColumn() + 1, getRow() + upOrDown)) { //diagonal if occupied (ff's removed later)
			returned.add(toBeAdded);
		}
		toBeAdded = new Position(getColumn() - 1, getRow() + upOrDown); 
		if (toBeAdded.isValid() && ChessBoard.isOccupied(getColumn() - 1, getRow() + upOrDown)) { //same as above
			returned.add(toBeAdded);
		}
		returned = removeInvalid(returned); //removes friendly fire and invalid spaces
		return returned;
	}

}
