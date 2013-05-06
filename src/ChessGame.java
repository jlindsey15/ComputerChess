import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
//Still to do:
/*

4. quiescence
5. En passant
6. piece square learning
7. additional efficiency improvements*/


public class ChessGame {
	//human player
	public static Player player1; 
	//Computer player
	public static Player player2;

	//player whose turn it is
	public static Player currentPlayer;

	//other player
	public static Player otherPlayer;

	//time left in game in seconds - initial value is variable depending on game rules
	public static int timeLeft = 60*20;

	//moves made by computer
	public static int movesMade = 0; 

	//principle variation - AKA the move sequence that the computer thinks is best
	public static Move[] pv = new Move[100];

	//move count in game
	private static int moveCount = 0; 

	private static int currentDepth = 0;

	public static ArrayList<Move> sortMoves(ArrayList<Move> moves) {
		Collections.sort(moves);
		return moves;

	}

	public static void InitializeGame() { 
		//starts game - self-explanatory
		player1 = new Player(true);
		player2 = new Player(false);

		player1.setOpponent(player2); 
		player2.setOpponent(player1);
		currentPlayer = player1;
		otherPlayer = player2;
	}

	public static void UpdateGame() {
		//this is called when it's the computer's turn to move, so we set currentPlayer and otherPlayer accordingly
		currentPlayer = player2;
		otherPlayer = player1;

		if (otherPlayer.hasStaleMate()) { //stop if stalemate
			System.out.println("Stalemate - Moves made to tie  game: " + moveCount);
			moveCount++;
			ChessApplication.UpdateDisplay();
			return;
		}
		if (otherPlayer.hasWon()) { //stop if checkmate
			System.out.println("Someone has one: " + moveCount);
			moveCount++;
			ChessApplication.UpdateDisplay();
			return;
		}
		moveCount++;
		ChessApplication.UpdateDisplay();

		//assuming the game's not over, actually calculate and perform a move:
		UpdateAI(currentPlayer.getMyTeam());

		ChessApplication.UpdateDisplay();

		//now it's the human's turn:
		currentPlayer = player1;
		otherPlayer = player2;

	}

