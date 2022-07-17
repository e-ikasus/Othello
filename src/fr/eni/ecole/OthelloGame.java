package fr.eni.ecole;

import java.util.Scanner;

public class OthelloGame
{
	private static final int IA_LEVEL = 4;

	private static final String PLAYER_PROMPT_LINE = "Ligne: ";
	private static final String PLAYER_PROMPT_COLUMN = "Colonne: ";
	private static final String PLAYER_TURN = "Au tour de %s ...%n";
	private static final String PLAYER_WIN = "Félicitations joueur %s. Vous avez gagné.%n";
	private static final String PLAYER_NO_WINNER = "Désolé. Personne n'a gagné.%n";
	private static final String PLAYER_TURN_PASSED = "Mauvaise position. Le jouer %s passe son tour.%n";
	private static final String PLAYER_TURN_CAN_NOT_PLAY = "Le jouer %s passe son tour car il ne peut pas jouer.%n";
	private static final String ERROR_BAD_ENTRY = "Erreur de saisie. Recommencez.";

	private Piece currentPlayer;

	private final CheckerBoard checkerBoard;

	private final Scanner scanner;

	private final int size;

	private int column;
	private int line;

	/**
	 * Initialise the Othello game.
	 *
	 * @param size Size of the board.
	 */
	public OthelloGame(int size)
	{
		this.size = size;

		// create the board.
		checkerBoard = new CheckerBoard(size);

		// Player with black pieces begin the game.
		currentPlayer = Piece.BLACK;

		// Scanner used to retrieve players choice.
		scanner = new Scanner(System.in);
	}

	/**
	 * Launch the real game. It ends if one user win.
	 *
	 * @param player1 True if the first player is human.
	 * @param player2 True if the second player is human.
	 */

	public void play(boolean player1, boolean player2)
	{
		Piece oneWinner;

		currentPlayer.setHuman(player1);
		currentPlayer.otherPiece().setHuman(player2);

		for (; ; )
		{
			// Display the checkerboard.
			displayBoard();

			// Ask the player to enter his choice.
			toPlayer();

			// Try to put a piece at the desired place.
			if (!checkerBoard.checkAndPlace(currentPlayer, column - 1, line - 1))
				System.out.printf(PLAYER_TURN_PASSED, currentPlayer.toString());

			// Find if there is a winner.
			oneWinner = checkerBoard.isThereAWinner();

			// If the game is over, determine why.
			if (oneWinner != null)
			{
				// Display the checkerboard the last time.
				displayBoard();

				if (oneWinner == Piece.FREE) System.out.printf(PLAYER_NO_WINNER);
				else System.out.printf(PLAYER_WIN, oneWinner.toString());

				break;
			}

			// Turn to the other player.
			currentPlayer = currentPlayer.otherPiece();
		}
	}

	/**
	 * Ask the player to enter his choice.
	 */

	private void toPlayer()
	{
		IAProposal iaProposal;

		// If the current player can't play, inform him and go to the other.
		if (!checkerBoard.canPlay(currentPlayer))
		{
			System.out.printf(PLAYER_TURN_CAN_NOT_PLAY, currentPlayer.toString());
			currentPlayer = currentPlayer.otherPiece();
		}

		// Print the current player.
		System.out.printf(PLAYER_TURN, currentPlayer.toString());

		if (currentPlayer.isHuman())
		{
			// Ask for the line.
			line = askPlayer(PLAYER_PROMPT_LINE);

			// And the column.
			column = askPlayer(PLAYER_PROMPT_COLUMN);
		}
		else
		{
			iaProposal = checkerBoard.iaTurn(currentPlayer, IA_LEVEL);

			line = iaProposal.getLine() + 1;
			column = iaProposal.getColumn() + 1;

			System.out.printf(PLAYER_PROMPT_LINE + "%d%n", line);

			System.out.printf(PLAYER_PROMPT_COLUMN + "%d%n", column);
		}
	}

	/**
	 * Ask the player to enter a number. This number must be between 0 and the
	 * size of the board minus one.
	 *
	 * @param msg Message to display to the player
	 *
	 * @return Number entered by the player
	 */

	private int askPlayer(String msg)
	{
		int result;

		// Ask the player until he enters a valid number.
		for (; ; )
		{
			// Display a message.
			System.out.print(msg);

			try
			{
				// Wait for a number.
				result = scanner.nextInt();

				// If the number is correct, then return it.
				if ((result >= 1) && (result <= size)) return result;
			}
			catch (Exception e)
			{
				// Something goes wrong.
				System.out.println(ERROR_BAD_ENTRY);
			}
			finally
			{
				// Empty the scanner buffer to wait for another response.
				scanner.nextLine();
			}
		}
	}

	/**
	 * Display the checkerboard with the players score.
	 */

	private void displayBoard()
	{
		// Clear the console (not works sometime).
		System.out.print("\033[H\033[2J");

		// Display scores.
		System.out.printf("%d %s%n", currentPlayer.getNumber(), currentPlayer);
		System.out.printf("%d %s%n", currentPlayer.otherPiece().getNumber(), currentPlayer.otherPiece());

		// And the board.
		checkerBoard.display();
	}
}
