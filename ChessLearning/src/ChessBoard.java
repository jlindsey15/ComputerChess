public class ChessBoard {
	//The tiles array
	public static ChessTile[][] tiles;
	public static ChessPiece[][] pieces;

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
	
	public static void ResetGame(Player player1, Player player2) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				ChessBoard.removeChessPiece(i, j);
			}
		}
		
		PieceSquare.whiteScore = -95;
		PieceSquare.blackScore = -95;
		InitializeChessPieces();
		
		player1.refreshArrayLists();
		player2.refreshArrayLists();
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
				if (getBoard()[j][i] != null) System.out.print(getBoard()[j][i] + ((getBoard()[j][i].isOnWhiteTeam) ? "1" : "2") + "    ");
				else System.out.print("X" + "     ");
			}
			System.out.println();
		}
	}
	public static void setChessPiece(int x, int y, ChessPiece piece) {
		//Set the occupant for that position
		pieces[x][y] = piece;
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
						player.setKing((King) piece);
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
