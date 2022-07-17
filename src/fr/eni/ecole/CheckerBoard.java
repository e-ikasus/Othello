package fr.eni.ecole;

import javafx.collections.transformation.SortedList;

import java.util.ArrayList;

public class CheckerBoard
{
	// Size of the board. This is the width and the height.
	private final int boardSize;

	// Board itself.
	private Piece[][] content;

	/**
	 * Create a checkerboard. Create a checkerboard with same width and height
	 * supplied in parameter. The board is initialised with all cases as empty
	 * except the four cases at the center which contains predefined pieces.
	 *
	 * @param size Size of the board.
	 */

	public CheckerBoard(int size)
	{
		// Check if the size is valid (not null and even).
		if ((size == 0) || ((size % 2) == 1)) throw new BadBoardSizeException();

		// Size of the board.
		boardSize = size;

		// The board itself.
		content = new Piece[boardSize][boardSize];

		// All the cases are empty.
		for (int i = 0; i < boardSize; i++)
			for (int j = 0; j < boardSize; j++)
				content[j][i] = Piece.FREE;

		// But at the center, there are four pieces.
		int x = boardSize / 2 - 1;
		int y = boardSize / 2 - 1;

		content[y][x] = Piece.WHITE;
		content[y][x + 1] = Piece.BLACK;
		content[y + 1][x] = Piece.BLACK;
		content[y + 1][x + 1] = Piece.WHITE;

		Piece.WHITE.win(2);
		Piece.BLACK.win(2);
	}

	/**
	 * Determine if there is a winner. If it is not possible to place another
	 * piece in the board, then the winner is the one who has the highest pieces
	 * in the board. In the other case, the winner is the one who always have
	 * pieces when the other doesn't.
	 *
	 * @return BLACK or WHITE for a winner, FREE if none exist or null if the game
	 * is not complete.
	 */

	public Piece isThereAWinner()
	{
		Piece result = null;

		if ( (!canPlay(Piece.BLACK)) && (!canPlay(Piece.WHITE)) )
		{
			// If it is not possible to play one time more, then find the player that has
			// the highest number of pieces.
			if (Piece.WHITE.getNumber() > Piece.BLACK.getNumber())
				result = Piece.WHITE;
			else if (Piece.BLACK.getNumber() > Piece.WHITE.getNumber())
				result = Piece.BLACK;
			else result = Piece.FREE;
		}
		else
		{
			// Determine if there is a winner.
			if (Piece.WHITE.getNumber() == 0) result = Piece.BLACK;
			else if (Piece.BLACK.getNumber() == 0) result = Piece.WHITE;
		}

		// Return the winner if one exist.
		return result;
	}

	/**
	 * Determine if a player can play.
	 *
	 * @return True if the supplied player can play, false otherwise.
	 */

	public boolean canPlay(Piece piece)
	{
		// Determine if the supplied player can play.
		for (int i = 0; i < boardSize; i++)
			for (int j = 0; j < boardSize; j++)
				if (checkCase(piece, i, j, true) != 0) return true;

		// Player can't play anymore.
		return false;
	}

	/**
	 * Find the best possible place to put a piece.
	 *
	 * @return The best place to win.
	 */

	public IAProposal iaTurn(Piece piece, int level)
	{
		Piece[][] backup;
		int blackPieces;
		int whitePieces;
		IAProposal result;

		ArrayList<IAProposal> possiblePlaces = new ArrayList<>();

		IAProposal other;
		int number;

		// For each line of the board.
		for (int j = 0; j < boardSize; j++)
		{
			// For each column of the board.
			for (int i = 0; i < boardSize; i++)
			{
				// Determine if it is possible to put a piece here.
				if ((number = checkCase(piece, i, j, true)) != 0)
				{
					// Backup the board, as we will simulate the other player.
					backup = duplicate();
					blackPieces = Piece.BLACK.getNumber();
					whitePieces = Piece.WHITE.getNumber();

					// Now, really place the piece.
					checkAndPlace(piece, i, j);

					// Search the maximum pieces the adverse will take.
					other = (level != 0) ? (iaTurn(piece.otherPiece(), level - 1)) : (null);

					// And create a proposal which contains a number indicating how many
					// pieces are won or loosen if the IA play here depending on its sign.
					possiblePlaces.add(new IAProposal(number - ((other == null) ? (0) : (other.getWonPieces())), i, j));

					// Now, restore the board.
					content = backup;
					Piece.BLACK.setNumber(blackPieces);
					Piece.WHITE.setNumber(whitePieces);
				}
			}
		}

		// If there was no place found.
		if (possiblePlaces.size() == 0) return null;

		// Start with the first proposal.
		result = possiblePlaces.get(0);

		// And search now for the best score.
		for (IAProposal cur : possiblePlaces) if (cur.getWonPieces() >= result.getWonPieces()) result = cur;

		// Return the result.
		return result;
	}

	/**
	 * Put a piece on the checkerboard. Try to put a piece in the checkerboard. If
	 * this succeed, then the numbers of pieces owned by players are updated.
	 *
	 * @param piece  Piece to put.
	 * @param column Column where to put that piece, beginning at zero.
	 * @param line   Line where to put that piece, beginning at zero.
	 *
	 * @return True if the piece was placed, false otherwise.
	 */

