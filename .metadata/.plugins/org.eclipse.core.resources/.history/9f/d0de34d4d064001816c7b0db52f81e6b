package presentation;

import java.awt.Color;

/**
 * Board UI that will be sent to the players by socket.
 */
public class BoardPieceMatrix
{
	/** The UI pieces of the board. */
	private FrmBoardPiece frmBoardPieces[][];
	
	/** The width of the board. */
	private int width;
	
	/** The height of the board. */
	private int height;
	
	/**
	 * Instantiates a new board UI.
	 *
	 * @param height the height
	 * @param width the width
	 */
	public BoardPieceMatrix(int height, int width)
	{
		this.height = height;
		this.width = width;
		
		initPieces();
	}

	/**
	 * Inits the pieces.
	 */
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
	
	/**
	 * Sets the color at.
	 *
	 * @param x the x
	 * @param y the y
	 * @param color the color
	 */
	public void setColorAt(int x, int y, Color color)
	{
		frmBoardPieces[x][y].setColor(color);
	}
	
	/**
	 * Clear board.
	 */
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
	
	/**
	 * Gets the matrix.
	 *
	 * @return the matrix
	 */
	public FrmBoardPiece[][] getMatrix()
	{
		return frmBoardPieces;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String res = "";
		
		for(int i=0; i < this.height; i++)
		{
			for(int j=0; j < this.width; j++)
			{
				res += frmBoardPieces[i][j].toString();
			}
			
			res += "\n";
		}
		
		return res;
	}
}
