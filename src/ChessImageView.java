import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class ChessImageView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	
	/**
	 * Constructor for the image view object
	 * 
	 * @param filename - The filename for the image
	 */
	public ChessImageView(String filename) {
		if (filename == "") return;
		try {
			image = ImageIO.read(new File(filename));
		} catch (IOException ex) {
			System.out.println("Unable to load image file: " + filename + "\n\n\n");
			ex.printStackTrace();
		}
		
		this.setSize(image.getWidth(), image.getHeight());
		this.setPreferredSize(new Dimension(image.getWidth()-20, image.getHeight()-20));
	}
	
	/**
	 * The draw method for the image view
	 * Called whenever the screen is rerendering
	 */
	public void paintComponent(Graphics g) {		
		super.paintComponent(g);
		g.drawImage(image, 0, 0, image.getWidth()-20, image.getHeight()-20, 0, 0, image.getWidth(), image.getHeight(), null);
	}
}
