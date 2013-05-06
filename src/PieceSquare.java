
public class PieceSquare {
	//as of 5/6/13, values taken from http://chessprogramming.wikispaces.com/CPW-Engine_eval_init
	//Hopefully will soon be determined by learning
	//Piece square tables are tables that assign a value to each possible position of each type of piece;
	//Used in evaluation.
	
	//incremented as moves are made and unmade to improve efficiency:
	public static int blackScore = -95; 
	public static int whiteScore = -95;
	
	
	//white piece square tables are simply "accessed" by referencing the black PS table
	//and replacing the row number with 7 - row
	
	public static int[][] blackPawn =  {{0,  0,  0,  0,  0,  0,  0,  0}, //i made this up myself
		{20, 20, 20, 20, 20, 20, 20, 20},
		{4, 4, 8, 12, 12, 8, 4, 4},
		 {2,  2, 4, 10, 10, 4,  2,  2},
		 {0,  0,  0, 8, 8,  0,  0,  0},
		 {2, -2,-4,  0,  0,-4, -2,  2},
		 {2, 4, 4,-8,-8, 4, 4,  2}, 
		 {0,  0,  0,  0,  0,  0,  0,  0}};
	public static int[][] blackBishop = {{-8,  -8,  -8,  -8,  -8,  -8,  -8,  -8},
	     {-8,   0,   0,   0,   0,   0,   0,  -8},
	     {-8,   0,   4,   4,   4,   4,   0,  -8},
	     {-8,   0,   4,   8,   8,   4,   0,  -8},
	     {-8,   0,   4,   8,   8,   4,   0,  -8},
	     {-8,   0,   4,   4,   4,   4,   0,  -8},
	     {-8,   0,   1,   2,   2,   1,   0,  -8},
	     {-8,  -12, -8,  -8,  -8,  -8, -12,  -8}};
	
	public static int[][] blackKnight = {{-8,  -8,  -8,  -8,  -8,  -8,  -8,  -8},
	     {-8,   0,   0,   0,   0,   0,   0,  -8},
	     {-8,   0,   4,   4,   4,   4,   0,  -8},
	     {-8,   0,   4,   8,   8,   4,   0,  -8},
	     {-8,   0,   4,   8,   8,   4,   0,  -8},
	     {-8,   0,   4,   4,   4,   4,   0,  -8},
	     {-8,   0,   1,   2,   2,   1,   0,  -8},
	     {-8,  -12, -8,  -8,  -8,  -8, -12,  -8}};
	
	public static int[][] blackRook =   {{5,   5,   5,   5,   5,   5,   5,   5},
	     {20,  20,  20,  20,  20,  20,  20,  20},
	     {-5,   0,   0,   0,   0,   0,   0,  -5},
	     {-5,   0,   0,   0,   0,   0,   0,  -5},
	     {-5,   0,   0,   0,   0,   0,   0,  -5},
	     {-5,   0,   0,   0,   0,   0,   0,  -5},
	     {-5,   0,   0,   0,   0,   0,   0,  -5},
	     { 0,   0,   0,   2,   2,   0,   0,   0}};
	
	public static int[][] blackQueen = {{0,   0,   0,   0,   0,   0,   0,   0},
	      {0,   0,   1,   1,   1,   1,   0,   0},
	      {0,   0,   1,   2,   2,   1,   0,   0},
	      {0,   0,   2,   3,   3,   2,   0,   0},
	      {0,   0,   2,   3,   3,   2,   0,   0},
	      {0,   0,   1,   2,   2,   1,   0,   0},
	      {0,   0,   1,   1,   1,   1,   0,   0},
	     {-5,  -5,  -5,  -5,  -5,  -5,  -5,  -5}};
	
	public static int[][] blackKingMiddle = {{-40, -40, -40, -40, -40, -40, -40, -40},
	    {-40, -40, -40, -40, -40, -40, -40, -40},
	    {-40, -40, -40, -40, -40, -40, -40, -40},
	    {-40, -40, -40, -40, -40, -40, -40, -40},
	    {-40, -40, -40, -40, -40, -40, -40, -40},
	    {-40, -40, -40, -40, -40, -40, -40, -40},
	    {-15, -15, -20, -20, -20, -20, -15, -15},
	      {0,  20,  30, -30,   0, -20,  30,  20}};
	
	public static int[][] blackKingEnd = {{0,  10,  20,  30,  30,  20,  10,   0},
		     {10,  20,  30,  40,  40,  30,  20,  10},
		     {20,  30,  40,  50,  50,  40,  30,  20},
		     {30,  40,  50,  60,  60,  50,  40,  30},
		     {30,  40,  50,  60,  60,  50,  40,  30},
		     {20,  30,  40,  50,  50,  40,  30,  20},
		     {10,  20,  30,  40,  40,  30,  20,  10},
		      {0,  10,  20,  30,  30,  20,  10,   0}};

}
