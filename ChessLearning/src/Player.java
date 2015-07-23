import java.util.ArrayList;


public class Player {
	public int PAWN_WEIGHT = 100;
	public int ROOK_WEIGHT = 500;
	public int BISHOP_WEIGHT = 330;
	public int KNIGHT_WEIGHT = 320;
	public int QUEEN_WEIGHT = 900;
	public int KING_WEIGHT = 200000000;
	public int ROOK_ALONE_BONUS = 5;
	public int PAWN_ISOLATION_PENALTY = -3;
	public int PAWN_BACKWARD_PENALTY = -5;
	public int PAWN_DOUBLED_PENALTY = -4;
	public int KING_SHIELDED_BONUS = 10;
	public int KING_PAWN_STORM = 10;
	public int RADIUS_SQUARED = 4;
	
	private final int PAWN_WEIGHT_F = 100;
	private final int ROOK_WEIGHT_F = 500;
	private final int BISHOP_WEIGHT_F = 330;
	private final int KNIGHT_WEIGHT_F = 320;
	private final int QUEEN_WEIGHT_F = 900;
	private final int KING_WEIGHT_F = 200000000;
	private final int ROOK_ALONE_BONUS_F = 5;
	private final int PAWN_ISOLATION_PENALTY_F = -3;
	private final int PAWN_BACKWARD_PENALTY_F = -5;
	private final int PAWN_DOUBLED_PENALTY_F = -4;
	private final int KING_SHIELDED_BONUS_F = 10;
	private final int KING_PAWN_STORM_F = 10;
	
	public int gamesWon = 0;
	
	public void incrementGamesWon() {
		gamesWon++;
	}
	
	public int getGamesWon() {
		return gamesWon;
	}

	public boolean isOnWhiteTeam;
	public Player opponent;
	public Player() { //default constructor
		;
	}
	
	public void setWeights(String filename) {
		if (filename == null || filename == "") {
			randomlyDistributeWeights();
		} else {
			//File loading...
		}
		
		//System.out.println(" Pawn Weight: " + PAWN_WEIGHT + "\n Rook Weight: " + ROOK_WEIGHT + "\n " +
			//	"Bishop Weight: " + BISHOP_WEIGHT + "\n Queen Weight: " + QUEEN_WEIGHT + "\n King Weight: " + KING_WEIGHT
			//	+ "\n Rook Alone Bonus: " + ROOK_ALONE_BONUS + "\n Pawn Isolation Penalty: " + PAWN_ISOLATION_PENALTY + 
			//	"\n Pawn Backward Penalty: " + PAWN_BACKWARD_PENALTY + "\n Pawn Doubled Penalty: " + PAWN_DOUBLED_PENALTY + 
			//	"\n King Shieled Bonus: " + KING_SHIELDED_BONUS + "\n King Pawn Storm: " + KING_PAWN_STORM);
	}
	
	public String getWeights() {
		return " Pawn Weight: " + PAWN_WEIGHT + "\n Rook Weight: " + ROOK_WEIGHT + "\n " +
					"Bishop Weight: " + BISHOP_WEIGHT + "\n Queen Weight: " + QUEEN_WEIGHT + "\n King Weight: " + KING_WEIGHT
				+ "\n Rook Alone Bonus: " + ROOK_ALONE_BONUS + "\n Pawn Isolation Penalty: " + PAWN_ISOLATION_PENALTY + 
					"\n Pawn Backward Penalty: " + PAWN_BACKWARD_PENALTY + "\n Pawn Doubled Penalty: " + PAWN_DOUBLED_PENALTY + 
					"\n King Shieled Bonus: " + KING_SHIELDED_BONUS + "\n King Pawn Storm: " + KING_PAWN_STORM;
	}
	
	public String getWeightsNumeric() {
		return PAWN_WEIGHT + "\n" + ROOK_WEIGHT + "\n" + BISHOP_WEIGHT + "\n" + KNIGHT_WEIGHT +
			"\n" + QUEEN_WEIGHT + "\n" + KING_WEIGHT
			+ "\n" + ROOK_ALONE_BONUS + "\n" + PAWN_ISOLATION_PENALTY + 
				"\n" + PAWN_BACKWARD_PENALTY + "\n" + PAWN_DOUBLED_PENALTY + 
				"\n" + KING_SHIELDED_BONUS + "\n" + KING_PAWN_STORM;
	}
	
