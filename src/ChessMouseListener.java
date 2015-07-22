import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class ChessMouseListener implements MouseListener { //checks for mouse clicks on board
	public void mouseClicked(MouseEvent event) {}
	public void mouseEntered(MouseEvent event) {}
	public void mouseExited(MouseEvent event) {}
	public void mouseReleased(MouseEvent event) {}
	
	public void mousePressed(MouseEvent event) {
		ChessGame.isComputersTurn = true;
		
		if(event.isControlDown()&&event.isShiftDown()){
			while (true) {
				System.out.println("swagging");
				if (!ChessGame.currentlyPondering) {
					Undo.undo();
					break;
				}
			}
			
			return;
		}
		int row = (event.getX()/100) ;
		int column = (event.getY()/100) ;
								
		while (true) {
			System.out.println("swagging");
			if (!ChessGame.currentlyPondering) {
				ChessBoard.SelectionMade(row, column, ChessGame.currentPlayer.isOnWhiteTeam);
				break;
			}
		}
	}
}
