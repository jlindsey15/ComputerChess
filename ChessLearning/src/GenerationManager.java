import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


public class GenerationManager {
	public static Player player1;
	public static Player player2;
	
	private static ArrayList<PlayerMatch> generation = new ArrayList<PlayerMatch>();
	private static ArrayList<Player> winningGeneration = new ArrayList<Player>();
			
	private static final int GENERATION_SIZE = 14;
	private static int gameCount;
	
	public static void Initialize() {
		BreedNewGeneration();
	}
	
	public static void StartNewGame() {
		gameCount++;
		
		PlayerMatch match = generation.get(gameCount - 1);
				
		player1 = match.getPlayer1();
		player2 = match.getPlayer2();
		
		player1.isOnWhiteTeam = true;
		player2.isOnWhiteTeam = false;
		
		player1.refreshArrayLists();
		player2.refreshArrayLists();
		
		player1.setOpponent(player2);
		player2.setOpponent(player1);
		
		match.SetPlayers(player1, player2);
		generation.set(0, match);
	}
	
	public static void GameEnded(Player winner, Player loser) {
		System.out.println("Game Count: " + gameCount);
		
		PlayerMatch match = generation.get(gameCount - 1);
		match.EndGame(winner, loser);
		generation.set(gameCount - 1, match);
				
		if (gameCount >= 66)
			CreateWinningGenerationList();
	}
	
	public static void CreateWinningGenerationList() {		
		Player winners[] = new Player[6];
		
		for (int i = 0; i < 6; ++i) {
			winners[i] = new Player();
		}
		
		for (int i = 0; i < generation.size(); ++i) {
			Player play1 = generation.get(i).getWinner();
			
			for (int j = 0; j < 6; ++j) {				
				if (play1 != winners[j] && play1.getGamesWon() >= winners[j].getGamesWon()) {
					Player temp = winners[j];
					winners[j] = play1;
					if (j < 5) winners[j + 1] = temp;
				} 
			}
		}
		
		for (int i = 0; i < 6; ++i) {
			System.out.println(winners[i].getGamesWon());
		}
		
		generation.clear();
		BreedNewGeneration();
	}
	
