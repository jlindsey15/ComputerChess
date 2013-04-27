
public class Position {
	public int row;
	public int column;
	public Position(int c, int r) {
		row = r;
		column = c;
	}
	public boolean isValid() {
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
