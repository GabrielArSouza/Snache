package domain;

import java.util.ArrayList;
import java.util.List;

public class Board
{
	private int width;
	private int height;
	private BoardPiece boardPieces[][];
	
	public Board(int width, int height)
	{
		this.width = width;
		this.height = height;
		boardPieces = new BoardPiece[width][height];
		
		for(int i = 0; i < width; ++i)
		{
			for(int j = 0; j < height; ++j)
			{
				boardPieces[i][j] = new BoardPiece(i, j);
			}
		}
	}
	
	public List<BoardPiece> availablePieces()
	{
		List<BoardPiece> available = new ArrayList<BoardPiece>();
		
		for(int i = 0; i < j; ++)
	}
}