	private static void BreedNewGeneration() {
		ArrayList<Player> playablePlayers = new ArrayList<Player>();
		
		if (winningGeneration.size() == 0) {
			generation.clear();
					
			for (int i = 0; i < (GENERATION_SIZE/2) - 1; i++) {
				Player player1 = new Player(true);
				Player player2 = new Player(false);
				
				player2.setWeights(null);
				
				player1.setOpponent(player2);
				player2.setOpponent(player1);
				
				playablePlayers.add(player1);
				playablePlayers.add(player2);
			}
						
			for (Iterator<Player> iter = playablePlayers.iterator(); iter.hasNext();) {
				Player player = iter.next();
							
				for (int i = 1; i < playablePlayers.size(); ++i) {					
					PlayerMatch match = new PlayerMatch(player, playablePlayers.get(i));
					generation.add(match);
				}
				
				iter.remove();
			}
			
			System.out.println("Size of the matching Array: " + generation.size() + "\n");
		} else {
			System.out.println("There is a winning generation!");
			generation.clear();
			
			try {
				PrintWinningPlayersStatistics();
			} catch (Exception ex) {}
			
			int size = winningGeneration.size();
			System.out.println("Size of the generation creation: " + size);
			
			int i = 0;
			
			while ((playablePlayers.size()) < GENERATION_SIZE - 2) {
				int pawnWeight[] = new int[2];
				int rookWeight[] = new int[2];
				int bishopWeight[] = new int[2];
				int knightWeight[] = new int[2];
				int queenWeight[] = new int[2];
				int kingWeight[] = new int[2];
				int aloneBonus[] = new int[2];
				int isolationPenalty[] = new int[2];
				int backwardPenalty[] = new int[2];
				int doubledPenalty[] = new int[2];
				int shieldBonus[] = new int[2];
				int pawnStorm[] = new int[2];
				
				System.out.println("Currently Breeding New Children with index: " + i);
				
				for (int j = 0; j < 2; ++j) {
					pawnWeight[j] = (Math.random() > 0.5) ? winningGeneration.get(i).PAWN_WEIGHT : winningGeneration.get(i + 1).PAWN_WEIGHT;
					rookWeight[j] = (Math.random() > 0.5) ? winningGeneration.get(i).ROOK_WEIGHT : winningGeneration.get(i + 1).ROOK_WEIGHT;
					bishopWeight[j] = (Math.random() > 0.5) ? winningGeneration.get(i).BISHOP_WEIGHT : winningGeneration.get(i + 1).BISHOP_WEIGHT;
					knightWeight[j] = (Math.random() > 0.5) ? winningGeneration.get(i).KNIGHT_WEIGHT : winningGeneration.get(i + 1).KNIGHT_WEIGHT;
					kingWeight[j] = (Math.random() > 0.5) ? winningGeneration.get(i).KING_WEIGHT : winningGeneration.get(i + 1).KING_WEIGHT;
					queenWeight[j] = (Math.random() > 0.5) ? winningGeneration.get(i).QUEEN_WEIGHT : winningGeneration.get(i + 1).QUEEN_WEIGHT;
					aloneBonus[j]  = (Math.random() > 0.5) ? winningGeneration.get(i).ROOK_ALONE_BONUS : winningGeneration.get(i + 1).ROOK_ALONE_BONUS;
					isolationPenalty[j] = (Math.random() > 0.5) ? winningGeneration.get(i).PAWN_ISOLATION_PENALTY : winningGeneration.get(i + 1).PAWN_ISOLATION_PENALTY;
					backwardPenalty[j] = (Math.random() > 0.5) ? winningGeneration.get(i).PAWN_BACKWARD_PENALTY : winningGeneration.get(i + 1).PAWN_BACKWARD_PENALTY;
					doubledPenalty[j] = (Math.random() > 0.5) ? winningGeneration.get(i).PAWN_DOUBLED_PENALTY : winningGeneration.get(i + 1).PAWN_DOUBLED_PENALTY;
					shieldBonus[j] = (Math.random() > 0.5) ? winningGeneration.get(i).KING_SHIELDED_BONUS : winningGeneration.get(i + 1).KING_SHIELDED_BONUS;
					pawnStorm[j] = (Math.random() > 0.5) ? winningGeneration.get(i).KING_PAWN_STORM : winningGeneration.get(i + 1).KING_PAWN_STORM;
				}
				
				Player player1 = new Player(true);
				Player player2 = new Player(false);
				
				player1.setWeights(pawnWeight[0], rookWeight[0], bishopWeight[0], queenWeight[0], knightWeight[0], kingWeight[0], aloneBonus[0], isolationPenalty[0], backwardPenalty[0], doubledPenalty[0], shieldBonus[0], pawnStorm[0]);
				player2.setWeights(pawnWeight[1], rookWeight[1], bishopWeight[1], queenWeight[1], knightWeight[1], kingWeight[1], aloneBonus[1], isolationPenalty[1], backwardPenalty[1], doubledPenalty[1], shieldBonus[1], pawnStorm[1]);

				Player player3 = new Player(true);
				Player player4 = new Player(false);
				
				player3.setWeights(pawnWeight[1], rookWeight[0], bishopWeight[1], queenWeight[0], knightWeight[1], kingWeight[0], aloneBonus[1], isolationPenalty[0], backwardPenalty[1], doubledPenalty[0], shieldBonus[1], pawnStorm[0]);
				player4.setWeights(pawnWeight[0], rookWeight[1], bishopWeight[0], queenWeight[1], knightWeight[0], kingWeight[1], aloneBonus[0], isolationPenalty[1], backwardPenalty[0], doubledPenalty[1], shieldBonus[0], pawnStorm[1]);
								
				playablePlayers.add(player1);
				playablePlayers.add(player2);
				
				playablePlayers.add(player3);
				playablePlayers.add(player4);
								
				i += 2;
			}
			
			for (Iterator<Player> iter = playablePlayers.iterator(); iter.hasNext();) {
				Player player = iter.next();
							
				for (int in = 1; in < playablePlayers.size(); ++in) {					
					PlayerMatch match = new PlayerMatch(player, playablePlayers.get(in));
					generation.add(match);
				}
				
				iter.remove();
			}
						
			System.out.println("Generation Size with new generation: " + generation.size());
			Collections.shuffle(generation);
			
			winningGeneration.clear();
			gameCount = 0;
		}
	}
	
	private static void PrintWinningPlayersStatistics() throws IOException {
		/*
		 * File format will be as follows
		 * Win Type (1 => TERMINATION, 2 => BLACK_WIN, 3 => WHITE_WIN)
		 * Pawn Weight
		 * Rook Weight
		 * Bishop Weight
		 * Knight Weight
		 * Queen Weight
		 * King Weight
		 * Rook Alone Bonus
		 * Pawn Isolation Penalty
		 * Pawn Backwards Penalty
		 * Pawn Doubled Penalty
		 * King Shielded Bonus
		 * King Pawn Storm
		 */
		
		FileWriter writer = new FileWriter("stats.txt", true);
		BufferedWriter out = new BufferedWriter(writer);
		
		for (Player player : winningGeneration) {
			out.write(player.getWeights() + "\n\n");
		}
		
		out.close();
		writer.close();
	}
}