	private static void UpdateAI(ArrayList<ChessPiece> pieces) {
		//This method is where it's at

		long time = System.nanoTime();
		Move bestMove = null;

		//must be >=1, if depth is less than 1 it just acts like it equals 1.  We make it big so the time control takes precedence
		int depth = 10;

		//determines the amount of time to be spent on this move.  The following procedure seems reasonable since most chess games are ~40 moves
		int maxTime;
		if (movesMade >= 40) {
			maxTime = timeLeft/10 - 1;
		}
		else maxTime = timeLeft/(45 - movesMade) - 2; 
		if (maxTime < 3) {
			maxTime = 3;
		}
		System.out.println("max time" + maxTime);

		//start Time of move

		int startTime = (int)(System.nanoTime()/1000000000);


		int max = Integer.MIN_VALUE+1; //so that it'll definitely be replaced with a real value
		boolean keepGoing = true;  //set to false when we've run out of time to make the move



		Player temp;
		ArrayList<Move> moves = new ArrayList<Move>();



		//System.out.println("*******************************888");
		for (int i = 1; i <= depth; i++) {
			//iterative move deepening : first do 1-ply search, then 2-ply, then 3-ply, etc. till time runs out
			moves.clear();
			//creates list of all possible moves:
			for (ChessPiece piece : currentPlayer.getMyTeam())  {
				for (Position pos : piece.possibleMoves()) 
				{
					//We allow danger moves since the computer will never pick them anyway
					Move theMove = new Move(pos, piece);
					if (theMove.equals(pv[0])) { 
						//move ordering for increased alpha beta pruning efficiency - take the best move from the last ply depth iteration and examine it first
						moves.add(0, theMove);
					}
					else moves.add(theMove);
				}
			}

			//adds castling to possible moves, if applicable:
			if (currentPlayer.getKing().castleAllowed && !currentPlayer.hasCastled) { //can't castle if king has moved
				if (!currentPlayer.opponent.opponentIsInCheck()) { //can't castle out of check
					for (Rook rook : currentPlayer.myRooks) {
						if (rook.castleAllowed) { //can't castle if rook has moved
							if (rook.getColumn() == 0) {
								Position castlePos = new Position(1, currentPlayer.getKing().getRow());
								castlePos.isCastle = true; //distinguishes from regular move
								castlePos.myRook = rook;
								King king = currentPlayer.getKing();
								int rookColumn = rook.getColumn();
								int kingColumn = king.getColumn();
								int theRow = king.getRow();
								boolean doIt = true;
								for (int x = Math.min(rookColumn, kingColumn) + 1; x < Math.max(rookColumn, kingColumn); x ++ ) {
									if (ChessBoard.isOccupied(x, theRow)) {
										//can't castle through occupied position
										doIt = false;
									}
								}
								if (doIt){
									Move castler = new Move(castlePos, king);
									castler.isCastle = true;
									//adds the castle move
									if (castler.equals(pv[0])) { 
										//move ordering for increased alpha beta pruning efficiency - take the best move from the last ply depth iteration and examine it first
										moves.add(0, castler);
									}
									else moves.add(castler);
								}
							}
							else if (rook.getColumn() == 7) {
								Position castlePos = new Position(6, currentPlayer.getKing().getRow());
								castlePos.isCastle = true;
								castlePos.myRook = rook;
								King king = currentPlayer.getKing();
								int rookColumn = rook.getColumn();
								int kingColumn = king.getColumn();
								int theRow = king.getRow();
								boolean doIt = true;
								for (int x = Math.min(rookColumn, kingColumn) + 1; x < Math.max(rookColumn, kingColumn); x ++ ) {
									if (ChessBoard.isOccupied(x, theRow)) {
										//can't castle through occupied position
										doIt = false;
									}
								}
								if (doIt){
									Move castler = new Move(castlePos, king);
									castler.isCastle = true;
									//ads the castle move
									if (castler.equals(pv[0])) { 
										//move ordering for increased alpha beta pruning efficiency - take the best move from the last ply depth iteration and examine it first
										moves.add(0, castler);
									}
									else moves.add(castler);
								}
							}
						}
					}
				}
			}

			//the current depth is, well, the current depth...
			currentDepth = i;

			int counter = 0;

			max = Integer.MIN_VALUE+1; //so that it'll definitely be replaced
			int oldColumn;
			int oldRow;
			ChessPiece oldOccupant;
			ChessPiece piece;
			Position pos;

			for (int j = 0; j < moves.size(); j++) {
				if ((int)(System.nanoTime()/1000000000) - startTime > maxTime) { 
					//if computer has run out of time to make the move
					System.out.println("incompletely finished searching to depth " + currentDepth + "move " + counter);
					keepGoing = false;
					break;
				}


				Move move = moves.get(j);
				counter++;
				//System.out.println("Move " + counter + " out of " + moves.size());

				piece = move.piece;
				pos = move.position;

				oldColumn = piece.getColumn();
				oldRow = piece.getRow();
				oldOccupant = ChessBoard.getBoard()[pos.column][pos.row];

				//for Piece Square incrementation undoing
				int oldWhiteScore = PieceSquare.whiteScore; 
				int oldBlackScore = PieceSquare.blackScore;

				Rook rook = null; //used if move is a castle

				//increments piece square tables:

				if (currentPlayer.isOnWhiteTeam) {
					PieceSquare.whiteScore -= piece.getBlackPS()[7 - oldRow][oldColumn];
					PieceSquare.whiteScore += piece.getBlackPS()[7 - pos.row][pos.column];
					if (oldOccupant != null && !oldOccupant.isOnWhiteTeam) {
						PieceSquare.blackScore -= oldOccupant.getBlackPS()[pos.row][pos.column];
					}
				}
				else {
					PieceSquare.blackScore -= piece.getBlackPS()[oldRow][oldColumn];
					PieceSquare.blackScore += piece.getBlackPS()[pos.row][pos.column];
					if (oldOccupant != null && oldOccupant.isOnWhiteTeam) {
						PieceSquare.whiteScore -= oldOccupant.getBlackPS()[7 - pos.row][pos.column];
					}
				}

				//used in move undoing
				boolean storedCastleAllowed = true; 

				if (move.isCastle) {
					//if move is a castle, do a castle
					ChessBoard.doCastle(piece, move.position.column, move.position.row);
					rook = move.position.myRook;
					((King)piece).castleAllowed = false;
					rook.castleAllowed = false;

				}
				else {
					//if move is not a castle, do a regular move
					ChessBoard.move(piece,  pos);

					//if you moved a rook or a king, those pieces can't castle anymore
					if (piece instanceof Rook) {
						storedCastleAllowed = ((Rook)(piece)).castleAllowed;
						((Rook)(piece)).castleAllowed = false;
					}
					if (piece instanceof King) {
						storedCastleAllowed = ((King)(piece)).castleAllowed;
						((King)(piece)).castleAllowed = false;
					}
				}


				//you've done your move, so switch the current player
				temp = otherPlayer;
				otherPlayer = currentPlayer;
				currentPlayer = temp;

				//evaluate the board state after the move using the negamax algorithm:
				int score;
				if (otherPlayer.hasStaleMate()) {
					//only checks for stale mates one move ahead.  Search would take way too long otherwise.
					//Not ideal, but keeps you out of doing stupid stalemates.
					score = 0;
				}
				else {
					score = -negaMax(i -1, Integer.MIN_VALUE+1 + 1, Integer.MAX_VALUE-1 - 1);
				}

				//switch the current player back
				temp = otherPlayer;
				otherPlayer = currentPlayer;
				currentPlayer = temp;


				//undo move:
				if (move.isCastle) {
					ChessBoard.undoCastle(piece, rook);
				}
				else {
					//more undoing:

					ChessBoard.move(piece,  new Position(oldColumn, oldRow));
					ChessBoard.setChessPiece(pos.column,  pos.row,  oldOccupant);

					if (piece instanceof Rook) {
						((Rook)(piece)).castleAllowed = storedCastleAllowed;

					}
					else if (piece instanceof King) {
						((King)(piece)).castleAllowed = storedCastleAllowed;
					}
				}

				//undo piece square incrementations
				PieceSquare.whiteScore = oldWhiteScore;
				PieceSquare.blackScore = oldBlackScore;

				if( score > max ) {
					//if you've found a move better than any others, change the best move to that one
					//System.out.println("score " + score);
					//System.out.println("max " + max);
					//System.out.println(move.piece);
					max = score;
					bestMove = move;
					pv[0] = move; //update principle variation - used in move ordering

				}


			}
			if (!keepGoing) {
				//if you have to stop the search because time runs out, make your final move using bestMove
				movesMade++;
				oldColumn = bestMove.piece.getColumn();
				oldRow = bestMove.piece.getRow();
				oldOccupant = ChessBoard.getBoard()[bestMove.position.column][bestMove.position.row];


				//Piece square incrementation:
				if (currentPlayer.isOnWhiteTeam) {
					PieceSquare.whiteScore -= bestMove.piece.getBlackPS()[7 - oldRow][oldColumn];
					PieceSquare.whiteScore += bestMove.piece.getBlackPS()[7 - bestMove.position.row][bestMove.position.column];
					if (oldOccupant != null && !oldOccupant.isOnWhiteTeam) {
						PieceSquare.blackScore -= oldOccupant.getBlackPS()[bestMove.position.row][bestMove.position.column];
					}
				}
				else {
					PieceSquare.blackScore -= bestMove.piece.getBlackPS()[oldRow][oldColumn];
					PieceSquare.blackScore += bestMove.piece.getBlackPS()[bestMove.position.row][bestMove.position.column];
					if (oldOccupant != null && oldOccupant.isOnWhiteTeam) {
						PieceSquare.whiteScore -= oldOccupant.getBlackPS()[7 - bestMove.position.row][bestMove.position.column];
					}
				}
				if (bestMove.isCastle) {
					//if it's a castle, do a castle move
					ChessBoard.doCastle(bestMove.piece, bestMove.position.column, bestMove.position.row);
				}
				else {
					//if not do a regular move
					currentPlayer.makeMove(bestMove.piece, bestMove.position);
				}

				//I've explained this before...
				if (bestMove.piece instanceof Rook) {
					((Rook)(bestMove.piece)).castleAllowed = false;
				}
				if (bestMove.piece instanceof King) {
					((King)(bestMove.piece)).castleAllowed = false;
				}
				System.out.println((System.nanoTime() - time)/1000000000.0);
				timeLeft -= (System.nanoTime() - time)/1000000000;
				return;
			}
		}







	}