	public void setWeights(int pawn, int rook, int bishop, int queen, int knight, int king, int alone, int isolation, int backward,
			int doubled, int shielded, int storm) {
		PAWN_WEIGHT = pawn;
		BISHOP_WEIGHT = bishop;
		QUEEN_WEIGHT = queen;
		KNIGHT_WEIGHT = knight;
		KING_WEIGHT = king;
		ROOK_ALONE_BONUS = alone;
		PAWN_ISOLATION_PENALTY = isolation;
		PAWN_BACKWARD_PENALTY = backward;
		PAWN_DOUBLED_PENALTY = doubled;
		KING_SHIELDED_BONUS = shielded;
		KING_PAWN_STORM = storm;
	}
	
	private void randomlyDistributeWeights() {
		PAWN_WEIGHT = (int) (Math.random() * (PAWN_WEIGHT_F * 2));
		ROOK_WEIGHT = (int) (Math.random() * (ROOK_WEIGHT_F * 2));
		BISHOP_WEIGHT = (int) (Math.random() * (BISHOP_WEIGHT_F * 2));
		KNIGHT_WEIGHT = (int) (Math.random() * (KNIGHT_WEIGHT_F * 2));
		QUEEN_WEIGHT = (int) (Math.random() * (QUEEN_WEIGHT_F * 2));
		KING_WEIGHT = (int) (Math.random() * (KING_WEIGHT_F * 2));
		ROOK_ALONE_BONUS = (int) (Math.random() * (ROOK_ALONE_BONUS_F * 2));
		PAWN_ISOLATION_PENALTY = (int) (-1 * (Math.random() * (Math.abs(PAWN_ISOLATION_PENALTY_F) * 2)));
		PAWN_BACKWARD_PENALTY = (int) (-1 * (Math.random() * (Math.abs(PAWN_BACKWARD_PENALTY_F) * 2)));
		PAWN_DOUBLED_PENALTY = (int) (-1 * (Math.random() * (Math.abs(PAWN_DOUBLED_PENALTY_F) * 2)));
		KING_SHIELDED_BONUS = (int) (Math.random() * (Math.abs(PAWN_DOUBLED_PENALTY_F) * 2));
		KING_PAWN_STORM = (int) (Math.random() * (Math.abs(KING_PAWN_STORM_F) * 2));
	}
	
	public  ArrayList<Rook> myRooks = new ArrayList<Rook>();
	public  ArrayList<Pawn> myPawns = new ArrayList<Pawn>();
	public  ArrayList<Bishop> myBishops = new ArrayList<Bishop>();
	public  ArrayList<Queen> myQueens = new ArrayList<Queen>();
	public  ArrayList<King> myKings = new ArrayList<King>();
	public ArrayList<Knight> myKnights = new ArrayList<Knight>();
	
	public void setKing(King king) {
		myKings.set(0, king);
	}
	
