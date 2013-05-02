import java.util.ArrayList;


public class Player {
	
	//a bunch of constant weights.  Are/will be determined by chess learning
	public static final int PAWN_WEIGHT = 100;
	public static final int ROOK_WEIGHT = 500;
	public static final int BISHOP_WEIGHT = 330;
	public static final int KNIGHT_WEIGHT = 320;
	public static final int QUEEN_WEIGHT = 900;
	public static final int KING_WEIGHT = 200000000; //super high cause the king is, like, important...
	public static final int ROOK_ALONE_BONUS = 5;
	public static final int PAWN_ISOLATION_PENALTY = -3;
	public static final int PAWN_BACKWARD_PENALTY = -5;
	public static final int PAWN_DOUBLED_PENALTY = -4;
	public static final int KING_SHIELDED_BONUS = 10;
	public static final int KING_PAWN_STORM = 10;
	public static final int RADIUS_SQUARED = 4;

	

	public boolean isOnWhiteTeam;
	public boolean hasCastled = false;
	
	public Player opponent;
	public Player() { //default constructor
		;
	}
	
	//piece arraylists:
	public  ArrayList<Rook> myRooks = new ArrayList<Rook>();
	public  ArrayList<Pawn> myPawns = new ArrayList<Pawn>();
	public  ArrayList<Bishop> myBishops = new ArrayList<Bishop>();
	public  ArrayList<Queen> myQueens = new ArrayList<Queen>();
	public  ArrayList<King> myKings = new ArrayList<King>();
	public ArrayList<Knight> myKnights = new ArrayList<Knight>();
	
	
	public Player(boolean whiteTeam) { //constructor
		isOnWhiteTeam = whiteTeam;
		for (ChessPiece piece : getMyTeam()) {
			//adds piece to piece arraylists
			if (piece instanceof Pawn) myPawns.add((Pawn)piece);
			if (piece instanceof Rook) myRooks.add((Rook)piece);
			if (piece instanceof Bishop) myBishops.add((Bishop)piece);
			if (piece instanceof Knight) myKnights.add((Knight)piece);
			if (piece instanceof King) myKings.add((King)piece);
			if (piece instanceof Queen) myQueens.add((Queen)piece);
			piece.player = this;
		}


	}
	public void refreshArrayLists() {
		//"refreshes" all of the player's various piece arraylists
		myPawns.clear();
		myRooks.clear();
		myBishops.clear();
		myKnights.clear();
		myQueens.clear();
		myKings.clear();
		for (ChessPiece piece : getMyTeam()) {
			//add piece to appropriate arraylist
			if (piece instanceof Pawn) myPawns.add((Pawn)piece);
			if (piece instanceof Rook) myRooks.add((Rook)piece);
			if (piece instanceof Bishop) myBishops.add((Bishop)piece);
			if (piece instanceof Knight) myKnights.add((Knight)piece);
			if (piece instanceof King) myKings.add((King)piece);
			if (piece instanceof Queen) myQueens.add((Queen)piece);
			piece.player = this;
		}

	}
	public void setOpponent(Player opp) {
		opponent = opp;
	}
	public void makeMove(ChessPiece piece, Position pos) { 
		//basically the same as ChessBoard.move, except it makes sure you're not moving the other team's piece.  
		//Also the pawn's hasMoved boolean is only set to true in this method. This presents a problem during move-searching,
		//since the hasMoved is not set accurately during simulated moves, but this slight inaccuracy is probably inconsequential
		//and I don't feel like fixing it./
		
		
		if (piece.isOnWhiteTeam == isOnWhiteTeam) {
			

			ChessBoard.move(piece, pos);
			if (piece instanceof Pawn) {
				
				((Pawn) piece).hasMoved = true;
			}
			
		}

	}
	public void restore(ChessPiece piece) {
		//restores a piece from the dead by adding it to the appropriate piece arraylist
		ChessPiece thing = piece;
		if (thing instanceof Pawn) myPawns.add((Pawn)thing);
		else if (thing instanceof Knight) myKnights.add((Knight)thing);
		else if (thing instanceof Rook) myRooks.add((Rook)thing);
		else if (thing instanceof Queen) myQueens.add((Queen)thing);
		else if (thing instanceof King) myKings.add((King)thing);
		else if (thing instanceof Bishop) myBishops.add((Bishop)thing);

	}

	
	public boolean hasWon() { //checks if the player has won the game
		if (!opponentIsInCheck()) { //opponent must currently be in check to achieve checkmate (but not for stalemate...)
			return false;
		}

		else {
			for (ChessPiece oppPiece : opponent.getMyTeam()) {

				for (Position movePosition : oppPiece.possibleMoves()) { //simmulate all possible opponent moves
					//used to restore the board back to original state after "preview" of opponent move
					ChessPiece[][] oldBoard = new ChessPiece[8][8];

					for (int i = 0; i < 8; i++) {
						for (int j = 0; j < 8; j++) {
							oldBoard[i][j] = ChessBoard.getBoard()[i][j];
						}
					}
					int oldColumn = oppPiece.getColumn();
					int oldRow = oppPiece.getRow();
					ChessPiece oldOccupant = ChessBoard.getBoard()[movePosition.column][movePosition.row];
					ChessBoard.move(oppPiece, movePosition); //simulate move
					if (!opponentIsInCheck()) {//this means there's a way for the opponent to not move into check, so the game's not a stale mate
						ChessBoard.move(oppPiece,  new Position(oldColumn, oldRow));
						ChessBoard.setChessPiece(movePosition.column,  movePosition.row,  oldOccupant);

						return false; 

					}
					else {
						//undo the simulated move:
						ChessBoard.move(oppPiece,  new Position(oldColumn, oldRow));
						ChessBoard.setChessPiece(movePosition.column,  movePosition.row,  oldOccupant);

					}

				}
			}
		}
		return true; //if there's no escape, return true
	}

