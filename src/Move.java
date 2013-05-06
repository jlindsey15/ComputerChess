
public class Move implements Comparable { 
	//Move class.  Moves contain a piece and a position to move to, as well as castling information if applicable.  
	//The Comparable implementation might be useful for future move sorting, but isn't used as of now.
	public Position position;
	public ChessPiece piece;
	public boolean isCastle = false;
	public Move(Position pos, ChessPiece thing) {
		position = pos;
		piece = thing;
	}
	
	public int compareTo(Object theMove) { //not used yet but could be
		Move otherMove = (Move)theMove;
		return (ChessBoard.getBoard()[position.column][position.row].weight - piece.weight - (ChessBoard.getBoard()[otherMove.position.column][otherMove.position.row].weight - otherMove.piece.weight));
		
	}
	public int evaluateMove() { //as above, not used currently
		int oldColumn = this.piece.getColumn();
		int oldRow = this.piece.getRow();
		ChessPiece oldOccupant = ChessBoard.getBoard()[this.position.column][this.position.row];
		ChessBoard.move(this.piece,  this.position);
		int score = Player.evaluateBoard(ChessBoard.getBoard(), this.piece.player);
		ChessBoard.move(this.piece, new Position(oldColumn, oldRow));
		ChessBoard.setChessPiece(this.position.column,  this.position.row, oldOccupant);
		return score;
	}
	public boolean equals(Move other) { 
		//checks if two moves have the same piece and position.  
		//Necessary because identical moves might have different references in computer memory
		try {
		if (!(this.piece.getColumn() == other.piece.getColumn())) {
			return false;
		}
		if (!(this.piece.getRow() == other.piece.getRow())) {
			return false;
		}
		if (!(this.position.column == other.position.column)) {
			return false;
		}
		if (!(this.position.row == other.position.row)) {
			return false;
		}
		return true;
		}
		catch (NullPointerException e) { //if one of the move's characteristics is null, something messed up...
			return false;
		}
	}

}