	public boolean checkAndPlace(Piece piece, int column, int line)
	{
		// Determine if it is possible to put the specified piece at that place.
		int foundCases = checkCase(piece, column, line, false);

		// If this is possible.
		if (foundCases != 0)
		{
			// Put the piece at the specified place.
			content[line][column] = piece;

			// One player win pieces.
			piece.win(foundCases);
			// As the other lost them.
			piece.otherPiece().lose(foundCases - 1);
		}

		// return the success of the operation.
		return (foundCases != 0);
	}

	/**
	 * Display the checkerboard.
	 */

	public void display()
	{
		int i, j;

		// Determine the size of the left margin.
		String left = (boardSize >= 10) ? ("%02d ") : ("%d ");

		// If not only units have to be drawn.
		if (boardSize >= 10)
		{
			// left margin.
			System.out.print("   ");

			// display the tens.
			for (i = 1; i <= boardSize; i++) System.out.printf("%d ", i / 10);

			// Go to the next line including the additionnel space.
			System.out.printf("%n ");
		}

		// Left margin.
		System.out.print("  ");

		// Display the units.
		for (i = 1; i <= boardSize; i++) System.out.printf("%d ", i % 10);

		// And go to the next line.
		System.out.println("");

		// For each line of the checkerboard.
		for (j = 0; j < boardSize; j++)
		{
			// number of the line.
			System.out.printf(left, j + 1);

			// Display the entire line
			for (i = 0; i < boardSize; i++)
				System.out.printf("%s ", content[j][i].toString());

			// Go to the next line.
			System.out.println("");
		}
	}

	/**
	 * Duplicate the board.
	 *
	 * @return A clone of the board.
	 */

	private Piece[][] duplicate()
	{
		Piece[][] copy = new Piece[boardSize][boardSize];

		for (int j = 0; j < boardSize; j++)
			for (int i = 0; i < boardSize; i++)
				copy[j][i] = content[j][i];

		return copy;
	}

	/**
	 * Check a place for putting a piece. Check if it is possible to put the
	 * supplied piece at the specified case and return the number of pieces won by
	 * the owner of the supplied piece.
	 *
	 * @param piece  Piece to put.
	 * @param column Column where to put that piece, beginning at zero.
	 * @param line   Line where to put that piece, beginning at zero.
	 *
	 * @return Number of pieces won, 0 otherwise.
	 */

	private int checkCase(Piece piece, int column, int line, boolean onlyCheck)
	{
		int result = 0;

		// Search to the right direction.
		result += checkDirection(piece, column, line, +1, +0, onlyCheck);
		// Search to the top right direction.
		result += checkDirection(piece, column, line, +1, -1, onlyCheck);
		// Search to the top direction.
		result += checkDirection(piece, column, line, +0, -1, onlyCheck);
		// Search to the top left direction.
		result += checkDirection(piece, column, line, -1, -1, onlyCheck);
		// Search to the left direction.
		result += checkDirection(piece, column, line, -1, +0, onlyCheck);
		// Search to the bottom left direction.
		result += checkDirection(piece, column, line, -1, +1, onlyCheck);
		// Search to the bottom direction.
		result += checkDirection(piece, column, line, +0, +1, onlyCheck);
		// Search to the bottom right direction.
		result += checkDirection(piece, column, line, +1, +1, onlyCheck);

		return (result == 0) ? (0) : (result + 1);
	}

	/**
	 * Check a place for putting a piece. Check into a specific direction if it is
	 * possible to put the piece at the specified place defined by column and
	 * line. The direction is determined by the xStep and yStep parameters. If it
	 * is possible to put that piece, the returned value is the number of pieces
	 * won by the owner of that piece.
	 *
	 * @param piece     Piece to put.
	 * @param column    Column where to put that piece, beginning at zero.
	 * @param line      Line where to put that piece, beginning at zero.
	 * @param xStep     Horizontal direction.
	 * @param yStep     Vertical direction.
	 * @param onlyCheck Wether or not invert the opposite pieces.
	 *
	 * @return Number of pieces won by the owner of the supplied piece, zero
	 * otherwise.
	 */
	private int checkDirection(Piece piece, int column, int line, int xStep, int yStep, boolean onlyCheck)
	{
		int result = 0;
		boolean outSide;

		// It is only possible to put a new piece in a free case.
		if (content[line][column] != Piece.FREE) return result;

		for (; ; )
		{
			// Go to the next place to analyse.
			column += xStep;
			line += yStep;

			// First check if we are still in the checkerboard (not outside).
			outSide = (!((column >= 0) && (column < boardSize) && (line >= 0) && (line < boardSize)));

			// If we are really outside, stop the scanning.
			if (outSide) break;

			// We only stop if we meet the same piece or a free case.
			if ((content[line][column] == piece) || (content[line][column] == Piece.FREE))
				break;

			// The owner of the supplied piece win one from the opposite.
			result++;
		}

		// If we finished outside the board or on a different piece (other or free),
		// then the supplied place is invalid.
		if ((outSide) || (content[line][column] != piece)) return 0;

		// If this was not only a check, revert the pieces found until the supplied
		// place excluded.
		if (!onlyCheck)
			for (int i = 0; i < result; i++)
			{
				// Go backward.
				column -= xStep;
				line -= yStep;

				// Change the piece.
				content[line][column] = piece;
			}

		// Return the number ot pieces found.
		return result;
	}
}
