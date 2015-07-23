import java.util.ArrayList;


public class King extends ChessPiece {
	private static final String kingSpriteFilenameWhite = "ChessPiecesImages/KingWhite.png";
	private static final String kingSpriteFilenameNonWhite = "ChessPiecesImages/KingBlack.png";
	public King(int theColumn, int theRow, boolean whiteTeam) { //constructor
		super(theColumn, theRow, whiteTeam, (whiteTeam) ? kingSpriteFilenameWhite : kingSpriteFilenameNonWhite);		
	}
	public int[][] getBlackPS() {
		return PieceSquare.blackKingMiddle;
	}
	public ArrayList<Position> possibleMoves() { //returns all possible moves for a king
		ArrayList<Position> returned = new ArrayList<Position>();
		for (int i = -1; i <=1; i++) { //defines the square around the king, not including the king's space
			for (int j = -1; j <=1; j++) {
				if ((i == 0) && (j == 0)) { //don't include the space the king's already on
					;
				}
				else {
					
					Position toBeAdded = new Position(getColumn() + i, getRow() + j);
					if (toBeAdded.isValid()) {
						returned.add(toBeAdded);
					}
				}
			}
		}
		returned = removeInvalid(returned); //removes friendly fire moves

		return returned;
	}

	public String toString() {
		return "K";
	}
}
