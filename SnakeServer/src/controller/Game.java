package controller;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;

import domain.Board;
import domain.BoardPiece;
import domain.Snake;
import domain.SnakeConstants;
import domain.SnakePiece;
import presentation.BoardPieceMatrix;

/**
 * The Class Game.
 *
 * @author gabriel
 */
public class Game
{
	/** Reference to the board UI. */
	private BoardPieceMatrix boardMatrix;
	
	/** Reference to the board logic. */
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
	 * Instantiates a new game.
	 *
	 * @param frmBoard board UI
	 * @param board board logic
	 */
	public Game(BoardPieceMatrix boardMatrix, Board board)
	{
		this.boardMatrix = boardMatrix;
		this.board = board;
		this.snakeColors = new HashMap<Snake, Color>();
		this.snakeSharedDirections = new HashMap<Snake, SharedSnakeDirection>();
		this.availableColors = new Stack<Color>();
		resetAvailableColors();
	}
	
	/**
	 * Resets the stack of available colors.
	 * It shuffles the array of all possible colors and put the colors on a stack.
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
	 * Draw snakes.
	 * It clears the board first and then paint all the current snakes.
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
	 * Creates a snake.
	 *
	 * @param sharedDirection direction changer associated to the new snake
	 * @return true, if the snake was successfully created
	 * 		   false, otherwise
	 */
	public boolean createSnake(SharedSnakeDirection sharedDirection)
	{
		EnumSnakeDirection direction = null;
		SnakePiece headPiece = null;
		
		// try to find a free vertical space
		BoardPiece columnTopEdge = board.getAvailableColumn(SnakeConstants.STANDARD_BODY_SIZE+1);
		
		// there's available space for a vertical snake
		if(columnTopEdge != null)
		{
			// space in the lower part of the board:
			// the snake is moving upwards (so that it is further from touching the board edges), and the head is the upmost piece)
			if(columnTopEdge.getRow() >= board.getHeight()/2)
			{
				headPiece = new SnakePiece(columnTopEdge);
				direction = EnumSnakeDirection.UP;
			}
			
			// space in the upper part of the board:
			// the snake is moving downwards (so that it is further from touching the board edges), and the head is the downmost piece)
			else
			{
				headPiece = new SnakePiece(columnTopEdge.getRow() + SnakeConstants.STANDARD_BODY_SIZE, columnTopEdge.getColumn());
				direction = EnumSnakeDirection.DOWN;
			}
		}
		
		// no available vertical space
		else 
		{
			// try to find free horizontal space
			BoardPiece rowLeftmostPiece = board.getAvailableRow(SnakeConstants.STANDARD_BODY_SIZE+1);
			
			// there's available space for a horizontal snake
			if(rowLeftmostPiece != null)
			{
				// space in the right half of the board:
				// the snake is moving leftwards (so that it is further from touching the board edges), and the head is the leftmost piece)
				if(rowLeftmostPiece.getRow() >= board.getHeight()/2)
				{
					headPiece = new SnakePiece(rowLeftmostPiece);
					direction = EnumSnakeDirection.LEFT;
				}
				
				// space in the left half of the board:
				// the snake is moving rightwards (so that it is further from touching the board edges), and the head is the rightmost piece)
				else
				{
					headPiece = new SnakePiece(rowLeftmostPiece.getRow(), rowLeftmostPiece.getColumn() + SnakeConstants.STANDARD_BODY_SIZE);
					direction = EnumSnakeDirection.RIGHT;
				}
			}
		}
		
		if(headPiece == null)
		{
			return false;
		}
		
		System.out.println("new snake: " + headPiece.getRow() + " " + headPiece.getColumn() + " " + direction);
		
		Snake snake = new Snake(headPiece.getRow(), headPiece.getColumn(), SnakeConstants.STANDARD_BODY_SIZE, direction);
		Color color = getAvailableColor();
		
		snakeSharedDirections.put(snake, sharedDirection);
		snakeColors.put(snake, color);
		snakes.add(snake);
		fillSnakeOnBoard(snake);
		
		return true;
	}

	/**
	 * Moves all the snakes.
	 */
	public void moveSnakes()
	{
		// shuffles the snake list to *try* to be fair with the players
		Collections.shuffle(snakes);
		
		// using iterator so that we can delete snakes while iterating the snake list
		ListIterator<Snake> snakeIterator = snakes.listIterator();
		
		while(snakeIterator.hasNext())
		{
			Snake snake = snakeIterator.next();
			
			// Current snake direction
			EnumSnakeDirection oldDir = snake.getDirection();
						
			// Direction set by the user
			EnumSnakeDirection newDir = snakeSharedDirections.get(snake).consume();
			
			// Valid direction changes
			if( (newDir == EnumSnakeDirection.UP && oldDir != EnumSnakeDirection.DOWN) ||
				(newDir == EnumSnakeDirection.DOWN && oldDir != EnumSnakeDirection.UP) ||
				(newDir == EnumSnakeDirection.RIGHT && oldDir != EnumSnakeDirection.LEFT) ||
				(newDir == EnumSnakeDirection.LEFT && oldDir != EnumSnakeDirection.RIGHT))
			{
				snake.setDirection(newDir);
			}
			
			SnakePiece oldTail	= snake.getTail();
			
			snake.move();
			
			SnakePiece newHead = snake.getHead();
			boolean newHeadOnLimits = false;
			
			// the new head respects the board edges
			if(newHead.getRow() >= 0 && newHead.getRow() < board.getHeight() &&
			   newHead.getColumn() >= 0 && newHead.getColumn() < board.getWidth())
			{
				newHeadOnLimits = true;
			}
			
			// the cell to which the snake is moving is empty
			if(board.getBoardPiece(newHead.getRow(), newHead.getColumn()).isEmpty())
			{
				board.occupyBoardPiece(newHead);
				board.freeBoardPiece(oldTail);
			}
			
			// the cell isn't empty (either is a board edge or another snake): the snake dies!
			else
			{
				killMovedSnake(snake, oldTail, newHeadOnLimits);
				snakeIterator.remove();
			}
		}
	}
	
	/**
	 * Kills a snake that has moved already.
	 * Frees the space previously occupied by the snake, and remove
	 * references to that snake on the map attributes.
	 *
	 * @param snake the snake
	 * @param oldTail the old tail of the snake to be killed
	 */
	public void killMovedSnake(Snake snake, SnakePiece oldTail, boolean headOnLimits)
	{
		if(headOnLimits)
			board.freeBoardPiece(oldTail);
		
		for(SnakePiece bodyPiece : snake.getBody())
			board.freeBoardPiece(bodyPiece);
		
		snakeColors.remove(snake);
		snakeSharedDirections.remove(snake);
	}
	
	/**
	 * Makes a snake occupy some positions on the board.
	 *
	 * @param snake the snake
	 */
	public void fillSnakeOnBoard(Snake snake)
	{
		board.occupyBoardPiece(snake.getHead());
		
		for(SnakePiece bodyPiece : snake.getBody())
		{
			board.occupyBoardPiece(bodyPiece);
		}
	}
 }
