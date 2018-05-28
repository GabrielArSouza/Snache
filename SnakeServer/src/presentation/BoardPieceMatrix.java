package presentation;

import java.awt.Color;

public class BoardPieceMatrix
{
	private FrmBoardPiece frmBoardPieces[][];
	private int width;
	private int height;
	
	public BoardPieceMatrix(int height, int width)
	{
		this.height = height;
		this.width = width;
		
		initPieces();
	}

	private void initPieces()
	{
		frmBoardPieces = new FrmBoardPiece[height][width];
		
		for(int i = 0; i < height; ++i)
		{
			for(int j = 0; j < width; ++j)
			{
				frmBoardPieces[i][j] = new FrmBoardPiece(Color.WHITE);
			}
		}
	}
	
	public void setColorAt(int x, int y, Color color)
	{
		frmBoardPieces[x][y].setColor(color);
	}
	
	public void clearBoard()
	{
		for (int i=0; i < this.height; i++)
		{
			for (int j=0; j < this.width; j++)
			{
				frmBoardPieces[i][j].setColor(Color.WHITE);
			}
		}
	}
	
	public FrmBoardPiece[][] getMatrix()
	{
		return frmBoardPieces;
	}
}
