package game;

import java.util.Scanner;

/* A 2D Array representation of the Checkers board game.
 */
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

	public static void main(String[] args) {
		Checkers game = new Checkers();
//		// while (!game.gameOver()) {
//		game.makeMove("B3", "C4");
//		game.makeMove("E6", "D5");
//		game.setRoyal(3, 2);
//		game.makeMove("C4", "E6");
//		game.makeMove("C6", "D5");
//		game.makeMove("E6", "C4");
//		game.printBoard();
//		game.makeMove("D5", "B3");
//		game.printBoard();
//		// }
//
//		System.out.println(game.getWinner() + " is the winner.");
		game.start();
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

//		System.out.println("Next: " + whosMove + "'s move.");
		System.out.println();
	}

	// Starts the interactive game
	public void start() {
		Scanner scan = new Scanner(System.in);
		String moveFrom;
		String moveTo;
		while (!gameOver()) {
			// Ask for moves from user
			System.out.print("Enter the position you want to move from " + (whosMove == 'b' ? "(Black): " : "(Red): "));
			moveFrom = scan.next();
			System.out.print("Enter the position you want to move to " + (whosMove == 'b' ? "(Black): " : "(Red): "));
			moveTo = scan.next();
			System.out.println();

			// Play the move given from the user
			if (makeMove(moveFrom, moveTo)) {
				printBoard();
				while (canMoveAgain(getX(moveTo), getY(moveTo))) {
					System.out.print("You can move again, do you want to? (yes/no): ");
					if (scan.next().equalsIgnoreCase("yes")) {
						System.out.print("Enter the position you want to move to "
								+ (whosMove == 'b' ? "(Black): " : "(Red): "));
						moveFrom = scan.next();
						System.out.println();
						if (makeMoveSkipOnly(moveTo, moveFrom))
							printBoard();
						else
							System.out.println("Invalid move, please try again (you may only skip opponent's pieces).");
					} else {
						break;
					}
				}

				// Change whose move is next
				if (whosMove == 'b')
					whosMove = 'r';
				else
					whosMove = 'b';

				// If an invalid move, ask the user to try again
			} else {
				System.out.println("Invalid move, please try again.");
			}
		}

		scan.close();

		System.out.println(getWinner() + " is the winner.");
	}

	// Performs the player's inputed move. Changes the board if its a valid move.
	// Otherwise, does nothing.
	public boolean makeMove(String from, String moveTo) {
		int fromX = getX(from);
		int fromY = getY(from);
		int toX = getX(moveTo);
		int toY = getY(moveTo);

		if (isMoveValid(fromX, fromY, toX, toY)) {
			board[toX][toY] = board[fromX][fromY];
			board[fromX][fromY] = emptySpace(fromX, fromY);
			if (((board[fromX][fromY] == 'b' && toX == 0) || (board[fromX][fromY] == 'r' && toX == SIZE - 1))) {
				setRoyal(toX, toY);
			}

			return true;
		}

		return false;
	}

	private boolean makeMoveSkipOnly(String from, String moveTo) {
		int fromX = getX(from);
		int fromY = getY(from);
		int toX = getX(moveTo);
		int toY = getY(moveTo);

		if (isMoveValidSkipOnly(fromX, fromY, toX, toY)) {
			board[toX][toY] = board[fromX][fromY];
			board[fromX][fromY] = emptySpace(fromX, fromY);
			if (((board[fromX][fromY] == 'b' && toX == 0) || (board[fromX][fromY] == 'r' && toX == SIZE - 1))) {
				setRoyal(toX, toY);
			}

			return true;
		}

		return false;
	}

	// Checks if a move is valid
	private boolean isMoveValid(int fromX, int fromY, int toX, int toY) {
		boolean result = false;

		// If valid index positions - within bounds
		if (fromX < SIZE && toX < SIZE && fromY < SIZE && fromX < SIZE && fromX >= 0 && toX >= 0 && fromY >= 0
				&& fromX >= 0) {
			// If blacks move
			if (whosMove == 'b') {
				// If a normal piece
				if (board[fromX][fromY] == 'b') {
					// One square diagonal movement
					if ((Math.abs(fromY - toY) == 1) && (toX - fromX == 1) && board[toX][toY] == emptySpace(toX, toY)) {
						result = true;
					}

					// If skipping spaces over opponent's piece
					if ((Math.abs(fromY - toY) == 2) && (toX - fromX == 2)
							&& (board[toX - 1][(toY + fromY) / 2] == 'r' || board[toX - 1][(toY + fromY) / 2] == 'R')
							&& board[toX][toY] == emptySpace(toX, toY)) {
						board[toX - 1][(toY + fromY) / 2] = emptySpace(toX - 1, (toY + fromY) / 2);
						redPieces--;
						result = true;
					}
				}

				// If a king piece
				if (board[fromX][fromY] == 'B') {
					// One square diagonal movement (both up and down)
					if ((Math.abs(fromY - toY) == 1) && (Math.abs(toX - fromX) == 1)
							&& board[toX][toY] == emptySpace(toX, toY)) {
						result = true;
					}

					// If skipping spaces over opponent's piece (both up and down)
					if ((Math.abs(fromY - toY) == 2) && (Math.abs(toX - fromX) == 2)
							&& (board[(toX + fromX) / 2][(toY + fromY) / 2] == 'r'
									|| board[(toX + fromX) / 2][(toY + fromY) / 2] == 'R')
							&& board[toX][toY] == emptySpace(toX, toY)) {
						board[(toX + fromX) / 2][(toY + fromY) / 2] = emptySpace((toX + fromX) / 2, (toY + fromY) / 2);
						redPieces--;
						result = true;
					}
				}

				// If reds move
			} else {
				// If a normal piece
				if (board[fromX][fromY] == 'r') {
					// One square diagonal movement
					if ((Math.abs(fromY - toY) == 1) && (fromX - toX == 1) && board[toX][toY] == emptySpace(toX, toY)) {
						result = true;
					}

					// If skipping spaces over opponent's piece
					if ((Math.abs(fromY - toY) == 2) && (fromX - toX == 2)
							&& (board[toX + 1][(toY + fromY) / 2] == 'b' || board[toX + 1][(toY + fromY) / 2] == 'B')
							&& board[toX][toY] == emptySpace(toX, toY)) {
						board[toX + 1][(toY + fromY) / 2] = emptySpace(toX + 1, (toY + fromY) / 2);
						blackPieces--;
						result = true;
					}
				}

				// If a king piece
				if (board[fromX][fromY] == 'R') {
					// One square diagonal movement (both up and down)
					if ((Math.abs(fromY - toY) == 1) && (Math.abs(toX - fromX) == 1)
							&& board[toX][toY] == emptySpace(toX, toY)) {
						result = true;
					}

					// If skipping spaces over opponent's piece (both up and down)
					if ((Math.abs(fromY - toY) == 2) && (Math.abs(toX - fromX) == 2)
							&& (board[(toX + fromX) / 2][(toY + fromY) / 2] == 'b'
									|| board[(toX + fromX) / 2][(toY + fromY) / 2] == 'B')
							&& board[toX][toY] == emptySpace(toX, toY)) {
						board[(toX + fromX) / 2][(toY + fromY) / 2] = emptySpace((toX + fromX) / 2, (toY + fromY) / 2);
						blackPieces--;
						result = true;
					}
				}
			}
		}

		return result;
	}

	private boolean isMoveValidSkipOnly(int fromX, int fromY, int toX, int toY) {
		boolean result = false;

		// If valid index positions - within bounds
		if (fromX < SIZE && toX < SIZE && fromY < SIZE && fromX < SIZE && fromX >= 0 && toX >= 0 && fromY >= 0
				&& fromX >= 0) {
			// If blacks move
			if (whosMove == 'b') {
				// If a normal piece
				if (board[fromX][fromY] == 'b') {
					// If skipping spaces over opponent's piece
					if ((Math.abs(fromY - toY) == 2) && (toX - fromX == 2)
							&& (board[toX - 1][(toY + fromY) / 2] == 'r' || board[toX - 1][(toY + fromY) / 2] == 'R')
							&& board[toX][toY] == emptySpace(toX, toY)) {
						board[toX - 1][(toY + fromY) / 2] = emptySpace(toX - 1, (toY + fromY) / 2);
						redPieces--;
						result = true;
					}
				}

				// If a king piece
				if (board[fromX][fromY] == 'B') {
					// If skipping spaces over opponent's piece (both up and down)
					if ((Math.abs(fromY - toY) == 2) && (Math.abs(toX - fromX) == 2)
							&& (board[(toX + fromX) / 2][(toY + fromY) / 2] == 'r'
									|| board[(toX + fromX) / 2][(toY + fromY) / 2] == 'R')
							&& board[toX][toY] == emptySpace(toX, toY)) {
						board[(toX + fromX) / 2][(toY + fromY) / 2] = emptySpace((toX + fromX) / 2, (toY + fromY) / 2);
						redPieces--;
						result = true;
					}
				}

				// If reds move
			} else {
				// If a normal piece
				if (board[fromX][fromY] == 'r') {
					// If skipping spaces over opponent's piece
					if ((Math.abs(fromY - toY) == 2) && (fromX - toX == 2)
							&& (board[toX + 1][(toY + fromY) / 2] == 'b' || board[toX + 1][(toY + fromY) / 2] == 'B')
							&& board[toX][toY] == emptySpace(toX, toY)) {
						board[toX + 1][(toY + fromY) / 2] = emptySpace(toX + 1, (toY + fromY) / 2);
						blackPieces--;
						result = true;
					}
				}

				// If a king piece
				if (board[fromX][fromY] == 'R') {
					// If skipping spaces over opponent's piece (both up and down)
					if ((Math.abs(fromY - toY) == 2) && (Math.abs(toX - fromX) == 2)
							&& (board[(toX + fromX) / 2][(toY + fromY) / 2] == 'b'
									|| board[(toX + fromX) / 2][(toY + fromY) / 2] == 'B')
							&& board[toX][toY] == emptySpace(toX, toY)) {
						board[(toX + fromX) / 2][(toY + fromY) / 2] = emptySpace((toX + fromX) / 2, (toY + fromY) / 2);
						blackPieces--;
						result = true;
					}
				}
			}
		}

		return result;
	}

	private boolean canMoveAgain(int x, int y) {
		boolean result = false;

		// diagonals: x-1:y-1, x-1:y+1, x+1:y-1, x+1:y+1

		if (board[x][y] == 'b') {
			if (x + 2 < SIZE && y - 2 < SIZE && x + 2 >= 0 && y - 2 >= 0
					&& (board[x + 1][y - 1] == 'r' || board[x + 1][y - 1] == 'R')
					&& board[x + 2][y - 2] == emptySpace(x + 2, y - 2)) {
				result = true;
			}

			if (x + 2 < SIZE && y + 2 < SIZE && x + 2 >= 0 && y + 2 >= 0
					&& (board[x + 1][y + 1] == 'r' || board[x + 1][y + 1] == 'R')
					&& board[x + 2][y + 2] == emptySpace(x + 2, y + 2)) {
				result = true;
			}

		} else if (board[x][y] == 'r') {
			if (x - 2 < SIZE && y - 2 < SIZE && x - 2 >= 0 && y - 2 >= 0
					&& (board[x - 1][y - 1] == 'b' || board[x - 1][y - 1] == 'B')
					&& board[x - 2][y - 2] == emptySpace(x - 2, y - 2)) {
				result = true;
			}

			if (x - 2 < SIZE && y + 2 < SIZE && x - 2 >= 0 && y + 2 >= 0
					&& (board[x - 1][y + 1] == 'b' || board[x - 1][y + 1] == 'B')
					&& board[x - 2][y + 2] == emptySpace(x - 2, y + 2)) {
				result = true;
			}

		} else if (board[x][y] == 'B') {
			if (x + 2 < SIZE && y - 2 < SIZE && x + 2 >= 0 && y - 2 >= 0
					&& (board[x + 1][y - 1] == 'r' || board[x + 1][y - 1] == 'R')
					&& board[x + 2][y - 2] == emptySpace(x + 2, y - 2)) {
				result = true;
			}

			if (x + 2 < SIZE && y + 2 < SIZE && x + 2 >= 0 && y + 2 >= 0
					&& (board[x + 1][y + 1] == 'r' || board[x + 1][y + 1] == 'R')
					&& board[x + 2][y + 2] == emptySpace(x + 2, y + 2)) {
				result = true;
			}

			if (x - 2 < SIZE && y - 2 < SIZE && x - 2 >= 0 && y - 2 >= 0
					&& (board[x - 1][y - 1] == 'r' || board[x - 1][y - 1] == 'R')
					&& board[x - 2][y - 2] == emptySpace(x - 2, y - 2)) {
				result = true;
			}

			if (x - 2 < SIZE && y + 2 < SIZE && x - 2 >= 0 && y + 2 >= 0
					&& (board[x - 1][y + 1] == 'r' || board[x - 1][y + 1] == 'R')
					&& board[x - 2][y + 2] == emptySpace(x - 2, y + 2)) {
				result = true;
			}
		} else { // if 'R'
			if (x + 2 < SIZE && y - 2 < SIZE && x + 2 >= 0 && y - 2 >= 0
					&& (board[x + 1][y - 1] == 'b' || board[x + 1][y - 1] == 'B')
					&& board[x + 2][y - 2] == emptySpace(x + 2, y - 2)) {
				result = true;
			}

			if (x + 2 < SIZE && y + 2 < SIZE && x + 2 >= 0 && y + 2 >= 0
					&& (board[x + 1][y + 1] == 'b' || board[x + 1][y + 1] == 'B')
					&& board[x + 2][y + 2] == emptySpace(x + 2, y + 2)) {
				result = true;
			}

			if (x - 2 < SIZE && y - 2 < SIZE && x - 2 >= 0 && y - 2 >= 0
					&& (board[x - 1][y - 1] == 'b' || board[x - 1][y - 1] == 'B')
					&& board[x - 2][y - 2] == emptySpace(x - 2, y - 2)) {
				result = true;
			}

			if (x - 2 < SIZE && y + 2 < SIZE && x - 2 >= 0 && y + 2 >= 0
					&& (board[x - 1][y + 1] == 'b' || board[x - 1][y + 1] == 'B')
					&& board[x - 2][y + 2] == emptySpace(x - 2, y + 2)) {
				result = true;
			}
		}

		return result;
	}

	// Sets a position to a "royal", ie. a king piece that can now move both
	// forwards and backwards
	private void setRoyal(int x, int y) {
		if (board[x][y] == 'b')
			board[x][y] = 'B';
		else
			board[x][y] = 'R';
	}

	// Checks if a side has won
	private boolean gameOver() {
		return (redPieces == 0) || (blackPieces == 0);
	}

	// Returns a String representing the winner of the game
	private String getWinner() {
		return redPieces == 0 ? "Black" : "Red";
	}

	// Returns the appropriate empty space value
	private char emptySpace(int x, int y) {
		if (x % 2 == 0 && y % 2 != 0)
			return 49;
		else if (x % 2 != 0 && y % 2 == 0)
			return 49;
		else
			return 48;
	}

	// Helper method that returns the int index value of the row of the position
	// given.
	private int getX(String position) {
		return position.charAt(1) - 49;
	}

	// Helper method that returns the int index value of the column of the
	// position given.
	private int getY(String position) {
		if (Character.isUpperCase(position.charAt(1)))
			return position.charAt(0) - 65;
		else
			return position.charAt(0) - 97;
	}
}
