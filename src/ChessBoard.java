import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;


public class ChessBoard {
	//The tiles array
	public static ChessTile[][] tiles;
	public static ChessPiece[][] pieces;

	//The currently selected piece
	private static ChessPiece currentlySelectedPiece = null;
	private static ArrayList<Position> currentMoves = new ArrayList<Position>();

	/**
	 * Initializes the chess board and the array of the tiles
	 */
	public static void Initialize() {
		//Allocate the array of tiles
		tiles = new ChessTile[8][8];
		pieces = new ChessPiece[8][8];

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				tiles[x][y] = new ChessTile(100, 100);
			}
		}

		InitializeChessPieces();
	}

	private static void InitializeChessPieces() {
		for (int i = 0; i < 8; i++) {
			new Pawn(i, 1, true); //creates white pawns - near side
			new Pawn(i, 6, false); //creates black pawns - far side
		}
		//white rooks:
		new Rook(0, 0, true);
		new Rook(7, 0, true);
		//black rooks:
		new Rook(0, 7, false);
		new Rook(7, 7, false);
		//white knights:
		new Knight(1, 0, true);
		new Knight(6, 0, true);
		//black knights:
		new Knight(1, 7, false);
		new Knight(6, 7, false);
		//white bishops:
		new Bishop(2, 0, true);
		new Bishop(5, 0, true);
		//black bishops:
		new Bishop(2, 7, false);
		new Bishop(5, 7, false);
		//white queen:
		new Queen(3, 0, true);
		//black queen:
		new Queen(3, 7, false);
		//white king:
		new King(4, 0, true);
		//black king:
		new King(4, 7, false);
	}

	/**
	 * Generates the initial chess board layout
	 * 
	 * @param width - The width of the chess board in pixels
	 * @param height - The height of the chess board in pixels
	 * @param firstTileColor - The first tile color for the chess board
	 * @param secondTileColor - The second tile color for the chess board
	 * @return - The content panel holding the chess board view
	 */
	public static JPanel GenerateChessBoard(int width, int height, Color firstTileColor, Color secondTileColor) {
		JPanel contentPanel = new JPanel();
		contentPanel.setSize(width, height);
		contentPanel.setPreferredSize(new Dimension(width, height));

		//Create the grid layout manager to make tile placement easier
		GridLayout layout = new GridLayout(0, 8);
		layout.setHgap(2);
		layout.setVgap(2);

		contentPanel.setLayout(layout);

		//Make all the tiles
		for (int y = 0; y < 8; ++y) {
			for (int x = 0; x < 8; ++x) {				
				//Check to see the position of the tile and determine the color
				if (x % 2 != 0 && y % 2 == 0) tiles[x][y].SetColor(firstTileColor);
				else if (x % 2 == 0 && y % 2 != 0) tiles[x][y].SetColor(firstTileColor);
				else if (x % 2 == 0 && y % 2 == 0) tiles[x][y].SetColor(secondTileColor);
				else if (x % 2 != 0 && y % 2 != 0) tiles[x][y].SetColor(secondTileColor);

				//Add the tile to the panel
				if (pieces[x][y] != null) {
					tiles[x][y].setOccupant(pieces[x][y]);
					pieces[x][y].LoadImage();
				}
				contentPanel.add(tiles[x][y].getPanel());
			}
		}

		return contentPanel;
	}

	/**
	 * Resets the selection UI. Handles removing the highlights on the board. 
	 * Removes the currently selected piece. Clears all the moves
	 */
	private static void ResetSelection() {
		//Clear all the moves and the current piece selected
		currentMoves.clear();
		currentlySelectedPiece = null;

		//Clear all the highlights
		for (int x = 0; x < 8; ++x) {
			for (int y = 0; y < 8; ++y) {
				tiles[x][y].setHighlighted(false);
			}
		}
	}

	/**
	 * Called from the mouse event pressed. Handles the selection of a tile. Determines whether clicking to move a piece, 
	 * to select a piece, or whether it is a wrong click.
	 * 
	 * @param row - The row which was clicked
	 * @param column - The column which was clicked
	 * @param onWhite - Whether or not the current team is the white team
	 */
	public static void SelectionMade(int row, int column, boolean onWhite) {
		//Check to see if a piece is selected and there are possible moves
		if (currentlySelectedPiece != null && !currentMoves.isEmpty()) {
			AttemptMove(row, column);
			return;
		}

		//Check to see if a piece is selected and there are no moves to do
		if (currentlySelectedPiece != null && currentMoves.isEmpty()) {
			ResetSelection();
		}

		//Check to see if just clicking in a random space
		if (pieces[row][column] == null && currentMoves.isEmpty()) {
			return;
		}

		//Check to see whether or not selection has been made
		if (currentlySelectedPiece == null) {
			if (pieces[row][column].isOnWhiteTeam != onWhite) return;

			//Set the current piece and the current moves
			currentlySelectedPiece = pieces[row][column];
			currentMoves = currentlySelectedPiece.removeDangerMoves(currentlySelectedPiece.possibleMoves());

			//Highlight the selection tile
			tiles[row][column].setHighlighted(true);

			//Go through all the possible positions and highlight them
			for (Position pos : currentMoves) {
				tiles[pos.column][pos.row].setHighlighted(true);
			}
		}
		
		ChessApplication.UpdateDisplay();
	}

	/**
	 * Function which moves and handles the pieces. Handles checking if it is a valid move and
	 * handles removing the old piece and adding the new piece
	 * 
	 * @param column - The column of the new position
	 * @param row - The row of the new position
	 */
	private static void AttemptMove(int column, int row) {	
		System.out.println(Player.evaluatePawnBonus(ChessBoard.getBoard(), ChessGame.currentPlayer));
		System.out.println(Player.evaluatePawnBonus(ChessBoard.getBoard(), ChessGame.otherPlayer));
		//System.exit(0);
		boolean validMove = false;

		//Go through all the current valid moves for the current piece
		for (Position pos : currentMoves) {		
			//We have the correct move clicked
			if (pos.row == row && pos.column == column) {
				validMove = true;
			}
		}

		//If we clicked a valid move then move the piece to that place
		if (validMove) {
			if (currentlySelectedPiece instanceof Pawn) {
				((Pawn) currentlySelectedPiece).hasMoved = true;

			}

			ChessGame.currentPlayer.makeMove(currentlySelectedPiece, new Position(column, row));
			
			if (currentlySelectedPiece instanceof Pawn) {
				if ((currentlySelectedPiece.isOnWhiteTeam && row == 7) || (!currentlySelectedPiece.isOnWhiteTeam && row == 0)) {
					System.out.println("CHANGE");
					//ChessBoard.removeChessPiece(column, row);
					Queen queen = new Queen(column, row, currentlySelectedPiece.isOnWhiteTeam);
					queen.player = ChessGame.currentPlayer;
					queen.LoadImage();
					ChessBoard.setChessPiece(column,  row,  queen);
					System.out.println("test city " + tiles[column][row].getOccupant());

				}
			}
			ResetSelection();
			ChessGame.UpdateGame();
		}

		//Reset all of the UI for selection
		ResetSelection();
	}

	public static void SetProperPiecePositions() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				setChessPiece(x, y, pieces[x][y]);
			}
		}
	}

	/**
	 * Sets the chess piece for the position. 
	 * 
	 * @param x - The x coordinate for the chess piece (board coordinates 1 - 8)
	 * @param y - The y coordinate for the chess piece (board coordinates 1 - 8)
	 * @param piece - The piece to set to the position
	 */
	public static void printBoard() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				System.out.print(getBoard()[j][i] + "  ");
			}
			System.out.println();
		}
	}
	public static void setChessPiece(int x, int y, ChessPiece piece) {
		//Set the occupant for that position
		pieces[x][y] = piece;
		tiles[x][y].removeOccupant();
		tiles[x][y].setOccupant(pieces[x][y]);
		if (piece != null) {
			try {
				Player player = piece.player;

				if (piece instanceof Pawn) {
					if (!player.myPawns.contains(piece)) {
						player.myPawns.add((Pawn)piece);
					}
				}
				else if (piece instanceof Rook) {
					if (!player.myRooks.contains(piece)) {
						player.myRooks.add((Rook)piece);
					}
				}
				if (piece instanceof Bishop) {
					if (!player.myBishops.contains(piece)) {
						player.myBishops.add((Bishop)piece);
					}
				}
				if (piece instanceof Knight) {
					if (!player.myKnights.contains(piece)) {
						player.myKnights.add((Knight)piece);
					}
				}
				if (piece instanceof King) {
					if (!player.myKings.contains(piece)) {
						player.myKings.add((King)piece);
					}
				}
				if (piece instanceof Queen) {
					if (!player.myQueens.contains(piece)) {
						player.myQueens.add((Queen)piece);
					}
				}

			}
			catch (NullPointerException e) {
				//e.printStackTrace();
			}
		}
	}

	public static ChessPiece removeChessPiece(int x, int y) {		
		ChessPiece piece = pieces[x][y];
		pieces[x][y] = null;
		tiles[x][y].removeOccupant();
		return piece;
	}

	public static ChessPiece removeChessPiece(ChessPiece piece) {
		return removeChessPiece(piece.getColumn(), piece.getRow());
	}

	public static ChessPiece[][] getBoard() {
		return pieces;
	}

	public static boolean isOccupied(int column, int row) {
		return pieces[column][row] != null;
	}

	public static void move(ChessPiece piece, Position pos) {

		try {
			ChessPiece thing = ChessBoard.getBoard()[pos.column][pos.row];
			removeChessPiece(piece.getColumn(), piece.getRow());
			setChessPiece(pos.column, pos.row, piece);
			piece.setPosition(new Position(pos.column, pos.row));
			if (thing != null) {
				
				thing.player.refreshArrayLists();
			}
			if (piece instanceof Pawn) {
				if ((piece.isOnWhiteTeam && pos.row == 7) || (!piece.isOnWhiteTeam && pos.row == 0)) {
					Queen queen = new Queen(pos.column, pos.row, piece.isOnWhiteTeam);
					queen.player = ChessGame.currentPlayer;
					ChessBoard.setChessPiece(pos.column,  pos.row,  queen);
				}
			}

		} catch (NullPointerException ex) {}
	}




	public static void setBoard(ChessPiece[][] board) {
		pieces = board;

		for (int x = 0; x < 8; ++x) {
			for (int y = 0; y < 8; ++y) {
				move(pieces[x][y], new Position(x, y));
			}
		}
	}
}
