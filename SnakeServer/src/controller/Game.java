package controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;

import domain.Board;
import domain.BoardPiece;
import domain.Snake;
import domain.SnakeConstants;
import domain.SnakePiece;
import presentation.BoardPieceMatrix;

// TODO: Auto-generated Javadoc
/**
 * Class that implements the game mechanics.
 *
 * @author gabriel
 */
public class Game
{
	/** The ids of the active snakes on the board. */
	private SortedSet<Long> currentIds;

	/** Reference to the board UI. */
	private BoardPieceMatrix boardMatrix;

	/** Reference to the board logic object. */
	private Board board;

	/** Colors that can be used in new snakes. */
	private Stack<Color> availableColors;

	/** Current snakes in the board. */
	private List<Snake> snakes;

	/** Colors assigned to the current snakes. */
	private Map<Snake, Color> snakeColors;

	/** Direction changers assigned to the current snakes. */
	private Map<Snake, SharedSnakeDirection> snakeSharedDirections;

	/**
	 * Instantiates a new game object.
	 *
	 * @param boardMatrix
	 *            the board UI
	 * @param board
	 *            the logic board
	 */
	public Game(BoardPieceMatrix boardMatrix, Board board)
	{
		this.boardMatrix = boardMatrix;
		this.board = board;
		this.snakes = new ArrayList<Snake>();
		this.snakeColors = new HashMap<Snake, Color>();
		this.snakeSharedDirections = new HashMap<Snake, SharedSnakeDirection>();
		this.availableColors = new Stack<Color>();
		this.currentIds = new TreeSet<Long>();
		resetAvailableColors();
	}

	/**
	 * Resets the stack of available colors. It shuffles the array of all possible
	 * colors and put the colors on a stack.
	 */
	private void resetAvailableColors()
	{
		List<Color> allColors = Arrays.asList(SnakeConstants.COLORS);
		Collections.shuffle(allColors);
		availableColors.addAll(allColors);
	}

	/**
	 * Gets an available color from the stack of colors.
	 *
	 * @return the top color on the stack of colors.
	 */
	private Color getAvailableColor()
	{
		Color color = availableColors.pop();

		if(availableColors.empty())
			resetAvailableColors();

		return color;
	}

	/**
	 * Draw snakes. It clears the board first and then paint all the current snakes.
	 */
	public void drawSnakes()
	{
		boardMatrix.clearBoard();

		for(Snake snake : snakes)
		{
			SnakePiece head = snake.getHead();
			Color color = snakeColors.get(snake);

			boardMatrix.setColorAt(head.getRow(), head.getColumn(), color);

			for(SnakePiece piece : snake.getBody())
			{
				boardMatrix.setColorAt(piece.getRow(), piece.getColumn(), color);
			}
		}
	}

	/**
	 * Creates a new snake.
	 *
	 * @param sharedDirection
	 *            direction changer associated to the new snake
	 * @return the snake created, or null if the creation wasn't possible
	 */
	public Snake createSnake(SharedSnakeDirection sharedDirection)
	{
		EnumSnakeDirection direction = null;
		SnakePiece headPiece = null;

		// try to find a free vertical space
		BoardPiece columnTopEdge = board.getAvailableColumn(SnakeConstants.STANDARD_BODY_SIZE + 1);

		// there's available space for a vertical snake
		if(columnTopEdge != null)
		{
			// space in the lower part of the board:
			// the snake is moving upwards (so that it is further from touching the board
			// edges), and the head is the topmost piece)
			if(columnTopEdge.getRow() >= board.getHeight() / 2)
			{
				headPiece = new SnakePiece(columnTopEdge);
				direction = EnumSnakeDirection.UP;
			}

			// space in the upper part of the board:
			// the snake is moving downwards (so that it is further from touching the board
			// edges), and the head is the bottom-most piece)
			else
			{
				headPiece = new SnakePiece(columnTopEdge.getRow() + SnakeConstants.STANDARD_BODY_SIZE,
						columnTopEdge.getColumn());
				direction = EnumSnakeDirection.DOWN;
			}
		}

		// no available vertical space
		else
		{
			// try to find free horizontal space
			BoardPiece rowLeftmostPiece = board.getAvailableRow(SnakeConstants.STANDARD_BODY_SIZE + 1);

			// there's available space for a horizontal snake
			if(rowLeftmostPiece != null)
			{
				// space in the right half of the board:
				// the snake is moving leftwards (so that it is further from touching the board
				// edges), and the head is the leftmost piece)
				if(rowLeftmostPiece.getRow() >= board.getHeight() / 2)
				{
					headPiece = new SnakePiece(rowLeftmostPiece);
					direction = EnumSnakeDirection.LEFT;
				}

				// space in the left half of the board:
				// the snake is moving rightwards (so that it is further from touching the board
				// edges), and the head is the rightmost piece)
				else
				{
					headPiece = new SnakePiece(rowLeftmostPiece.getRow(),
							rowLeftmostPiece.getColumn() + SnakeConstants.STANDARD_BODY_SIZE);
					direction = EnumSnakeDirection.RIGHT;
				}
			}
		}

		if(headPiece == null)
		{
			return null;
		}

		System.out.println("snake created: " + headPiece.getRow() + " " + headPiece.getColumn() + " " + direction);

		Snake snake = new Snake(headPiece.getRow(), headPiece.getColumn(), SnakeConstants.STANDARD_BODY_SIZE, direction,
				nextAvailableId());
		Color color = getAvailableColor();

		snakeSharedDirections.put(snake, sharedDirection);
		snakeColors.put(snake, color);
		snakes.add(snake);
		fillSnakeOnBoard(snake);

		return snake;
	}

