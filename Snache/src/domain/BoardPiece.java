package domain;

// TODO: Auto-generated Javadoc
/**
 * The Class BoardPiece.
 */
public class BoardPiece
{
	
	/** The row. */
	private int row;
	
	/** The column. */
	private int column;
	
	/** The empty. */
	private boolean empty;
	
	/**
	 * Instantiates a new board piece.
	 *
	 * @param row the row
	 * @param column the column
	 */
	public BoardPiece(int row, int column)
	{
		this.row = row;
		this.column = column;
		this.empty = true;
	}
	
	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty()
	{
		return empty;
	}
	
	/**
	 * Gets the row.
	 *
	 * @return the row
	 */
	public int getRow()
	{
		return row;
	}
	
	/**
	 * Gets the column.
	 *
	 * @return the column
	 */
	public int getColumn()
	{
		return column;
	}

	/**
	 * Sets the row.
	 *
	 * @param row the new row
	 */
	public void setRow(int row)
	{
		this.row = row;
	}

	/**
	 * Sets the column.
	 *
	 * @param column the new column
	 */
	public void setColumn(int column)
	{
		this.column = column;
	}
	
	public void setEmpty(boolean empty)
	{
		this.empty = empty;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

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
