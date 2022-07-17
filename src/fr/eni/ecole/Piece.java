package fr.eni.ecole;

public enum Piece
{
	FREE('.'), WHITE('O'), BLACK('X');

	private static final char FREE_SYMBOL = '.';
	private static final char WHITE_SYMBOL = 'O';
	private static final char BLACK_SYMBOL = 'X';

	// Number of symbols in the checkerboard.
	private int number;

	// Name of this symbol.
	private final char symbol;

	// Type of player (human or IA)
	private boolean isHuman = true;

	/**
	 * @return
	 */

	public boolean isHuman()
	{
		return isHuman;
	}

	/**
	 * @param human
	 */

	public void setHuman(boolean human)
	{
		isHuman = human;
	}

	/**
	 * Get the symbol that represents the piece.
	 *
	 * @return Symbol
	 */

	public char getSymbol()
	{
		return symbol;
	}

	/**
	 * Get the symbol that represents the piece.
	 *
	 * @return Symbol in a form of a string.
	 */

	@Override
	public String toString()
	{
		return String.valueOf(symbol);
	}

	/**
	 * create a piece with a default symbol.
	 *
	 * @param symbol Symbol representing the piece.
	 */

	Piece(char symbol)
	{
		this.symbol = symbol;
	}

	/**
	 * Get the number of that particular piece in the checkerboard.
	 *
	 * @return Number of pieces.
	 */

	public int getNumber()
	{
		return number;
	}

	/**
	 * Set the number of that particular piece in the checkerboard.
	 *
	 * @param number Number of pieces.
	 */

	public void setNumber(int number)
	{
		this.number = number;
	}

	/**
	 * Increase the number of that particular piece in the checkerboard.
	 *
	 * @param nbr Number to add.
	 */

	public void win(int nbr)
	{
		number += nbr;
	}

	/**
	 * Decrease the number of that particular piece in the checkerboard.
	 *
	 * @param nbr Number to subtract.
	 */

	public void lose(int nbr)
	{
		number -= nbr;
	}

	/**
	 * Return the opposite piece of this instance.
	 *
	 * @return Opposite instance.
	 */

	public Piece otherPiece()
	{
		return (this == WHITE) ? (Piece.BLACK) : ((this == BLACK) ? (Piece.WHITE) : (Piece.FREE));
	}
}