	public static int negaMax(int depth, int alpha, int beta) {
		//negamax algorithm.  Evaluates a board with respect to the current player
		int bestScore = Integer.MIN_VALUE+1;
		int score;
		Player temp;
		int FullDepthMoves = 4;
		int ReductionLimit = 3;
		int otherKings = otherPlayer.myKings.size();
		int currentKings = currentPlayer.myKings.size();

		
		// nextBoard.showBoard(

		
		if (depth <= 0) {
			//evaluate the board using heuristics if you've reached the depth limit
			return Player.lazyEval(ChessBoard.getBoard(), currentPlayer, alpha, beta);
		}
		int R = 4; //for null move pruning

		//null move pruning: (i.e. see if not moving would still give value outside alpha beta range)
		//I don't check for zugzwang positions yet - TODO
		temp = otherPlayer;
		otherPlayer = currentPlayer;
		currentPlayer = temp;
		
		//null move pruning:
		score = -negaMax (depth - R, -beta, -beta + 1); 
		
		
		if (score >= beta ) {
		      // 
			temp = otherPlayer;
			otherPlayer = currentPlayer;
			currentPlayer = temp;
		      return beta; // null move pruning
		}
		temp = otherPlayer;
		otherPlayer = currentPlayer;
		currentPlayer = temp;


		ArrayList<Move> moves = new ArrayList<Move>();

		for (ChessPiece piece : currentPlayer.getMyTeam())  {
			for (Position pos : piece.possibleMoves()) {
				//creates list of possible moves - note we're allowing danger moves to improve efficiency, since the computer will never
				//pick them anyway
				Move theMove = new Move(pos, piece);
				if (theMove.equals(pv[currentDepth - depth])) {
					//move ordering from the principle variation from iterative move deepening
					moves.add(0, theMove);
				}
				else moves.add(theMove);
			}
		}


		//adds castling to possible moves if applicable

		if (currentPlayer.getKing().castleAllowed && !currentPlayer.hasCastled) { //can't castle if king has moved
			//note that we're allowing castling out of check in these fake simulated moves.  This produces a bit of inaccuracy, but
			//the additional speed gained by not having to check for checks each time is worth it
				for (Rook rook : currentPlayer.myRooks) {
					if (rook.castleAllowed) { //can't castle if rook has moved
						if (rook.getColumn() == 0) {
							Position castlePos = new Position(1, currentPlayer.getKing().getRow());
							castlePos.isCastle = true;
							castlePos.myRook = rook;
							King king = currentPlayer.getKing();
							int rookColumn = rook.getColumn();
							int kingColumn = king.getColumn();
							int theRow = king.getRow();
							boolean doIt = true;
							for (int x = Math.min(rookColumn, kingColumn) + 1; x < Math.max(rookColumn, kingColumn); x ++ ) {
								if (ChessBoard.isOccupied(x, theRow)) {
									//can't castle through occupied space
									doIt = false;
								}
							}
							if (doIt){
								Move castler = new Move(castlePos, king);
								castler.isCastle = true;
								//add castle move
								moves.add(castler);
							}
						}
						else if (rook.getColumn() == 7) {
							Position castlePos = new Position(6, currentPlayer.getKing().getRow());
							castlePos.isCastle = true;
							castlePos.myRook = rook;
							King king = currentPlayer.getKing();
							int rookColumn = rook.getColumn();
							int kingColumn = king.getColumn();
							int theRow = king.getRow();
							boolean doIt = true;
							for (int x = Math.min(rookColumn, kingColumn) + 1; x < Math.max(rookColumn, kingColumn); x ++ ) {
								if (ChessBoard.isOccupied(x, theRow)) {
									//can't castle through occupied space
									doIt = false;
								}
							}
							if (doIt){
								Move castler = new Move(castlePos, king);
								castler.isCastle = true;
								//add castle move
								moves.add(castler);
							}
						}
					}
				}
			

		}


		//All the following is basically the same as in UpdateAI()

		int movesSearched = 0;

		for (Move move : moves) {
			
			boolean storedCastleAllowed = true; //used in castle undoing

			ChessPiece piece = move.piece;
			if (piece instanceof Rook) {
				storedCastleAllowed = ((Rook)(piece)).castleAllowed;
				((Rook)(piece)).castleAllowed = false;

			}
			if (piece instanceof King) {
				storedCastleAllowed = ((King)(piece)).castleAllowed;
				((King)(piece)).castleAllowed = false;
			}
			Position pos = move.position;
			int oldColumn = piece.getColumn();
			int oldRow = piece.getRow();

			ChessPiece oldOccupant = ChessBoard.getBoard()[pos.column][pos.row];
			int oldWhiteScore = PieceSquare.whiteScore;
			int oldBlackScore = PieceSquare.blackScore;
			Rook rook = null;
			if (oldOccupant instanceof King) {
				//if you killed their king you won - return infinity basically
				return Integer.MAX_VALUE-1;
			}
			
			//do piece square incrementation

			if (currentPlayer.isOnWhiteTeam) {
				PieceSquare.whiteScore -= piece.getBlackPS()[7 - oldRow][oldColumn];
				PieceSquare.whiteScore += piece.getBlackPS()[7 - pos.row][pos.column];
				if (oldOccupant != null && !oldOccupant.isOnWhiteTeam) {
					PieceSquare.blackScore -= oldOccupant.getBlackPS()[pos.row][pos.column];
				}
			}
			else {
				PieceSquare.blackScore -= piece.getBlackPS()[oldRow][oldColumn];
				PieceSquare.blackScore += piece.getBlackPS()[pos.row][pos.column];
				if (oldOccupant != null && oldOccupant.isOnWhiteTeam) {
					PieceSquare.whiteScore -= oldOccupant.getBlackPS()[7 - pos.row][pos.column];
				}
			}

			if (move.isCastle) {
				//do castle
				
				Rook aRook = ChessBoard.doCastle(piece, move.position.column, move.position.row);
				rook = move.position.myRook;


			}

			else {
				//do regular move
				ChessBoard.move(piece,  pos);


			}

			
			//switch current player
			temp = otherPlayer;
			otherPlayer = currentPlayer;
			currentPlayer = temp;

			// nextBoard.showBoard("next", board);
			if(movesSearched == 0){
				// First move - searched completely
				score = -negaMax(depth - 1, -beta, -alpha);
			}
				
			else {
				if(movesSearched >= FullDepthMoves && depth >= ReductionLimit) //late move reductions
					// Search with reduced depth:
					score = -negaMax(depth - 2, -(alpha+1), -alpha);
				else score = alpha+1;  // hack to get it to do the full search

				if(score > alpha) { 
					//principle variation search
					score = -negaMax(depth - 1, -(alpha+1), -alpha);
					if(score > alpha && score < beta)
						score = -negaMax(depth -1, -beta, -alpha);
				}
			}

			//switch players back
			
			temp = otherPlayer;
			otherPlayer = currentPlayer;
			currentPlayer = temp;
			if (move.isCastle) {
				//undo castle

				ChessBoard.undoCastle(piece, rook);

			}
			else {
				//undo move
				ChessBoard.move(piece,  new Position(oldColumn, oldRow));
				ChessBoard.setChessPiece(pos.column,  pos.row,  oldOccupant);

				
				if (piece instanceof Rook) {
					((Rook)(piece)).castleAllowed = storedCastleAllowed;

				}
				else if (piece instanceof King) {
					((King)(piece)).castleAllowed = storedCastleAllowed;
				}

			}
			
			//undo piece sqare incrementations
			PieceSquare.whiteScore = oldWhiteScore;
			PieceSquare.blackScore = oldBlackScore;

			if (score > bestScore) {
				//if you've found a new best move///
				bestScore = score;
				pv[currentDepth - depth] = move;
				if(score>=alpha) {
					alpha=score; //alpha pruning
				}
			}

			if(score>=beta) return score; //beta pruning
			movesSearched++;
		}





		return alpha;
	}

}
