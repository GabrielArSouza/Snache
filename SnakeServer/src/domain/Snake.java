package domain;

import java.util.ArrayList;
import java.util.List;

import controller.EnumSnakeDirection;

/**
 * The class Snake represents a snake on the logic board.
 */
public class Snake
{

	/** number of SnakePiece objects in the List<SnakePiece> object. */
	private int bodySize;

	/** The head of the snake. */
	private SnakePiece head;

	/** The body of the snake (including tail). */
	private List<SnakePiece> body;

	/** The current direction of the snake. */
	private EnumSnakeDirection direction;

	/** The id of the snake. */
	private long id;

	/**
	 * Instantiates a new snake. The snake body is built from a head position and a
	 * size only, meaning that the snake is, initially, contiguous.
	 *
	 * @param headRow
	 *            the row of the snake on the board
	 * @param headColumn
	 *            the column of the snake on the board
	 * @param bodySize
	 *            the size of the body of the snake
	 * @param initialDirection
	 *            the initial direction of the snake
	 * @param id
	 *            the id of the snake
	 */
	public Snake(int headRow, int headColumn, int bodySize, EnumSnakeDirection initialDirection, long id)
	{
		this.id = id;
		this.head = new SnakePiece(headRow, headColumn);
		this.bodySize = bodySize;
		this.body = new ArrayList<SnakePiece>();

		// inits the body pieces in a contiguous way
		for(int i = 0; i < bodySize; ++i)
		{
			if(initialDirection == EnumSnakeDirection.LEFT)
			{
				body.add(new SnakePiece(headRow, headColumn + i + 1));
			}

			else if(initialDirection == EnumSnakeDirection.RIGHT)
			{
				body.add(new SnakePiece(headRow, headColumn - i - 1));
			}

			else if(initialDirection == EnumSnakeDirection.DOWN)
			{
				body.add(new SnakePiece(headRow - i - 1, headColumn));
			}

			else if(initialDirection == EnumSnakeDirection.UP)
			{
				body.add(new SnakePiece(headRow + i + 1, headColumn));
			}

			this.direction = initialDirection;
		}
	}
	
	public long getId()
	{
		return id;
	}

	/**
	 * Moves the snake. It moves the head to a new position. Then, shifts all the
	 * pieces in the body except for the first. The new first piece of the body will
	 * be the old head.
	 */
	public void move()
	{
		// new head after the movement
		SnakePiece newHead = null;

		// moves the snake head only
		if(direction == EnumSnakeDirection.UP)
			newHead = new SnakePiece((head.getRow() - 1), head.getColumn());
		else if(direction == EnumSnakeDirection.DOWN)
			newHead = new SnakePiece((head.getRow() + 1), head.getColumn());
		else if(direction == EnumSnakeDirection.LEFT)
			newHead = new SnakePiece(head.getRow(), (head.getColumn() - 1));
		else if(direction == EnumSnakeDirection.RIGHT)
			newHead = new SnakePiece(head.getRow(), (head.getColumn() + 1));

		// moves the body. except for the first piece, each of the pieces will be
		// changed to
		// the previous one (nearer to the head) in the old body
		for(int i = body.size() - 1; i >= 1; i--)
			body.get(i).copy(body.get(i - 1));

		// assigns the old head to the first body piece
		body.get(0).copy(head);

		// updates the head
		head.copy(newHead);
	}

	/**
	 * Gets the head of the snake.
	 *
	 * @return the head of the snake
	 */
	public SnakePiece getHead()
	{
		return head;
	}

	/**
	 * Gets the body of the snake.
	 *
	 * @return the body of the snake
	 */
	public List<SnakePiece> getBody()
	{
		return body;
	}

	/**
	 * Gets the current direction of the snake.
	 *
	 * @return the direction of the snake
	 */
	public EnumSnakeDirection getDirection()
	{
		return direction;
	}

	/**
	 * Sets the current direction of the snake.
	 *
	 * @param direction
	 *            the new direction of the snake
	 */
	public void setDirection(EnumSnakeDirection direction)
	{
		this.direction = direction;
	}

	/**
	 * Gets the tail of the snake.
	 *
	 * @return the tail of the snake
	 */
	public SnakePiece getTail()
	{
		return body.get(bodySize - 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * The hash function depends only on the snake id because this attribute is
	 * immutable, unlike the head and the body, so that the snake's hash is always
	 * the same.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return Long.hashCode(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * The string representation of the snake is the coordinates of its head and its
	 * body pieces.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String res = "";
		res += "[" + head.getRow() + ", " + head.getColumn() + "], ";

		for(SnakePiece p : body)
		{
			res += "[" + p.getRow() + ", " + p.getColumn() + "], ";
		}

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * Two snakes are equal if they have the same ids.
	 * 
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
		Snake other = (Snake) obj;

		return id == other.id;
	}
}
