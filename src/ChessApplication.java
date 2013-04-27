import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChessApplication {
	//The window object
	private static JFrame frame;
	public static JPanel thePanel;
	
	/**
	 * Initializes the window. It also constructs the window and sets all the proper properties. It then adds
	 * the panel which contains all of the UI elements needed to construct the window
	 * 
	 * @param width - The width of the window
	 * @param height - The height of the window
	 */
	public static void InitializeChessApplication(int width, int height) {
		//Create the main window
		frame = new JFrame("Chess Bot Application");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		
		ChessMouseListener listener = new ChessMouseListener();
		frame.addMouseListener(listener);
		
		//Add the UI to the window
		frame.getContentPane().add(CreateInterface(width, height));
		
		//Set the window to visible and start it
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Creates the interface for the window, it constructs all of the UI elements into a hierarchy,
	 * which are all inside the panel object that is returned to the initialize method
	 * 
	 * @param width - The width of the window
	 * @param height - The height of the window
	 * @return The panel which contains all of the UI elements
	 */
	private static JPanel CreateInterface(int width, int height) {
		//Create the contents panel
		JPanel panel = new JPanel();
		panel.setSize(width, height);
		panel.setPreferredSize(new Dimension(width, height));
		panel.setBackground(Color.GRAY);
		
		//Create the chess board
		CreateChessBoard(panel, width, height, Color.WHITE, Color.BLACK);
		thePanel = panel;
		return panel;
	}
	
	/**
	 * Creates the chess board. Uses the panel object passing in through the parameter to add a chess board too it.
	 * Creates a board with dynamically sized chess board tiles
	 * 
	 * @param panel - The panel to add the tile too
	 * @param width - The width of the board in pixels
	 * @param height - The height of the board in pixels
	 */
	private static void CreateChessBoard(JPanel panel, int width, int height, Color firstTileColor, Color secondTileColor) {
		ChessBoard.Initialize();
		ChessGame.InitializeGame();
		panel.add(ChessBoard.GenerateChessBoard(width, height, firstTileColor, secondTileColor));
	}
	
	public static void UpdateDisplay() {
		frame.paintAll(frame.getGraphics());
	}
}
