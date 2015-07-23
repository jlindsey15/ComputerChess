
public enum WinType {
	TERMINATION (1),
	WHITE_WIN (2),
	BLACK_WIN (3);
	
	public int value;
	
	WinType(int val) {
		this.value = val;
	}
}
