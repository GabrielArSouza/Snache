package controller;

import java.awt.Color;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import domain.Board;
import domain.BoardPiece;
import domain.Snake;
import domain.SnakeConstants;
import domain.SnakePiece;
import presentation.FrmBoard;

public class BoardController
{
	private FrmBoard frmBoard;
	private Board board;
	private Stack<Color> availableColors;
	
	public BoardController(FrmBoard frmBoard, Board board)
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
	
	private Color getColor()
	{
		Color color = availableColors.pop();
		
		if(availableColors.empty())
			resetAvailableColors();
		
		return color;
	}
	
	public void drawSnake(Snake snake)
	{
		SnakePiece head = snake.getHead();
		Color color = getColor();
		
		frmBoard.setColorAt(head.getRow(), head.getColumn(), color);
		
		for(SnakePiece piece : snake.getBody())
		{
			frmBoard.setColorAt(piece.getRow(), piece.getColumn(), color);
		}
	}
	
	public Snake createSnake()
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
			return null;
		}
		
		System.out.println("new snake: " + headPiece.getRow() + " " + headPiece.getColumn() + " " + direction);
		
		return new Snake(headPiece.getRow(), headPiece.getColumn(), SnakeConstants.STANDARD_BODY_SIZE, direction);
	}
}