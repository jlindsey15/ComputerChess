import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

public abstract class ChessPiece { //superclass for all chess pieces
	//the player that owns the piece:
	public Player player;
	//The content panel
	protected JPanel panel;
	//The image view for the chess piece
	protected ChessImageView imageView;
	
	protected String imageFilename = ""; 
	//location of image file - overriden in subclasses
	//to be overriden
	public int weight = 0; 
	
	//start out alive
	
	public boolean dead = false; 
	
	//gets the piece's black piece square table
	//White pieces' PS tables are accessed by replacing row with 7-row
	public abstract int[][] getBlackPS(); 
	
	/**
	 * Constructor for the chess piece
	 * 
	 * @param filename - The filename of the image to display
	 */
	public ChessPiece(String filename) { 
		this.panel = new JPanel();
		this.setImage(filename);
	}
	
	public void LoadImage() {
		setImage(this.imageFilename);
	}
	
	/**
	 * Sets the image for the imageview for the piece
	 * 
	 * @param filename - The filename of the image to display
	 */
	public void setImage(String filename) {
		this.imageView = new ChessImageView(filename);
		this.panel.removeAll();
		this.panel.setPreferredSize(new Dimension(80, 80));
		this.panel.add(this.imageView);
	}
	
	/**
	 * Returns the content panel holding the imageview
	 * 
	 * @return the content panel
	 */
	public JPanel getPanel() {
		return this.panel;
	}
	
	private Position position;
	public boolean isOnWhiteTeam; //which team you're on
	public ChessPiece() { //default constructor
		;
	}
	public ChessPiece(int theColumn, int theRow, boolean whiteTeam) { //constructor
		position = new Position(theColumn, theRow);
		isOnWhiteTeam = whiteTeam;		
		this.panel = new JPanel();
		ChessBoard.setChessPiece(position.column, position.row, this);
	}
	public ChessPiece(int theColumn, int theRow, boolean whiteTeam, String filename) { //constructor
		position = new Position(theColumn, theRow);
		isOnWhiteTeam = whiteTeam;
		ChessBoard.setChessPiece(position.column, position.row, this);
		this.panel = new JPanel();
		this.imageFilename = filename;
	}
	public int getRow() { //obvi
		return position.row;
	}
	public int getColumn() { //obvi
		return position.column;
	}
	public void setPosition(Position pos) { 
		//sets the piece's internally stored position, not its actual location in the chessboard
		position = pos;
	}
	
	public ArrayList<Position> removeDangerMoves(ArrayList<Position> moves) {
		//returns the parameter arraylist, minus any moves that would put you in check
		ArrayList<Position> returned = new ArrayList<Position>();
		
		
		int oldColumn = this.getColumn();
		int oldRow = this.getRow();
		for (Position pos : moves ) {
			ChessPiece oldOccupant = ChessBoard.getBoard()[pos.column][pos.row];
			ChessBoard.move(this, pos); //simulates move
			if (!this.player.opponent.opponentIsInCheck()) { //checks if it's legal
				returned.add(pos);
			}
			//undo move:
			ChessBoard.move(this, new Position(oldColumn, oldRow));
			ChessBoard.setChessPiece(pos.column, pos.row, oldOccupant);
			this.setPosition(new Position(oldColumn, oldRow));
			
			
		}
		return returned;
	}
	
	public ArrayList<Position> getPositionsInDirection (int horiz, int vert) { 
		//used in some pieces' possibleMoves() methods - gets all valid positions in specified direction
		//vert and horiz should be either -1, 0, or 1. For example (1, -1) would be diagonal down right since you increase the column number and decrease the row number
																			
		ArrayList<Position> returned = new ArrayList<Position>();
		boolean stop = false;
		Position currentPosition = position;
		while (!stop) {
			currentPosition = new Position(currentPosition.column + horiz, currentPosition.row + vert);
			if (currentPosition.isValid()) {
				returned.add(currentPosition);
			}
			else {
				stop = true;
			}
		}
		return returned;
	}
	
	public ArrayList<Position> removeInvalid(ArrayList<Position> positions) {
		//doesn't let you move off the board or onto a position owned by your piece
		ArrayList<Position> returned = new ArrayList<Position>();
		for (Position pos : positions) {
			
			if (!pos.isValid()) { 
				//if the position is off the board
				;
			}
			else if (ChessBoard.getBoard()[pos.column][pos.row] == null) { 
				//if it's on the board and empty
				returned.add(pos);
			}
			
			else if (((ChessPiece)ChessBoard.getBoard()[pos.column][pos.row]).isOnWhiteTeam == isOnWhiteTeam )  { 
				//if it's occupied by your own team
				;
			}
			
			else {
				//if it's occupied by the other team
				returned.add(pos);
			}
			
			
		}
		
		return returned;
	}
	
	public ArrayList<ArrayList<Position>> ignoreAfterObstruction(ArrayList<ArrayList<Position>> meta) {
		//applied to the results of getPositionsInDirection - doesn't let you move "through" a piece
		for (ArrayList<Position> line : meta) {
			ArrayList<Position> toBeRemoved = new ArrayList<Position>();
			boolean startIgnoring = false;
			for (int i = 0; i < line.size(); i++) { 
				if (startIgnoring) {
					toBeRemoved.add(line.get(i)); 
					//remove from the returned arraylist if it's on the other side of the obstruction
				}
				else {
					if (ChessBoard.isOccupied(line.get(i).column,  line.get(i).row)) {
						startIgnoring = true; 
						//ignore all positions after the "obstruction"
					}
				}
			}
			for (Position removeThis : toBeRemoved) {
				line.remove(removeThis);
			}
			
		}
		return meta;
	}
	
	
	
	
	public abstract ArrayList<Position> possibleMoves(); //implemented by subclasses, obvi different for each one
	
}
