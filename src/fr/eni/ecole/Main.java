package fr.eni.ecole;

public class Main
{
	public static void main(String[] args)
	{
		OthelloGame othelloGame = new OthelloGame(6);
		othelloGame.play(true, false);
	}
}
