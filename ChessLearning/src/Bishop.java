import java.util.ArrayList;


public class Bishop extends ChessPiece{
	private static final String bishopSpriteFilenameWhite = "ChessPiecesImages/BishopWhite.png";;
	private static final String bishopSpriteFilenameNonWhite = "ChessPiecesImages/BishopBlack.png";;
	
	public Bishop(int theColumn, int theRow, boolean whiteTeam) { //constructor
		super(theColumn, theRow, whiteTeam, (whiteTeam) ? bishopSpriteFilenameWhite : bishopSpriteFilenameNonWhite);
	}
	public int[][] getBlackPS() {
		return PieceSquare.blackBishop;
	}
	public ArrayList<Position> possibleMoves() { //possible moves for a bishop

		ArrayList<Position> returned = new ArrayList<Position>();
		//gives all the possible "lines" of movement
		ArrayList<Position> rightUp = getPositionsInDirection(1, 1);
		ArrayList<Position> leftUp = getPositionsInDirection(-1, 1);
		ArrayList<Position> rightDown = getPositionsInDirection(1, -1);
		ArrayList<Position> leftDown = getPositionsInDirection(-1, -1);
		ArrayList<ArrayList<Position>> meta = new ArrayList<ArrayList<Position>>();
		meta.add(rightUp);
		meta.add(leftUp);
		meta.add(rightDown);
		meta.add(leftDown);
		meta = ignoreAfterObstruction(meta); //can't move through a piece that's in the way  - remove those extra spaces at the end
		for (ArrayList<Position> line : meta) {
			returned.addAll(line);
		}
		returned = removeInvalid(returned);
		return returned;


	}

	public String toString() {
		return "B";
	}
}
