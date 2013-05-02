import java.util.ArrayList;


public class Queen extends ChessPiece {
	private static final String queenSpriteFilenameWhite = "ChessPiecesImages/QueenWhite.png"; //image file location
	private static final String queenSpriteFilenameNonWhite = "ChessPiecesImages/QueenBlack.png";
	public Queen(int theColumn, int theRow, boolean whiteTeam) { //constructor
		super(theColumn, theRow, whiteTeam, (whiteTeam) ? queenSpriteFilenameWhite : queenSpriteFilenameNonWhite);
		weight = Player.QUEEN_WEIGHT;
		
	}
	public int[][] getBlackPS() { //returns black queen piece square table
		return PieceSquare.blackQueen;
	}
	public ArrayList<Position> possibleMoves() {
		ArrayList<Position> returned = new ArrayList<Position>();
		//gives all the possible "lines" of movement
		ArrayList<Position> rightUp = getPositionsInDirection(1, 1);
		ArrayList<Position> leftUp = getPositionsInDirection(-1, 1);
		ArrayList<Position> rightDown = getPositionsInDirection(1, -1);
		ArrayList<Position> leftDown = getPositionsInDirection(-1, -1);
		ArrayList<Position> right = getPositionsInDirection(1, 0);
		ArrayList<Position> left = getPositionsInDirection(-1, 0);
		ArrayList<Position> up = getPositionsInDirection(0, 1);
		ArrayList<Position> down = getPositionsInDirection(0, -1);
		
		ArrayList<ArrayList<Position>> meta = new ArrayList<ArrayList<Position>>(); 
		meta.add(right);
		meta.add(left);
		meta.add(up);
		meta.add(down);
		meta.add(rightUp);
		meta.add(leftUp);
		meta.add(rightDown);
		meta.add(leftDown);
		meta = ignoreAfterObstruction(meta); //can't move through a piece that's in the way  - remove those extra spaces at the end
		for (ArrayList<Position> line : meta) {
			returned.addAll(line);
		}
		returned = removeInvalid(returned); //remove friendly fire
		return returned;
	}

}
