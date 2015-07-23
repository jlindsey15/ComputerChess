import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ChessGame {
	public static Player player1;
	public static Player player2;
	public static Player currentPlayer;
	public static Player otherPlayer;

	public static Move[] pv = new Move[100];

	private static int moveCount = 0;
	private static int currentDepth = 0;
	
	public static boolean gameOver = false;

	public static ArrayList<Move> sortMoves(ArrayList<Move> moves) {
		Collections.sort(moves);
		return moves;
	}

	public static void InitializeGame() {
		GenerationManager.Initialize();
		GenerationManager.StartNewGame();
		
		currentPlayer = GenerationManager.player1;
		otherPlayer = GenerationManager.player2;
	}
	
	public static void UpdateGame() {
		if (moveCount >= 200) {			
			if (currentPlayer.evaluateBoard(ChessBoard.getBoard()) > otherPlayer.evaluateBoard(ChessBoard.getBoard())) GameEnded(currentPlayer, otherPlayer);
			else GameEnded(otherPlayer, currentPlayer);
			
			return;
		}

		if (otherPlayer.hasStaleMate()) {
			moveCount++;			
			ReplayGame();
			
			return;
		}
		if (otherPlayer.hasWon()) {			
			moveCount++;			
			GameEnded(otherPlayer, currentPlayer);
			
			return;
		}
		moveCount++;

		UpdateAI(currentPlayer.getMyTeam());
	
		Player temp = currentPlayer;
		currentPlayer = otherPlayer;
		otherPlayer = temp;

	}
	
	public static void ReplayGame() {
		ChessBoard.ResetGame(currentPlayer, otherPlayer);
	}
	
	public static void GameEnded(Player winner, Player loser) {
		GenerationManager.GameEnded(winner, loser);
		ChessBoard.ResetGame(winner, loser);	
		GenerationManager.StartNewGame();
		
		currentPlayer = GenerationManager.player1;
		otherPlayer = GenerationManager.player2;
				
		moveCount = 0;
	}

	private static void UpdateAI(ArrayList<ChessPiece> pieces) {
		
		Move bestMove = null;
		int bestIndex = -1;
		int depth = 3; //must be >=1, if depth is less than 1 it just acts like it equals 1
		//ChessPiece bestPiece = null;
		//Position bestMove = null;
		int startColumn = -1;
		int startRow = -1;
		int endColumn = -1;
		int endRow = -1;
		int max = Integer.MIN_VALUE; //so that it'll definitely be replaced with a real value
		
		
		
		
		Player temp;
		ArrayList<Move> moves = new ArrayList<Move>();

		for (ChessPiece piece : currentPlayer.getMyTeam())  {
			for (Position pos : piece.possibleMoves()) 
			{
				Move theMove = new Move(pos, piece);
				if (theMove.equals(pv[0])) {
					moves.add(0, theMove);
				}
				else moves.add(theMove);
			}
		}
		for (int i = 1; i <= depth; i++) {
			currentDepth = i;
			int counter = 0;
			max = Integer.MIN_VALUE;
			int oldColumn;
			int oldRow;
			ChessPiece oldOccupant;
			ChessPiece piece;
			Position pos;
			
			for (int j = 0; j < moves.size(); j++) {
				

				Move move = moves.get(j);
				counter++;
				piece = move.piece;
				pos = move.position;

				oldColumn = piece.getColumn();
				oldRow = piece.getRow();
				oldOccupant = ChessBoard.getBoard()[pos.column][pos.row];
				/*if (currentPlayer.isOnWhiteTeam) {
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
				}*/
				ChessBoard.move(piece,  pos);

				temp = otherPlayer;
				otherPlayer = currentPlayer;
				currentPlayer = temp;
				int score = -negaMax(i -1, Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1);
				temp = otherPlayer;
				otherPlayer = currentPlayer;
				currentPlayer = temp;
				ChessBoard.move(piece,  new Position(oldColumn, oldRow));
				ChessBoard.setChessPiece(pos.column,  pos.row,  oldOccupant);
				if( score > max ) {
					max = score;
					bestMove = move;
					pv[0] = move;
					bestIndex = j;

				}


			}
			if (i == depth) {
				try {
				
					oldColumn = bestMove.piece.getColumn();
					oldRow = bestMove.piece.getRow();
					oldOccupant = ChessBoard.getBoard()[bestMove.position.column][bestMove.position.row];
		
					currentPlayer.makeMove(bestMove.piece, bestMove.position);
				} catch (NullPointerException ex){}
			}
		}


		try {
			if (bestMove.piece instanceof Pawn) {
				if ((bestMove.piece.isOnWhiteTeam && endRow == 7) || (!bestMove.piece.isOnWhiteTeam && endRow == 0)) {
					Queen queen = new Queen(endColumn, endRow, bestMove.piece.isOnWhiteTeam);
					queen.player = ChessGame.currentPlayer;
					ChessBoard.setChessPiece(endColumn,  endRow,  queen);
				}
			}
		} catch (NullPointerException ex) {}
	}


	public static int negaMax(int depth, int alpha, int beta) {
		boolean bSearchPv = true;
		int bestScore = Integer.MIN_VALUE;
		int score;
		Player temp;
		int FullDepthMoves = 4;
		int ReductionLimit = 3;
		int otherKings = otherPlayer.myKings.size();
		int currentKings = currentPlayer.myKings.size();

		// nextBoard.showBoard(
		
		if (currentPlayer.myKings.size() < 1) {
			return Integer.MIN_VALUE;
		}
		if (otherPlayer.myKings.size() < 1) {
			return Integer.MAX_VALUE;
		}
		if (depth <= 0) {
			return currentPlayer.lazyEval(ChessBoard.getBoard(), alpha, beta);
		}
		int R = 4; //for null move pruning
		temp = otherPlayer;
		otherPlayer = currentPlayer;
		currentPlayer = temp;
		score = -negaMax (depth - R, -beta, -beta + 1); //null move pruning
		temp = otherPlayer;
		otherPlayer = currentPlayer;
		currentPlayer = temp;


		ArrayList<Move> moves = new ArrayList<Move>();

		for (ChessPiece piece : currentPlayer.getMyTeam())  {
			for (Position pos : piece.possibleMoves()) 
			{
				Move theMove = new Move(pos, piece);
				if (theMove.equals(pv[currentDepth - depth])) {
					moves.add(0, theMove);
				}
				else moves.add(theMove);
			}
		}

		

		int movesSearched = 0;
		/*if (pv[currentDepth - depth] != null) {
			moves.remove(pv[currentDepth - depth]);
			moves.add(0, pv[currentDepth - depth]);
		}*/
		for (Move move : moves) {
			ChessPiece piece = move.piece;
			Position pos = move.position;
			int oldColumn = piece.getColumn();
			int oldRow = piece.getRow();
			ChessPiece oldOccupant = ChessBoard.getBoard()[pos.column][pos.row];
			int initialWhitePS = PieceSquare.whiteScore;
			int initialBlackPS = PieceSquare.blackScore;
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
			ChessBoard.move(piece,  pos);
			
			temp = otherPlayer;
			otherPlayer = currentPlayer;
			currentPlayer = temp;
			
			// nextBoard.showBoard("next", board);
			if(movesSearched == 0) // First move, use full-window search
				score = -negaMax(depth - 1, -beta, -alpha);
			else {
				if(movesSearched >= FullDepthMoves && depth >= ReductionLimit) //late move reductions
					// Search this move with reduced depth:
					score = -negaMax(depth - 2, -(alpha+1), -alpha);
				else score = alpha+1;  // ensures full-depth search
				// is done.
				if(score > alpha) { //pvs
					score = -negaMax(depth - 1, -(alpha+1), -alpha);
					if(score > alpha && score < beta)
						score = -negaMax(depth -1, -beta, -alpha);
				}
			}
			

			temp = otherPlayer;
			otherPlayer = currentPlayer;
			currentPlayer = temp;
			if (currentPlayer.myKings.size() != 1 || otherPlayer.myKings.size() !=1) {
			}
			ChessBoard.move(piece,  new Position(oldColumn, oldRow));
			ChessBoard.setChessPiece(pos.column,  pos.row,  oldOccupant);

			PieceSquare.whiteScore = initialWhitePS;
			PieceSquare.blackScore = initialBlackPS;
			if (score > bestScore) {
				bestScore = score;
				pv[currentDepth - depth] = move;
				if(score>=alpha) {
					alpha=score;
					bSearchPv = false;
				}
			}

			if(score>=beta) return score;
			movesSearched++;
		}





		return alpha;
	}

}