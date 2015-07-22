//import com.apple.eawt.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JDialog;

public class ChessApplication {
	//The window object
	static JFrame frame;
	public static JPanel thePanel;
	public static boolean humanWhite = true;
	
	/**
	 * Initializes the window. It also constructs the window and sets all the proper properties. It then adds
	 * the panel which contains all of the UI elements needed to construct the window
	 * 
=======
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChessApplication {
	private static JFrame frame;

	/**
	 * Initializes the window. It also constructs the window and sets all the proper properties. It then adds
	 * the panel which contains all of the UI elements needed to construct the window
	 *
>>>>>>> 085c4706ee13d5463101df2eadb23ae94916230f
	 * @param width - The width of the window
	 * @param height - The height of the window
	 */
	public static void InitializeChessApplication(int width, int height) {
		getOptions();
		//Create the main window
		Openings.initiate();
		frame = new JFrame("The Eviscerator 6000");
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


		
		if (!humanWhite) ChessGame.UpdateGame();
		//JPanel p = new JPanel();
		//p.setLayout(new BorderLayout());
		//p.add(new JMenuBar(), BorderLayout.NORTH);
	}
	
	/**
	 * Creates the interface for the window, it constructs all of the UI elements into a hierarchy,
	 * which are all inside the panel object that is returned to the initialize method
	 * 
=======
		frame = new JFrame("Chess Bot Application");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(width, height);

		frame.getContentPane().add(CreateInterface(width, height));

		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Creates the interface for the window, it constructs all of the UI elements into a hierarchy,
	 * which are all inside the panel object that is returned to the initialize method
	 *
>>>>>>> 085c4706ee13d5463101df2eadb23ae94916230f
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
=======
		JPanel panel = new JPanel();
		panel.setSize(width, height);
		panel.setPreferredSize(new Dimension(width, height));

		GridLayout layout = new GridLayout(0, 8);
		layout.setHgap(2);
		layout.setVgap(2);
		panel.setLayout(layout);

		CreateChessBoard(panel, width, height);

		return panel;
	}

	/**
	 * Creates the chess board. Uses the panel object passing in through the parameter to add a chess board too it.
	 * Creates a board with dynamically sized chess board tiles
	 *
>>>>>>> 085c4706ee13d5463101df2eadb23ae94916230f
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
	
	public static void getOptions() {
		String[] options = {"White", "Black"};
	     JOptionPane pane = new JOptionPane("Would you like to play as White or Black?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
	     pane.setOptions(options);

	     JDialog dialog = pane.createDialog(null, "");
	     
	     dialog.show();
	     Object selectedValue = pane.getValue();
	     	    
	     //If there is an array of option buttons:
	     int counter;
	        for (counter = 0; counter < 2; counter++) {
	        if(pane.getOptions()[counter].equals(selectedValue))
	        break;
	     }
	    ChessApplication.humanWhite = (counter == 0);
	    String timePerMove = JOptionPane.showInputDialog(null, "How many seconds do you want the computer to take for each move?");
	    try {
	    	ChessGame.timePerMove = Integer.parseInt(timePerMove);
	    }
	    catch (NumberFormatException e) {
	    	ChessGame.timePerMove = 10;
	    }
	}

	private static void CreateChessBoard(JPanel panel, int width, int height) {
		int gridSize = (int)(height/8);

		for (int y = 0; y < 8; ++y) {
			for (int x = 0; x < 8; ++x) {
				JPanel gridPanel = new JPanel();
				gridPanel.setSize(gridSize, gridSize);
				if (x % 2 == 0 && y % 2 == 0) gridPanel.setBackground(Color.CYAN);
				else if (x % 2 != 0 && y % 2 == 0) gridPanel.setBackground(Color.RED);
				else if (x % 2 == 0 && y % 2 != 0) gridPanel.setBackground(Color.RED);
				else gridPanel.setBackground(Color.CYAN);

				panel.add(gridPanel);
			}
		}
	}
}