	public boolean hasStaleMate() { //TODO: other stalemate rules (like 3 same moves in a row, 50 move rule, etc.)
		//checks if the player has won the game 
		if (opponentIsInCheck()) { //opponent must NOT currently be in check to achieve STALEMATE
			return false;
		}
		else {
			for (ChessPiece oppPiece : opponent.getMyTeam()) {
				for (Position movePosition : oppPiece.possibleMoves()) { //simulate all possible opponent moves
					//used to restore the board back to original state after "preview" of opponent move
					ChessPiece[][] oldBoard = new ChessPiece[8][8];

					for (int i = 0; i < 8; i++) {
						for (int j = 0; j < 8; j++) {
							oldBoard[i][j] = ChessBoard.getBoard()[i][j];
						}
					}
					int oldColumn = oppPiece.getColumn();
					int oldRow = oppPiece.getRow();
					ChessPiece oldOccupant = ChessBoard.getBoard()[movePosition.column][movePosition.row];
					ChessBoard.move(oppPiece, movePosition); //simulate move
					if (!opponentIsInCheck()) {//this means there's a way for the opponent to not move into check, so the game's not a stale mate
						ChessBoard.move(oppPiece,  new Position(oldColumn, oldRow));
						ChessBoard.setChessPiece(movePosition.column,  movePosition.row,  oldOccupant);
						return false; 

					}
					else {
						//undo simulated move
						ChessBoard.move(oppPiece,  new Position(oldColumn, oldRow));
						ChessBoard.setChessPiece(movePosition.column,  movePosition.row,  oldOccupant);

					}

				}
			}
		}

		return true; 
		//if there's no escape, return true
	}

	public boolean opponentIsInCheck() { 
		//checks whether you could attack the opponent's king
		
		for (ChessPiece piece : getMyTeam()) {

			for (Position movePosition : piece.possibleMoves()) {
				//check every one of the player's possible moves
				
				int oldColumn = piece.getColumn(); 
				int oldRow = piece.getRow();
				ChessPiece oldOccupant = ChessBoard.getBoard()[movePosition.column][movePosition.row];
				ChessBoard.move(piece, movePosition); //simulates move

				if (opponent.myKings.size() == 0) { 
					//if one of your pieces could attack opponent's King, return true
					
					//undo move:
					ChessBoard.move(piece, new Position(oldColumn, oldRow));
					ChessBoard.setChessPiece(movePosition.column,  movePosition.row,  oldOccupant);
					piece.setPosition(new Position(oldColumn, oldRow));
					return true;
				}
				//undo move:
				ChessBoard.move(piece, new Position(oldColumn, oldRow));
				ChessBoard.setChessPiece(movePosition.column,  movePosition.row,  oldOccupant);
				
			}
		}

		return false; //if no possible ways to attack opponent king, then he is not in check
	}

