
public class ChessLearning {
	public static void main(String args[]) {
			System.out.println("New game simulation has been started.");
		
			ChessBoard.Initialize();
			ChessGame.InitializeGame();
		
			while (!ChessGame.gameOver) {
				ChessGame.UpdateGame();
			}
		
		System.out.println("Simulation has completed: " + ChessGame.gameOver);
	}
}
