package fr.eni.ecole;

public class BadBoardSizeException extends RuntimeException
{
	private static final String MESSAGE = "Bad board size.";

	public BadBoardSizeException()
	{
		super(MESSAGE);
	}
}
