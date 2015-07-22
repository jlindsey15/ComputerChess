import java.util.ArrayList;


public class Bishop extends ChessPiece{ 
	//Bishop class
	private static final String bishopSpriteFilenameWhite = "ChessPiecesImages/BishopWhite.png";; //locations of image files
	private static final String bishopSpriteFilenameNonWhite = "ChessPiecesImages/BishopBlack.png";;
	
	public Bishop(int theColumn, int theRow, boolean whiteTeam) { //constructor
		super(theColumn, theRow, whiteTeam, (whiteTeam) ? bishopSpriteFilenameWhite : bishopSpriteFilenameNonWhite);
		weight = Player.BISHOP_WEIGHT; //Material weight for bishop

	}
	public int[][] getBlackPS() { 
		//gets the piece square table for a black bishop.  White bishop's PS tables are accessed by replacing row with 7-row
		return PieceSquare.blackBishop;
	}
	public ArrayList<Position> possibleSwagMoves() { //possible moves for a bishop

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
		returned = removeInvalid(returned); //removes moves that are off the board, kill your own piece, etc.
		return returned;


	}


}
