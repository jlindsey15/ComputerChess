
public class PlayerMatch {
	private Player player1;
	private Player player2;
	
	private Player winner;
	private Player loser;
	
	public boolean hasEnded = false;
	
	public PlayerMatch(Player play1, Player play2) {
		this.player1 = play1;
		this.player2 = play2;
		
		this.winner = null;
		this.loser = null;
	}
	
	public void EndGame(Player winner, Player loser) {
		this.winner = winner;
		this.loser = loser;
		
		System.out.println("End Game: " + this);
		
		this.winner.incrementGamesWon();
		this.hasEnded = true;
				
		this.player1 = null;
		this.player2 = null;
	}
	
	public Player getWinner() {
		return this.winner;
	}
	
	public Player getLoser() {
		return this.loser;
	}
	
	public Player getPlayer1() {
		return player1;
	}
	
	public Player getPlayer2() {
		return player2;
	}

	public void SetPlayers(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
	}
}
