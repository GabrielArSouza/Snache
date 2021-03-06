package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The class Board represents the logic structure of the board used in the game.
 * Its components (BoardPiece objects) are either empty or nonempty.
 */
public class Board
{
	/** The board width. */
	private int width;

	/** The board height. */
	private int height;

	/** Matrix of components of the board. */
	private BoardPiece boardPieces[][];

	/**
	 * Instantiates a new board.
	 *
	 * @param width
	 *            the board width
	 * @param height
	 *            the board height
	 */
	public Board(int width, int height)
	{
		this.width = width;
		this.height = height;
		boardPieces = new BoardPiece[width][height];

		// inits the cells
		for(int i = 0; i < width; ++i)
		{
			for(int j = 0; j < height; ++j)
			{
				boardPieces[i][j] = new BoardPiece(i, j);
			}
		}
	}

	/**
	 * Gets the board width.
	 *
	 * @return the board width
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * Gets the board height.
	 *
	 * @return the board height
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * Gets a sequence of contiguous empty vertical components of a given size. This
	 * method is used to define a start position for a new snake. It iterates over
	 * all the columns of the board randomly, and then checks from top to bottom if
	 * there's an available vertical space.
	 *
	 * @param size
	 *            the size of the space
	 * @return the topmost component if such space exists, null otherwise
	 */
	public BoardPiece getAvailableColumn(int size)
	{
		if(size >= height)
			return null;

		// random permutation of the column indexes
		List<Integer> columnIndexes = new ArrayList<Integer>();
		for(int j = 0; j < width; ++j)
			columnIndexes.add(j);
		Collections.shuffle(columnIndexes);

		int currAvailableSize = 0;
		int currentColumn;

		for(int i = 0; i < width; ++i)
		{
			currentColumn = columnIndexes.get(i);

			for(int row = 0; row < height; ++row)
			{
				if(!boardPieces[row][currentColumn].isEmpty())
				{
					currAvailableSize = 0;
				}

				else
				{
					// a space with enough length was found: return the topmost position of it
					if(++currAvailableSize > size)
					{
						return new BoardPiece(row - size, currentColumn);
					}
				}
			}

		}

		currAvailableSize = 0;

		return null;
	}

	/**
	 * Gets a sequence of contiguous empty horizontal components of a given size.
	 * This method is used to define a start position for a new snake. It iterates
	 * over all the rows of the board randomly, and then checks from left to right
	 * if there's an available horizontal space.
	 *
	 * @param size
	 *            the size of the space
	 * @return the leftmost component if such space exists, null otherwise
	 */
	public BoardPiece getAvailableRow(int size)
	{
		if(size >= width)
			return null;

		// random permutation of the column indexes
		List<Integer> rowIndexes = new ArrayList<Integer>();
		for(int j = 0; j < height; ++j)
			rowIndexes.add(j);
		Collections.shuffle(rowIndexes);

		int currAvailableSize = 0;
		int currentRow;

		for(int i = 0; i < height; ++i)
		{
			currentRow = rowIndexes.get(i);

			for(int column = 0; column < width; ++column)
			{
				if(!boardPieces[currentRow][column].isEmpty())
				{
					currAvailableSize = 0;
				}

				else
				{
					// a space with enough length was found: return the leftmost position of it
					if(++currAvailableSize > size)
					{
						return new BoardPiece(currentRow, column - size);
					}
				}
			}

		}

		currAvailableSize = 0;

		return null;
	}

	/**
	 * Gets a piece at a specific position.
	 *
	 * @param row
	 *            the row of the piece
	 * @param column
	 *            the column of the piece
	 * @return the piece at the specified coordinates
	 */
	public BoardPiece getBoardPiece(int row, int column)
	{
		return boardPieces[row][column];
	}

	/**
	 * Sets a piece to nonempty. If the head of a snake moves to a nonempty piece,
	 * the snake dies
	 *
	 * @param p
	 *            the piece to be set to nonempty
	 */
	public void occupyBoardPiece(BoardPiece p)
	{
		boardPieces[p.getRow()][p.getColumn()].setEmpty(false);
	}

	/**
	 * Sets a piece to empty.
	 *
	 * @param p
	 *            the piece to be set to empty
	 */
	public void freeBoardPiece(BoardPiece p)
	{
		boardPieces[p.getRow()][p.getColumn()].setEmpty(true);
	}
}