	public Player(boolean whiteTeam) {
		isOnWhiteTeam = whiteTeam;
		for (ChessPiece piece : getMyTeam()) {
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
		myPawns.clear();
		myRooks.clear();
		myBishops.clear();
		myKnights.clear();
		myQueens.clear();
		myKings.clear();
		for (ChessPiece piece : getMyTeam()) {
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
		if (piece.isOnWhiteTeam == isOnWhiteTeam) {

			ChessBoard.move(piece, pos);
			if (piece instanceof Pawn) {
				
				((Pawn) piece).hasMoved = true;
			}
		}

	}
	public void restore(ChessPiece piece) {
		ChessPiece thing = piece;
		if (thing instanceof Pawn) myPawns.add((Pawn)thing);
		else if (thing instanceof Knight) myKnights.add((Knight)thing);
		else if (thing instanceof Rook) myRooks.add((Rook)thing);
		else if (thing instanceof Queen) myQueens.add((Queen)thing);
		else if (thing instanceof King) myKings.add((King)thing);
		else if (thing instanceof Bishop) myBishops.add((Bishop)thing);

	}

	/*public int gameOver() {
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
				ChessBoard.move(oppPiece, movePosition);
				if (!opponentIsInCheck()) {//this means there's a way for the opponent to not move into check, so the game's not a stale mate
					ChessBoard.move(oppPiece,  new Position(oldColumn, oldRow));
					ChessBoard.setChessPiece(movePosition.column,  movePosition.row,  oldOccupant);
					//ChessBoard.setBoard(oldBoard);
					//oppPiece.setPosition(new Position(oldColumn, oldRow));
					return -1;

				}
				else {
					ChessBoard.move(oppPiece,  new Position(oldColumn, oldRow));
					ChessBoard.setChessPiece(movePosition.column,  movePosition.row,  oldOccupant);
					//ChessBoard.setBoard(oldBoard);
					//oppPiece.setPosition(new Position(oldColumn, oldRow));
					//ChessBoard.setBoard(oldBoard);
					//oppPiece.setPosition(new Position(oldColumn, oldRow));//restores board back to old state before simulation
				}

			}
		}
		if (!opponentIsInCheck()) {
			return 0;
		}
		else return 1;
	}*/

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
					ChessBoard.move(oppPiece, movePosition);
					if (!opponentIsInCheck()) {//this means there's a way for the opponent to not move into check, so the game's not a stale mate
						ChessBoard.move(oppPiece,  new Position(oldColumn, oldRow));
						ChessBoard.setChessPiece(movePosition.column,  movePosition.row,  oldOccupant);
						//ChessBoard.setBoard(oldBoard);
						//oppPiece.setPosition(new Position(oldColumn, oldRow));
						return false; 

					}
					else {
						ChessBoard.move(oppPiece,  new Position(oldColumn, oldRow));
						ChessBoard.setChessPiece(movePosition.column,  movePosition.row,  oldOccupant);
						//ChessBoard.setBoard(oldBoard);
						//oppPiece.setPosition(new Position(oldColumn, oldRow));
						//ChessBoard.setBoard(oldBoard);
						//oppPiece.setPosition(new Position(oldColumn, oldRow));//restores board back to old state before simulation
					}

				}
			}
		}
		return true; //if there's no escape, return true
	}

	public boolean hasStaleMate() { //TODO: other stalemate rules (like 3 same in a row, 50 move rule, etc.)
		//checks if the player has won the game 
		if (opponentIsInCheck()) { //opponent must NOT currently be in check to achieve STALEMATE
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
					ChessBoard.move(oppPiece, movePosition);
					if (!opponentIsInCheck()) {//this means there's a way for the opponent to not move into check, so the game's not a stale mate
						ChessBoard.move(oppPiece,  new Position(oldColumn, oldRow));
						ChessBoard.setChessPiece(movePosition.column,  movePosition.row,  oldOccupant);
						//oppPiece.setPosition(new Position(oldColumn, oldRow));
						return false; 

					}
					else {
						ChessBoard.move(oppPiece,  new Position(oldColumn, oldRow));
						ChessBoard.setChessPiece(movePosition.column,  movePosition.row,  oldOccupant);
						//oppPiece.setPosition(new Position(oldColumn, oldRow));
						//oppPiece.setPosition(new Position(oldColumn, oldRow));//restores board back to old state before simulation
					}

				}
			}
		}

		return true; //if there's no escape, return true
	}

	public boolean opponentIsInCheck() { //checks whether you could attack the opponent's king
		ChessPiece[][] oldBoard = new ChessPiece[8][8];

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				oldBoard[i][j] = ChessBoard.getBoard()[i][j];
			}
		}
		for (ChessPiece piece : getMyTeam()) {

			for (Position movePosition : piece.possibleMoves()) {
				int oldColumn = piece.getColumn();
				int oldRow = piece.getRow();
				ChessPiece oldOccupant = ChessBoard.getBoard()[movePosition.column][movePosition.row];
				ChessBoard.move(piece, movePosition);

				if (opponent.myKings.size() == 0) { //if one of your pieces could attack opponent's King
					ChessBoard.move(piece, new Position(oldColumn, oldRow));
					ChessBoard.setChessPiece(movePosition.column,  movePosition.row,  oldOccupant);
					piece.setPosition(new Position(oldColumn, oldRow));
					return true;
				}
				ChessBoard.move(piece, new Position(oldColumn, oldRow));
				ChessBoard.setChessPiece(movePosition.column,  movePosition.row,  oldOccupant);
				//piece.setPosition(new Position(oldColumn, oldRow));
			}
		}

		return false; //if no possible ways to attack opponent king, then he is not in check
	}

	public ArrayList<ChessPiece> getMyTeam() {
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
		try {
			return myKings.get(0);
		} catch (IndexOutOfBoundsException ex) {}
		
		return null;
	}

	public int evaluateMaterial(ChessPiece board[][], Player player) {
		int rank = 0;

		rank = KING_WEIGHT * (player.myKings.size() - player.opponent.myKings.size())
				+ QUEEN_WEIGHT * (player.myQueens.size() - player.opponent.myQueens.size())
				+ ROOK_WEIGHT * (player.myRooks.size() - player.opponent.myRooks.size())
				+ BISHOP_WEIGHT * (player.myBishops.size() - player.opponent.myBishops.size())
				+ KNIGHT_WEIGHT * (player.myKnights.size() - player.opponent.myKnights.size())
				+ PAWN_WEIGHT * (player.myPawns.size() - player.opponent.myPawns.size());

 		return rank;

	}
	public int evaluateMobility(ChessPiece board[][], Player player) {
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

	public int evaluateRookBonus(ChessPiece board[][], Player player) {
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

	public int evaluatePawnBonus(ChessPiece board[][], Player player) { //penalties for isolated or "backwards" pawns
		int count = 0;



		for (Pawn pawn : player.myPawns) {
			int column = pawn.getColumn();
			int row = pawn.getRow();
			int left = column - 1;
			int right = column + 1;
			int bonus;

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
				//count += doubleBonus;
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
				//count += doubleBonus;
			}
			bonus = 0;
			for (Pawn otherPawn : player.myPawns) {
				if (otherPawn.getColumn() == column) {
					if (otherPawn != pawn) {
						bonus = PAWN_DOUBLED_PENALTY;
					}
				}
			}
			count += bonus;


		}
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
				//count -= doubleBonus;
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

	public int evaluatePieceSquare(ChessPiece board[][], Player player) {
		//int score = PieceSquare.whiteScore - PieceSquare.blackScore;
		int score = PieceSquare.whiteScore - PieceSquare.blackScore;
		
		if (player.isOnWhiteTeam) {
			//System.out.println("Time " + (System.nanoTime() - time));
			return score;
		}
		else {
			//System.out.println("Time " + (System.nanoTime() - time));
			return -score;
		}


		/*int count = 0;
		if (!player.isOnWhiteTeam) {
			for (ChessPiece piece : player.getMyTeam()) {
				count += piece.getBlackPS()[piece.getRow()][piece.getColumn()];
			}
			//***********************************************************************************
			for (ChessPiece piece : player.opponent.getMyTeam()) {
				count -= piece.getBlackPS()[7 - piece.getRow()][piece.getColumn()];
			}
		}
		else {
			for (ChessPiece piece : player.getMyTeam()) {
				count += piece.getBlackPS()[7 - piece.getRow()][piece.getColumn()];
			}
			//***********************************************************************************
			for (ChessPiece piece : player.opponent.getMyTeam()) {
				count -= piece.getBlackPS()[piece.getRow()][piece.getColumn()];
			}
		}*/

		//*****************************************************************88

		//**********************************************************************************


		//return count;
	}

	public int evaluateKingBonus(ChessPiece board[][], Player player) {
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

	public int evaluateBoard(ChessPiece board[][]) {
		return evaluateMaterial(board, this) + evaluatePieceSquare(board, this) + evaluateRookBonus(board, this) + evaluatePawnBonus(board, this) + evaluateMobility(board, this)  + evaluateKingBonus(board, this) - evaluateKingBonus(board, this.opponent);
	}

	public int lazyEval(ChessPiece board[][], int alpha, int beta) {
		
		int counter = evaluateMaterial(board, this) + evaluatePieceSquare(board, this);
		if (counter < alpha - 50 ) {
			return counter;
		}
		else {
			counter +=(evaluateRookBonus(board, this) + evaluatePawnBonus(board, this)) + /*evaluateMobility(board, player) +*/ evaluateKingBonus(board, this) - evaluateKingBonus(board, this.opponent);
			return counter;
		}

	}
	
	public void setWeight(int geneCode, int value) {
		switch (geneCode) {
		case 0:
			PAWN_WEIGHT = value;
			break;
		case 1:
			ROOK_WEIGHT = value;
			break;
		case 2:
			BISHOP_WEIGHT = value;
			break;
		case 3:
			KNIGHT_WEIGHT = value;
			break;
		case 4:
			QUEEN_WEIGHT = value;
			break;
		case 5:
			KING_WEIGHT = value;
			break;
		case 6:
			ROOK_ALONE_BONUS = value;
			break;
		case 7:
			PAWN_ISOLATION_PENALTY = value;
			break;
		case 8:
			PAWN_BACKWARD_PENALTY = value;
			break;
		case 9:
			PAWN_DOUBLED_PENALTY = value;
			break;
		case 10:
			KING_SHIELDED_BONUS = value;
			break;
		case 11:
			KING_PAWN_STORM = value;
			break;
		default:
			break;
		}
	}

	public double getGeneToMutateOriginalValue(int geneToMutate) {
		switch (geneToMutate) {
		case 0:
			return PAWN_WEIGHT_F;
		case 1:
			return ROOK_WEIGHT_F;
		case 2:
			return BISHOP_WEIGHT_F;
		case 3:
			return KNIGHT_WEIGHT_F;
		case 4:
			return QUEEN_WEIGHT_F;
		case 5:
			return KING_WEIGHT_F;
		case 6:
			return ROOK_ALONE_BONUS_F;
		case 7:
			return PAWN_ISOLATION_PENALTY_F;
		case 8:
			return PAWN_BACKWARD_PENALTY_F;
		case 9:
			return PAWN_DOUBLED_PENALTY_F;
		case 10:
			return KING_SHIELDED_BONUS_F;
		case 11:
			return KING_PAWN_STORM_F;
		default:
			return -999;
		}
	}


}

