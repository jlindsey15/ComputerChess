import java.util.ArrayList;


public class King extends ChessPiece {
	private static final String kingSpriteFilenameWhite = "ChessPiecesImages/KingWhite.png"; //image file location
	private static final String kingSpriteFilenameNonWhite = "ChessPiecesImages/KingBlack.png";
	public boolean castleAllowed = true;
	public King(int theColumn, int theRow, boolean whiteTeam) { //constructor
		super(theColumn, theRow, whiteTeam, (whiteTeam) ? kingSpriteFilenameWhite : kingSpriteFilenameNonWhite);
		weight = Player.KING_WEIGHT;
		
	}
	
	public int[][] getBlackPS() { 
		//gets the piece's black piece square table. Note that this is more complicated for kings,
		// since we interpolate between the opening game table and end game table
		// depending on whether a lot of pieces have been lost
		
		int[][] returned = new int[8][8];
		double howFar = Math.min(ChessGame.currentPlayer.getMyTeam().size(), ChessGame.otherPlayer.getMyTeam().size())/ 20.0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				returned[i][j] = (int)(PieceSquare.blackKingMiddle[i][j] * howFar + PieceSquare.blackKingEnd[i][j] * (1.0 - howFar));
			}
		}
		return returned;
	}
	public ArrayList<Position> possibleSwagMoves() { //returns all possible moves for a king
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
		
		
		returned = removeInvalid(returned); //removes friendly fire moves and other stupid things
		
		return returned;
	}

}
