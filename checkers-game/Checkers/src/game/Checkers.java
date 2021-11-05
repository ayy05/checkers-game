package game;

public class Checkers {

	private char[][] board;
	private final int SIZE = 8;
	private char whosMove;
	private int redPieces;
	private int blackPieces;

	// Constructor for the Checkers class
	// Creates an empty board
	public Checkers() {
		board = new char[SIZE][SIZE];
		whosMove = 'b';
		redPieces = 12;
		blackPieces = 12;

		// Sets the base board
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++)
				if (i % 2 == 0 && j % 2 != 0)
					board[i][j] = 49;
				else if (i % 2 != 0 && j % 2 == 0)
					board[i][j] = 49;
				else
					board[i][j] = 48;
		}

		// Sets starting white player positions
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (board[i][j] == 49)
					board[i][j] = 98;
			}
		}

		// Sets starting red player positions
		for (int i = 5; i < 8; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (board[i][j] == 49)
					board[i][j] = 114;
			}
		}

		printBoard();
	}

	// Prints the current board status
	public void printBoard() {
		System.out.println("    A B C D E F G H");
		System.out.println("    ---------------");
		for (int i = 0; i < SIZE; i++) {
			System.out.print((i + 1) + " | ");
			for (int j = 0; j < SIZE; j++)
				System.out.print(board[i][j] + " ");
			System.out.println();
		}

		System.out.println("Next: " + whosMove + "'s move.");
		System.out.println();
	}

	public void getMove(String from, String moveTo) {

	}

	public void makeMove(String from, String moveTo) {
		int fromX = getX(from);
		int fromY = getY(from);
		int toX = getX(moveTo);
		int toY = getY(moveTo);

		if (isMoveValid(from, moveTo)) {
			board[toX][toY] = board[fromX][fromY];
			board[fromX][fromY] = emptySpace(fromX, fromY);

			// Change whose move is next
			if (whosMove == 'b')
				whosMove = 'r';
			else
				whosMove = 'b';
		}

		// Check if canMoveAgain()

		// Print board
		printBoard();
	}

	// Helper method that returns the int index value of the row of the position
	// given.
	private int getX(String position) {
		return position.charAt(1) - 49;
	}

	// Helper method that returns the int index value of the column of the
	// position given.
	private int getY(String position) {
		return position.charAt(0) - 65;
	}

	// Checks if a move is valid
	// Only valid if the piece is moved from an occupied space to an empty space.
	private boolean isMoveValid(String from, String moveTo) {
		int fromX = getX(from);
		int fromY = getY(from);
		int toX = getX(moveTo);
		int toY = getY(moveTo);

		// If valid index positions - within bounds
		if (fromX < board.length && toX < board.length && fromY < SIZE - 1 && fromX < SIZE - 1) {
			// for regular piece -only if row increases or decreases
			// Checks that row changed by 1
			if (Math.abs(fromY - toY) == 1) {
				if (whosMove == 'b' && board[fromX][fromY] == 'b' && (fromX - toX == 1)) {

				} else if (whosMove == 'r' && board[fromX][fromY] == 'r' && (fromX - toX == -1)) {

				}
			}

			// For royal - doesn't matter

			// If to immediate diagonal space
			// Or If to 2-space diagonal AND immediate diagonal is enemy piece

			// Then run changeToRoyal()
		}

		return (board[toX][toY] == '1' && (board[fromX][fromY] == 'b' || board[fromX][fromY] == 'r'));
	}

//	private int doesSkipSpace() {
//
//	}

	private boolean gameOver() {
		return (redPieces == 0) || (blackPieces == 0);
	}

	private String getWinner() {
		return redPieces == 0 ? "Black" : "Red";
	}

	private char emptySpace(int x, int y) {
		if (x % 2 == 0 && y % 2 != 0)
			return 49;
		else if (x % 2 != 0 && y % 2 == 0)
			return 49;
		else
			return 48;
	}

	public static void main(String[] args) {
		Checkers game = new Checkers();
		while (!game.gameOver()) {
			game.makeMove("B3", "C4");
		}

		System.out.println(game.getWinner() + " is the winner.");
	}

}
