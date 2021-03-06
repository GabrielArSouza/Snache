package domain;

/**
 * This class represents one component of the logic board.
 */
public class BoardPiece
{
	/** The row of the piece in the board. */
	private int row;

	/** The column of the piece in the board. */
	private int column;

	/** false if there's a snake in this piece, false otherwise. */
	private boolean empty;

	/**
	 * Instantiates a new piece.
	 *
	 * @param row
	 *            the row of the board
	 * @param column
	 *            the column of the board
	 */
	public BoardPiece(int row, int column)
	{
		this.row = row;
		this.column = column;
		this.empty = true;
	}

	/**
	 * Checks if the piece is empty (i.e., if there isn't any snake in it).
	 *
	 * @return true if the piece is empty, false otherwise
	 */
	public boolean isEmpty()
	{
		return empty;
	}

	/**
	 * Gets the row of the piece in the board.
	 *
	 * @return the row in the board
	 */
	public int getRow()
	{
		return row;
	}

	/**
	 * Gets the column of the piece in the board.
	 *
	 * @return the column in the board
	 */
	public int getColumn()
	{
		return column;
	}

	/**
	 * Sets the row of the piece.
	 *
	 * @param row
	 *            the new row of the piece
	 */
	public void setRow(int row)
	{
		this.row = row;
	}

	/**
	 * Sets the column of the piece.
	 *
	 * @param column
	 *            the new column of the piece
	 */
	public void setColumn(int column)
	{
		this.column = column;
	}

	/**
	 * Sets the piece to either empty or nonempty.
	 *
	 * @param empty true to set to empty, false to set to nonempty
	 */
	public void setEmpty(boolean empty)
	{
		this.empty = empty;
	}
		
	/* (non-Javadoc)
	 * 
	 * Two BoardPiece objects are equal when so are their rows and columns.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		BoardPiece other = (BoardPiece) obj;
		if(column != other.column)
			return false;
		if(row != other.row)
			return false;
		return true;
	}
}
