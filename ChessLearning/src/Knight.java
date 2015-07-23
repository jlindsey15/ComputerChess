import java.util.ArrayList;


public class Knight extends ChessPiece {
	private static final String knightSpriteFilenameWhite = "ChessPiecesImages/KnightWhite.png";
	private static final String knightSpriteFilenameNonWhite = "ChessPiecesImages/KnightBlack.png";
	public Knight(int theColumn, int theRow, boolean whiteTeam) { //constructor
		super(theColumn, theRow, whiteTeam, (whiteTeam) ? knightSpriteFilenameWhite : knightSpriteFilenameNonWhite);		
	}
	public int[][] getBlackPS() {
		return PieceSquare.blackKnight;
	}
	public ArrayList<Position> possibleMoves() { //all possible knight moves - this is how knights work...
		ArrayList<Position> returned = new ArrayList<Position>();
		int c = getColumn();
		int r = getRow();
		Position toBeAdded;
		toBeAdded = new Position(c + 2, r + 1);
		if (toBeAdded.isValid()) {
			returned.add(toBeAdded);
		}
		toBeAdded = new Position(c + 2, r - 1);
		if (toBeAdded.isValid()) {
			returned.add(toBeAdded);
		}
		toBeAdded = new Position(c - 2, r + 1);
		if (toBeAdded.isValid()) {
			returned.add(toBeAdded);
		}
		toBeAdded = new Position(c - 2, r - 1);
		if (toBeAdded.isValid()) {
			returned.add(toBeAdded);
		}
		toBeAdded = new Position(c + 1, r + 2);
		if (toBeAdded.isValid()) {
			returned.add(toBeAdded);
		}
		toBeAdded = new Position(c + 1, r - 2);
		if (toBeAdded.isValid()) {
			returned.add(toBeAdded);
		}
		toBeAdded = new Position(c - 1, r + 2);
		if (toBeAdded.isValid()) {
			returned.add(toBeAdded);
		}
		toBeAdded = new Position(c - 1, r -2);
		if (toBeAdded.isValid()) {
			returned.add(toBeAdded);
		}
		returned = removeInvalid(returned);
		return returned;
	}

	public String toString() {
		return "N";
	}
}
