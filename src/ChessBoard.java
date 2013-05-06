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
	public static void Initialize() { //creates the chessboard
		//Allocate the array of tiles
		tiles = new ChessTile[8][8]; //for rendering
		pieces = new ChessPiece[8][8]; //for actual game

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				tiles[x][y] = new ChessTile(100, 100); //for rendering
			}
		}

		InitializeChessPieces(); //creates the pieces in the board
	}

	public static void ResetGame(Player player1, Player player2) { 
		//resets the game.  Not really needed here but used in genetic learning
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				ChessBoard.removeChessPiece(i, j);
			}
		}
		player1.refreshArrayLists();
		player2.refreshArrayLists();
		PieceSquare.whiteScore = -95;
		PieceSquare.blackScore = -95;
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



			//The following code adds castling to the list of possible moves, if applicable.
			if (ChessGame.player1.getKing().castleAllowed) {
				
				//can't castle if king has moved
				if (!ChessGame.player2.opponentIsInCheck()) { //can't castle out of check
					for (Rook rook : ChessGame.player1.myRooks) { 
						if (!rook.dead && rook.castleAllowed) { //can't castle if rook has moved
							if (rook.getColumn() == 0) {
								Position castlePos = new Position(1, ChessGame.player1.getKing().getRow());
								castlePos.isCastle = true; //distinguishes from normal moves
								castlePos.myRook = rook;
								King king = ChessGame.player1.getKing();
								int rookColumn = rook.getColumn();
								int kingColumn = king.getColumn();
								int theRow = king.getRow();
								boolean doIt = true;
								for (int i = Math.min(rookColumn, kingColumn) + 1; i < Math.max(rookColumn, kingColumn); i ++ ) {
									if (ChessBoard.isOccupied(i, theRow)) { //can't castle through occupied squares
										doIt = false;
									}
								}
								if (doIt)
									currentMoves.add(castlePos);
							}
							else if (rook.getColumn() == 7) {
								Position castlePos = new Position(6, ChessGame.player1.getKing().getRow());
								castlePos.isCastle = true; //distinguishes from normal moves
								castlePos.myRook = rook;
								King king = ChessGame.player1.getKing();
								int rookColumn = rook.getColumn();
								int kingColumn = king.getColumn();
								int theRow = king.getRow();
								boolean doIt = true;
								for (int i = Math.min(rookColumn, kingColumn) + 1; i < Math.max(rookColumn, kingColumn); i ++ ) {
									if (ChessBoard.isOccupied(i, theRow)) { //can't castle through occupied squares
										doIt = false;
									}
								}
								if (doIt)
									currentMoves.add(castlePos);
							}
						}
					}
				}
			}

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
		int oldRow = currentlySelectedPiece.getRow();
		int oldColumn = currentlySelectedPiece.getColumn();
		ChessPiece oldOccupant = ChessBoard.getBoard()[column][row];
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

			boolean castle = false;
			if (currentlySelectedPiece instanceof King) {
				if (Math.abs(column - currentlySelectedPiece.getColumn()) > 1) {
					castle = true;
				}
			}

			if (castle) {


				doCastle(currentlySelectedPiece, column, row);
			}
			else {
				ChessGame.currentPlayer.makeMove(currentlySelectedPiece, new Position(column, row));
			}
			if (ChessGame.currentPlayer.isOnWhiteTeam) {
				PieceSquare.whiteScore -= currentlySelectedPiece.getBlackPS()[7 - oldRow][oldColumn];
				PieceSquare.whiteScore += currentlySelectedPiece.getBlackPS()[7 - row][column];
				if (oldOccupant != null && !oldOccupant.isOnWhiteTeam) {
					PieceSquare.blackScore -= oldOccupant.getBlackPS()[row][column];
				}
			}
			else {
				PieceSquare.blackScore -= currentlySelectedPiece.getBlackPS()[oldRow][oldColumn];
				PieceSquare.blackScore += currentlySelectedPiece.getBlackPS()[row][column];
				if (oldOccupant != null && oldOccupant.isOnWhiteTeam) {
					PieceSquare.whiteScore -= oldOccupant.getBlackPS()[7 - row][column];
				}
			}

			if (currentlySelectedPiece instanceof Rook) {
				((Rook)(currentlySelectedPiece )).castleAllowed = false;
			}
			if (currentlySelectedPiece  instanceof King) {
				((King)(currentlySelectedPiece )).castleAllowed = false;
			}


			ResetSelection();
			ChessGame.UpdateGame();
		}

		//Reset all of the UI for selection
		ResetSelection();
	}

	public static void undoCastle(ChessPiece piece, Rook rook) {

		piece.player.hasCastled = false;
		((King)piece).castleAllowed = true;
		rook.castleAllowed = true;
		if (piece.isOnWhiteTeam) {
			ChessBoard.move(piece, new Position(4, piece.getRow()));
		}
		else {
			ChessBoard.move(piece, new Position(4, piece.getRow()));
		}
		if (rook.getColumn() == 2) {
			//System.exit(0);
			ChessBoard.move(rook, new Position(0, rook.getRow()));
		}
		else if (rook.getColumn() == 5) {
			//System.exit(0);
			ChessBoard.move(rook, new Position(7, rook.getRow()));
		}
		else {
			//System.exit(0);
		}


	}
	public static Rook doCastle(ChessPiece currentlySelectedPiece, int column, int row) {
		//does a castle move.  Part of the ridiculously complicated castle-handling
		int oldRow = currentlySelectedPiece.getRow();
		int oldColumn = currentlySelectedPiece.getColumn();
		((King)currentlySelectedPiece).castleAllowed = false; //can't castle twice!!

		currentlySelectedPiece.player.hasCastled = true; //duh
		if (!currentlySelectedPiece.player.opponent.opponentIsInCheck()) { //this should always be the case if it's a possible move, but just to make sure
			Rook rook = null;
			//Finds the associated rook with the move:
			if (currentlySelectedPiece.player.isOnWhiteTeam) {
				if (column == 1) {
					rook = (Rook)ChessBoard.getBoard()[0][0];
				}
				else if (column == 6) {
					rook = (Rook)ChessBoard.getBoard()[7][0];
				}

			}
			else if (!currentlySelectedPiece.player.isOnWhiteTeam) {
				if (column == 1) {
					rook = (Rook)ChessBoard.getBoard()[0][7];
				}
				else if (column == 6) {
					rook = (Rook)ChessBoard.getBoard()[7][7];
				}
			}

			if (rook == null) { //should never happen, but useful for testing/debugging
				System.out.println("ooops");
				ChessBoard.printBoard();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			int oldRookRow = rook.getRow();
			int oldRookColumn = rook.getColumn();
			King king = (King)currentlySelectedPiece;
			int rookColumn = rook.getColumn();
			int kingColumn = king.getColumn();
			int theRow = king.getRow();
			boolean doIt = true;
			for (int m = Math.min(rookColumn, kingColumn) + 1; m < Math.max(rookColumn, kingColumn); m ++ ) {
				if (ChessBoard.isOccupied(m, theRow)) { //can't castle through occupied square
					doIt = false;
				}
			}
			int newRookRow = -1;
			int newRookColumn = -1;
			if (doIt) {
				//Actually do the castle

				if (rookColumn == 0) {
					ChessBoard.move(rook, new Position(2, theRow));
					newRookRow = theRow;
					newRookColumn = 2;
					ChessBoard.move(currentlySelectedPiece, new Position(column, row));
				}
				else if (rookColumn == 7) {
					ChessBoard.move(rook, new Position(5, theRow));
					newRookRow = theRow;
					newRookColumn  = 5;
					ChessBoard.move(currentlySelectedPiece, new Position(column, row));
				}


			}
			rook.castleAllowed = false; // can't castle twice!!!
			Player currentPlayer = currentlySelectedPiece.player;
			ChessPiece piece = currentlySelectedPiece;

			//increments the Piece Square scores for each player:

			if (currentPlayer.isOnWhiteTeam) {
				PieceSquare.whiteScore -= piece.getBlackPS()[7 - oldRow][oldColumn];
				PieceSquare.whiteScore += piece.getBlackPS()[7 - row][column];
				PieceSquare.whiteScore -= rook.getBlackPS()[7 - oldRow][oldColumn];
				PieceSquare.whiteScore += rook.getBlackPS()[7 - newRookRow][newRookColumn];

			}
			else {
				PieceSquare.blackScore -= piece.getBlackPS()[oldRow][oldColumn];
				PieceSquare.blackScore += piece.getBlackPS()[row][column];
				PieceSquare.blackScore -= rook.getBlackPS()[oldRookRow][oldRookColumn];
				PieceSquare.blackScore += rook.getBlackPS()[newRookRow][newRookColumn];

			}
			return rook;
		}
		return null;

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
	public static void printBoard() { //prints the board in text - useful for testing
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				System.out.print(getBoard()[j][i] + "  ");
			}
			System.out.println();
		}
	}
	public static void setChessPiece(int x, int y, ChessPiece piece) { //sets a chess piece at designated location
		//Set the occupant for that position
		pieces[x][y] = piece;
		tiles[x][y].removeOccupant();
		tiles[x][y].setOccupant(pieces[x][y]);
		if (piece != null) {
			try {
				Player player = piece.player;
				if (!player.getMyTeam().contains(piece)) {
					player.getMyTeam().add(piece); //adds it to the players list of people on team if not already there
					//right now this doesn't actualyl do anything since getMyTeam() doesn't return a reference.  But it should eventually hopefully...
				}

				//adds the piece to piece-type specific lists if not already present
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

	public static ChessPiece removeChessPiece(int x, int y) {	//removes the piece at that position from the board	
		ChessPiece piece = pieces[x][y];
		pieces[x][y] = null;
		tiles[x][y].removeOccupant();
		return piece;
	}

	public static ChessPiece removeChessPiece(ChessPiece piece) { //removes the piece from the board
		return removeChessPiece(piece.getColumn(), piece.getRow());
	}

	public static ChessPiece[][] getBoard() { //returns the chess board
		return pieces;
	}

	public static boolean isOccupied(int column, int row) { //duh
		return pieces[column][row] != null;
	}

	public static void move(ChessPiece piece, Position pos)  { //moves a piece to the desired position.  Replaces anyone already in that position



		try {
			ChessPiece thing = ChessBoard.getBoard()[pos.column][pos.row];
			//does the move:
			removeChessPiece(piece.getColumn(), piece.getRow());
			setChessPiece(pos.column, pos.row, piece);
			piece.setPosition(new Position(pos.column, pos.row));
			if (thing != null) {

				thing.player.refreshArrayLists(); //if you killed a piece, update the dying piece's player's list of pieces
			}
			if (piece instanceof Pawn) { //pawn promotion code
				if ((piece.isOnWhiteTeam && pos.row == 7) || (!piece.isOnWhiteTeam && pos.row == 0)) {
					Queen queen = new Queen(pos.column, pos.row, piece.isOnWhiteTeam);
					queen.player = ChessGame.currentPlayer;
					queen.LoadImage();
					ChessBoard.setChessPiece(pos.column,  pos.row,  queen);
				}
			}


		} catch (NullPointerException ex) {} //#goodcodingstyle
	}




	public static void setBoard(ChessPiece[][] board) { //not used anywhere as of 5/1/13.  But maybe it will be someday...
		pieces = board;

		for (int x = 0; x < 8; ++x) {
			for (int y = 0; y < 8; ++y) {
				move(pieces[x][y], new Position(x, y));
			}
		}
	}
}
