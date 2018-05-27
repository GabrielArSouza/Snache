package controller;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;

import domain.Board;
import domain.BoardPiece;
import domain.Snake;
import domain.SnakeConstants;
import domain.SnakePiece;
import presentation.FrmBoard;

/**
 * @author gabriel
 *
 */
public class Game
{
	private FrmBoard frmBoard;
	private Board board;
	private Stack<Color> availableColors;
	private List<Snake> snakes;
	private Map<Snake, Color> snakeColors;
	
	public Game(FrmBoard frmBoard, Board board)
	{
		this.frmBoard = frmBoard;
		this.board = board;
		this.availableColors = new Stack<Color>();
		resetAvailableColors();
	}
	
	private void resetAvailableColors()
	{
		List<Color> allColors = Arrays.asList(SnakeConstants.COLORS);
		Collections.shuffle(allColors);
		availableColors.addAll(allColors);
	}
	
	private Color getAvailableColor()
	{
		Color color = availableColors.pop();
		
		if(availableColors.empty())
			resetAvailableColors();
		
		return color;
	}
	
	public void drawSnakes()
	{
		frmBoard.clearBoard();
		
		for(Snake snake : snakes)
		{
			SnakePiece head = snake.getHead();
			Color color = snakeColors.get(snake);
			
			frmBoard.setColorAt(head.getRow(), head.getColumn(), color);
			
			for(SnakePiece piece : snake.getBody())
			{
				frmBoard.setColorAt(piece.getRow(), piece.getColumn(), color);
			}
		}
	}
	
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
		
		Snake snake = new Snake(headPiece.getRow(), headPiece.getColumn(), SnakeConstants.STANDARD_BODY_SIZE, direction, sharedDirection);
		Color color = getAvailableColor();
		
		snakeColors.put(snake, color);
		snakes.add(snake);
		fillSnakeOnBoard(snake);
		
		return true;
	}

	public void moveSnakes()
	{
		// using iterator so that we can delete snakes while iterating the snake list
		ListIterator<Snake> snakeIterator = snakes.listIterator();
		
		while(snakeIterator.hasNext())
		{
			Snake snake = snakeIterator.next();
			
			// Current snake direction
			EnumSnakeDirection oldDir = snake.getDirection();
						
			// Direction set by the user
			EnumSnakeDirection newDir = snake.getSharedDirection().consume();
			
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
		
			// the cell to which the snake is moving is empty
			if(board.getBoardPiece(newHead.getRow(), newHead.getColumn()).isEmpty())
			{
				board.occupyBoardPiece(newHead);
				board.freeBoardPiece(oldTail);
			}
			
			// the cell isn't empty (either is a board edge or another snake): the snake dies!
			else
			{
				killMovedSnake(snake, oldTail);
				snakeIterator.remove();
			}
		}
	}
	
	public void killMovedSnake(Snake snake, SnakePiece oldTail)
	{
		for(SnakePiece bodyPiece : snake.getBody())
			board.freeBoardPiece(bodyPiece);
		
		board.freeBoardPiece(oldTail);
	}
	
	public void fillSnakeOnBoard(Snake snake)
	{
		board.occupyBoardPiece(snake.getHead());
		
		for(SnakePiece bodyPiece : snake.getBody())
		{
			board.occupyBoardPiece(bodyPiece);
		}
	}

	public List<Snake> getSnakes() 
	{
		return snakes;
	}

	public void setSnakes(List<Snake> snakes) 
	{
		this.snakes = snakes;
	}
	
	public void mainLoop()
	{
		
	}
 }