	public ArrayList<ChessPiece> getMyTeam() { 
		//gets everyone in my team by going through the 
		ArrayList<ChessPiece> returned = new ArrayList<ChessPiece>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (ChessBoard.getBoard()[i][j] == null) {
					;
				}
				else {
					if (ChessBoard.getBoard()[i][j].isOnWhiteTeam == isOnWhiteTeam) {
						returned.add(ChessBoard.getBoard()[i][j]);
					}
				}
			}
		}
		return returned;
	}
	public King getKing() {
		//gets the player's king...
		return myKings.get(0);
	}

	public static int evaluateMaterial(ChessPiece board[][], Player player) {
		//evaluates Material with respect to player
		int rank = 0;

		rank = KING_WEIGHT * (player.myKings.size() - player.opponent.myKings.size())
				+ QUEEN_WEIGHT * (player.myQueens.size() - player.opponent.myQueens.size())
				+ ROOK_WEIGHT * (player.myRooks.size() - player.opponent.myRooks.size())
				+ BISHOP_WEIGHT * (player.myBishops.size() - player.opponent.myBishops.size())
				+ KNIGHT_WEIGHT * (player.myKnights.size() - player.opponent.myKnights.size())
				+ PAWN_WEIGHT * (player.myPawns.size() - player.opponent.myPawns.size());

 		return rank;

	}
	public static int evaluateMobility(ChessPiece board[][], Player player) {
		//evaluates mobility with respect to player using possible moves as the heuristic
		int counter = 0;
		for (ChessPiece piece : player.getMyTeam()) {
			ArrayList<Position> moves = piece.removeDangerMoves(piece.possibleMoves());
			counter += moves.size();
		}
		for (ChessPiece piece : player.opponent.getMyTeam()) {
			ArrayList<Position> moves = piece.removeDangerMoves(piece.possibleMoves());
			counter -= moves.size();
		}
		return counter;
	}

	public static int evaluateRookBonusHelper(ChessPiece board[][], Player player) {
		//bonus for having rook on by itself on a column.  Evaluated only for player
		int count = 0;

		int aloneBonus = ROOK_ALONE_BONUS;
		for (Rook rook : player.myRooks) {
			int column = rook.getColumn();
			for (int i = 0; i < 8; i ++) {
				if (ChessBoard.getBoard()[column][i] != null) {
					aloneBonus = 0;
				}
			}
			count += aloneBonus;
		}
		return count;

	}

	public static int evaluatePawnBonus(ChessPiece board[][], Player player) { 
		//penalties for isolated or doubled pawns.  Evaluated with respect to player
		int count = 0;



		for (Pawn pawn : player.myPawns) {
			int column = pawn.getColumn();
			int row = pawn.getRow();
			int left = column - 1;
			int right = column + 1;
			int bonus;

			//isolated pawn penalty:
			if (0 <= left && left < 8) {
				bonus = PAWN_ISOLATION_PENALTY;


				for (Pawn otherPawn : player.myPawns) {
					if (otherPawn.getColumn() == left) {
						if (otherPawn != pawn) {
							bonus = 0;
							
						}
					}
				}
				count += bonus;
			}

			if (0 <= right && right < 8) {
				bonus = PAWN_ISOLATION_PENALTY;
				for (Pawn otherPawn : player.myPawns) {
					if (otherPawn.getColumn() == right) {
						if (otherPawn != pawn) {
							bonus = 0;
							
						}
					}
				}
				count += bonus;
			}
			bonus = 0;
			
			//doubled pawn penalty:
			for (Pawn otherPawn : player.myPawns) {
				if (otherPawn.getColumn() == column) {
					if (otherPawn != pawn) {
						bonus = PAWN_DOUBLED_PENALTY;
					}
				}
			}
			count += bonus;


		}
		
		//same as above but for opponent:
		
		for (Pawn pawn : player.opponent.myPawns) {
			int column = pawn.getColumn();
			int row = pawn.getRow();
			int left = column - 1;
			int right = column + 1;
			int bonus;
			int doubleBonus;

			if (0 <= left && left < 8) {
				bonus = PAWN_ISOLATION_PENALTY;

				for (Pawn otherPawn : player.opponent.myPawns) {
					if (otherPawn.getColumn() == left) {
						if (otherPawn != pawn) {
							bonus = 0;
							
						}
					}
				}
				count -= bonus;
			}

			if (0 <= right && right < 8) {
				bonus = PAWN_ISOLATION_PENALTY;
				for (Pawn otherPawn : player.opponent.myPawns) {
					if (otherPawn.getColumn() == right) {
						if (otherPawn != pawn) {
							bonus = 0;
							
						}
					}
				}
				count -= bonus;
			}
			bonus = 0;
			for (Pawn otherPawn : player.opponent.myPawns) {
				if (otherPawn.getColumn() == column) {
					if (otherPawn != pawn) {
						bonus = PAWN_DOUBLED_PENALTY;
					}
				}
			}
			count -= bonus;


		}
		return count;
	}

	public static int evaluatePieceSquare(ChessPiece board[][], Player player) {
		//easy because we've been incremented piece square values for each team throughout the game.  Evaluated with respect to player.
		int score = PieceSquare.whiteScore - PieceSquare.blackScore;
		
		if (player.isOnWhiteTeam) {
			return score;
		}
		else {
			
			return -score;
		}


		
	}

	public static int evaluateKingBonusHelper(ChessPiece board[][], Player player) {
		//bonus for king safety in various different ways.  Evaluated only for player
		int rank = 0;
		try {
			for (Pawn pawn : player.myPawns) {
				if (!pawn.dead) {
					int column = pawn.getColumn();
					int row = pawn.getRow();

					int kingRow = (player.isOnWhiteTeam) ? player.getKing().getRow() + 1 : player.getKing().getRow() - 1;
					int kingLeftColumn = player.getKing().getColumn() - 1;
					int kingRightColumn = player.getKing().getColumn() + 1;
					if ((column == kingLeftColumn || column == kingRightColumn || column == player.getKing().getColumn()) && kingRow == row) {
						rank += KING_SHIELDED_BONUS;
					}
				}
			}

			for (Pawn pawn : player.opponent.myPawns) {
				if (!pawn.dead) {
				if (Math.pow(pawn.getRow() - player.getKing().getRow(), 2) + Math.pow(pawn.getColumn() - player.getKing().getColumn(), 2) <= RADIUS_SQUARED) {
					rank -= KING_PAWN_STORM;
				}
				}
			}

			for (ChessPiece piece : player.opponent.getMyTeam()) {
				rank += (int) Math.sqrt(Math.pow(piece.getRow() - player.getKing().getRow(), 2) + Math.pow(piece.getColumn() - player.getKing().getColumn(), 2));
			}
		}
		catch (NullPointerException e) {
			;
		}

		return rank;
	}
	
	public static int evaluateRookBonus(ChessPiece board[][], Player player) {
		//rook bonus with respect to player
		return evaluateRookBonusHelper(board, player) - evaluateRookBonusHelper(board, player.opponent);

	}
	
	public static int evaluateKingBonus(ChessPiece board[][], Player player) {
		//king bonus with respect to player
		return evaluateKingBonusHelper(board, player) - evaluateKingBonusHelper(board, player.opponent);

	}

	public static int evaluateBoard(ChessPiece board[][], Player player) {
		//evaluates the board state ith respect to player by summing up all the individual evaluations
		return evaluateMaterial(board, player) + evaluatePieceSquare(board, player) + evaluateRookBonus(board, player) + evaluatePawnBonus(board, player) + evaluateMobility(board, player)  + evaluateKingBonus(board, player);
	}

	public static int lazyEval(ChessPiece board[][], Player player, int alpha, int beta) {
		//returns prematurely if the evaluation is definitely going to be less than alpha
		int counter = evaluateMaterial(board, player) + evaluatePieceSquare(board, player);
		
		//bonus for castling:
		if (player.hasCastled) {
			counter += 100;
		}
		if (player.opponent.hasCastled) {
			counter -= 100;
		}
		if (counter < alpha - 100 ) { //if the value is already way less than alpha, don't bother doing the more time-intensive calculations
			return counter;
		}
		else {
			//the score is reasonable, so we need to do a full evaluation
			//mobility evaluation right now is too slow to bother with
			counter +=(evaluateRookBonus(board, player) + evaluatePawnBonus(board, player)) + /*evaluateMobility(board, player) +*/ evaluateKingBonus(board, player) - evaluateKingBonus(board, player.opponent);
			return counter;
		}

	}


}

