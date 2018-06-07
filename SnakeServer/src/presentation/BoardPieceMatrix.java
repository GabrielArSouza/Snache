package presentation;

import java.awt.Color;
import java.io.Serializable;

import controller.GameConstants;

/**
 * Server's version of the board UI. It's just a wrapper for the board matrix
 * UI, and will be sent to the players through sockets.
 */
public class BoardPieceMatrix implements Serializable
{
	/** ID for serialization purposes */
	private static final long serialVersionUID = 1118830666788146452L;

	/** The UI pieces of the board. */
	private FrmBoardPiece frmBoardPieces[][];

	/** The width of the board. */
	private int width;

	/** The height of the board. */
	private int height;

	/**
	 * Instantiates a new board UI.
	 *
	 * @param height
	 *            the height of the board
	 * @param width
	 *            the width of the board
	 */
	public BoardPieceMatrix(int height, int width)
	{
		this.height = height;
		this.width = width;

		initPieces();
	}

	/**
	 * Initializes the pieces of the board UI.
	 */
	private void initPieces()
	{
		frmBoardPieces = new FrmBoardPiece[height][width];

		for(int i = 0; i < height; ++i)
		{
			for(int j = 0; j < width; ++j)
			{
				frmBoardPieces[i][j] = new FrmBoardPiece(GameConstants.BACK_COLOR);
			}
		}
	}

	/**
	 * Sets the color of a cell in a specific position.
	 *
	 * @param row
	 *            the row of the cell
	 * @param column
	 *            the column of the cell
	 * @param color
	 *            the new color of the cell
	 */
	public void setColorAt(int row, int column, Color color)
	{
		frmBoardPieces[row][column].setColor(color);
	}

	/**
	 * Sets all the cell colors on the board to the background one.
	 */
	public void clearBoard()
	{
		for(int i = 0; i < this.height; i++)
		{
			for(int j = 0; j < this.width; j++)
			{
				frmBoardPieces[i][j].setColor(Color.WHITE);
			}
		}
	}

	/**
	 * Gets the matrix of UI cells.
	 *
	 * @return the matrix of UI cells.
	 */
	public FrmBoardPiece[][] getMatrix()
	{
		return frmBoardPieces;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * The string representation of the board UI is a concatenation of the string
	 * representation of the UI cells.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String res = "";

		for(int i = 0; i < this.height; i++)
		{
			for(int j = 0; j < this.width; j++)
			{
				res += frmBoardPieces[i][j].toString();
			}

			res += "\n";
		}

		return res;
	}

	/**
	 * Getters and Setters Methods
	 */
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	
}
