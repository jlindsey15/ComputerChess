import java.util.ArrayList;


public class Rook extends ChessPiece {
	private static final String rookSpriteFilenameWhite = "ChessPiecesImages/RookWhite.png"; //image file locations
	private static final String rookSpriteFilenameNonWhite = "ChessPiecesImages/RookBlack.png";
	public boolean castleAllowed = true;
	public Rook(int theColumn, int theRow, boolean whiteTeam) { //constructor
		super(theColumn, theRow, whiteTeam, (whiteTeam) ? rookSpriteFilenameWhite : rookSpriteFilenameNonWhite);
		weight = Player.ROOK_WEIGHT;
	}
	public int[][] getBlackPS() { //returns black rook piece square table
		return PieceSquare.blackRook;
	}
	public ArrayList<Position> possibleSwagMoves() {
		ArrayList<Position> returned = new ArrayList<Position>();
		//gives all the possible "lines" of movement
		ArrayList<Position> right = getPositionsInDirection(1, 0);
		ArrayList<Position> left = getPositionsInDirection(-1, 0);
		ArrayList<Position> up = getPositionsInDirection(0, 1);
		ArrayList<Position> down = getPositionsInDirection(0, -1);
		ArrayList<ArrayList<Position>> meta = new ArrayList<ArrayList<Position>>();
		meta.add(right);
		meta.add(left);
		meta.add(up);
		meta.add(down);
		meta = ignoreAfterObstruction(meta); //can't move through a piece that's in the way  - remove those extra spaces at the end
		for (ArrayList<Position> line : meta) {
			returned.addAll(line);
		}
		returned = removeInvalid(returned);
		//System.out.println("rook " + returned.size());
		return returned;

	}

}
