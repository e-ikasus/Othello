package fr.eni.ecole;

public class IAProposal implements Comparable
{
	private int wonPieces;
	private int column;
	private int line;

	public int getWonPieces()
	{
		return wonPieces;
	}

	public void setWonPieces(int wonPieces)
	{
		this.wonPieces = wonPieces;
	}

	public int getColumn()
	{
		return column;
	}

	public void setColumn(int column)
	{
		this.column = column;
	}

	public int getLine()
	{
		return line;
	}

	public void setLine(int line)
	{
		this.line = line;
	}

	public IAProposal(int wonPieces, int column, int line)
	{
		this.wonPieces = wonPieces;
		this.column = column;
		this.line = line;
	}

	@Override
	public int compareTo(Object o)
	{
		if (wonPieces > ((IAProposal) o).wonPieces) return -1;
		else if (wonPieces < ((IAProposal) o).wonPieces) return 1;

		return 0;
	}
}
