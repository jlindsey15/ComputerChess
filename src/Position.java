
public class Position {
	//Position class.  Contains row, column, and castle information if applicable.
	public int row;
	public int column;
	public boolean isCastle = false;
	public Rook myRook = null;
	public Position(int c, int r) {
		row = r;
		column = c;
	}
	public boolean isValid() {
		//false if outside board dimensions.
		if (row < 0) {
			return false;
		}
		if (row >7) {
			return false;
		}
		if (column < 0) {
			return false;
		}
		if (column >7) {
			return false;
		}
		return true;
	}

}
