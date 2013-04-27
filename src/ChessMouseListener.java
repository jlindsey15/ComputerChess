import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class ChessMouseListener implements MouseListener {
	public void mouseClicked(MouseEvent event) {}
	public void mouseEntered(MouseEvent event) {}
	public void mouseExited(MouseEvent event) {}
	public void mouseReleased(MouseEvent event) {}
	
	public void mousePressed(MouseEvent event) {
		int row = (event.getX()/100) ;
		int column = (event.getY()/100) ;
								
		ChessBoard.SelectionMade(row, column, true);
	}
}
