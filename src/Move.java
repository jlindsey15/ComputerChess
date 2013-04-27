
public class Move implements Comparable {
	public Position position;
	public ChessPiece piece;
	public Move(Position pos, ChessPiece thing) {
		position = pos;
		piece = thing;
	}
	
	public int compareTo(Object theMove) {
		return (((Move)theMove).evaluateMove() - this.evaluateMove());
		
	}
	public int evaluateMove() {
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
		catch (NullPointerException e) {
			return false;
		}
	}

}