	/**
	 * Moves all the snakes on the board. Builds a <Snake, Integer> map containing
	 * the order in which the snakes moved.
	 *
	 * @return a map whose keys are the snakes, and each value is the time at which
	 *         each snake moved
	 */
	public Map<Snake, Integer> moveSnakes()
	{
		// times in which the snakes moved 
		Map<Snake, Integer> moveTimes = new HashMap<Snake, Integer>();
		
		// shuffles the snake list to *try* to be fair with the players
		//Collections.shuffle(snakes);

		// using iterator so that we can delete snakes while iterating the snake list
		ListIterator<Snake> snakeIterator = snakes.listIterator();
		
		// time in which the snakes will move at 
		int moveTime = 0;

		while(snakeIterator.hasNext())
		{
			Snake snake = snakeIterator.next();

			// Current snake direction
			EnumSnakeDirection oldDir = snake.getDirection();

			System.out.println(snake);
			// Direction set by the user
			EnumSnakeDirection newDir = snakeSharedDirections.get(snake).consume();

			// Valid direction changes
			if((newDir == EnumSnakeDirection.UP
					&& (oldDir == EnumSnakeDirection.LEFT || oldDir == EnumSnakeDirection.RIGHT))
					|| (newDir == EnumSnakeDirection.DOWN
							&& (oldDir == EnumSnakeDirection.LEFT || oldDir == EnumSnakeDirection.RIGHT))
					|| (newDir == EnumSnakeDirection.LEFT
							&& (oldDir == EnumSnakeDirection.UP || oldDir == EnumSnakeDirection.DOWN))
					|| (newDir == EnumSnakeDirection.RIGHT
							&& (oldDir == EnumSnakeDirection.UP || oldDir == EnumSnakeDirection.DOWN)))
			{
				snake.setDirection(newDir);
			}

			else
			{
				System.out.println("INVALID DIRECTION CHANGE! old = " + oldDir + ", new = " + newDir);
			}

			// old tail position set to empty now
			board.freeBoardPiece(snake.getTail());

			snake.move();

			SnakePiece newHead = snake.getHead();
			boolean newHeadOnLimits = false;

			// verify if the new head respects the board edges
			if(newHead.getRow() >= 0 && newHead.getRow() < board.getHeight() && newHead.getColumn() >= 0
					&& newHead.getColumn() < board.getWidth())
			{
				newHeadOnLimits = true;
			}

			// the cell to which the snake is moving is empty and the head respect the board
			// edges
			if(newHeadOnLimits && board.getBoardPiece(newHead.getRow(), newHead.getColumn()).isEmpty())
			{
				board.occupyBoardPiece(newHead);
			}

			// the cell isn't empty or the new head doesn't respect the board edges: the
			// snake dies!
			else
			{
				killMovedSnake(snake);
				snakeIterator.remove();
				System.out.println("Tentou remover");
			}
			
			// records the time at which this snake moved 
			moveTimes.put(snake, moveTime++);
		}
		
		return moveTimes;
	}

	/**
	 * Kill a snake (before it moves) whose player is inactive.
	 *
	 * @param snake
	 *            the snake
	 */
	public void killInactiveSnake(Snake snake)
	{
		board.freeBoardPiece(snake.getHead());

		for(SnakePiece bodyPiece : snake.getBody())
			board.freeBoardPiece(bodyPiece);

		snakeColors.remove(snake);
		snakeSharedDirections.remove(snake);

		snakes.remove(snake);
	}

	/**
	 * Kills a snake that has moved already. Frees the space previously occupied by
	 * the snake, and remove references to that snake on the map attributes.
	 *
	 * @param snake
	 *            the snake to be killed
	 */
	public void killMovedSnake(Snake snake)
	{
		// The position of the new head in the board wasn't set to nonempty, so it
		// doesn't need to be freed. The position of the old head is the first position
		// of the bodyPiece list because the snake moved, so it is freed in this loop.
		for(SnakePiece bodyPiece : snake.getBody())
			board.freeBoardPiece(bodyPiece);

		snakeColors.remove(snake);
		snakeSharedDirections.remove(snake);
	}

	/**
	 * Makes a snake occupy some positions on the board.
	 *
	 * @param snake
	 *            the snake
	 */
	public void fillSnakeOnBoard(Snake snake)
	{
		board.occupyBoardPiece(snake.getHead());

		for(SnakePiece bodyPiece : snake.getBody())
		{
			board.occupyBoardPiece(bodyPiece);
		}
	}

	/**
	 * Prints the board on the console.
	 */
	public void printBoardMatrix()
	{
		System.out.println(boardMatrix);
	}

	/**
	 * Gets the least id available.
	 *
	 * @return the least id available to be assigned to a snake
	 */
	public Long nextAvailableId()
	{
		if(currentIds.isEmpty())
		{
			return 0l;
		}

		else
		{
			Long currentId = 0l;

			for(Long id : currentIds)
			{
				if(id == currentId)
				{
					++currentId;
				}

				else
				{
					return currentId;
				}
			}

			return currentId;
		}
	}

	/**
	 * Gets the board matrix game.
	 *
	 * @return the board matrix object
	 */
	public BoardPieceMatrix getBoardMatrix()
	{
		return this.boardMatrix;
	}

	/**
	 * Gets the snakes.
	 *
	 * @return the snakes
	 */
	public List<Snake> getSnakes()
	{
		return snakes;
	}
}
