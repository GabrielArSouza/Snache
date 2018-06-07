package presentation;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import controller.GameConstants;

public class BoardCanvas extends Canvas
{
	/** ID used for serialization purposes */
	private static final long serialVersionUID = -2612521258159419326L;
	
	private Color[][] colorMatrix;
	
	private int nRows;
	private int nColumns;
	private int squareSize;
	
	public BoardCanvas(int nRows, int nColumns, int squareSize)
	{
		super.setSize(nColumns * squareSize, nRows * squareSize);
		super.setBackground(GameConstants.BACKGROUND_COLOR);
		
		colorMatrix = new Color[nRows][nColumns];
		
		this.nRows = nRows;
		this.nColumns = nColumns;
		this.squareSize = squareSize;
		
		clearCanvas();
	}
	
	@Override
	public void paint(Graphics g)
	{
		for(int i = 0; i < nRows; ++i)
		{
			for(int j = 0; j < nColumns; ++j)
			{
				g.setColor(colorMatrix[i][j]);
				g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
			}
		}
	}
	
	public void setColorAt(int row, int column, Color color)
	{
		colorMatrix[row][column] = color;
	}
	
	public void clearCanvas()
	{
		for(int i = 0; i < nRows; ++i)
		{
			for(int j = 0; j < nColumns; ++j)
			{
				colorMatrix[i][j] = GameConstants.BACKGROUND_COLOR;
			}
		}
	}
}
