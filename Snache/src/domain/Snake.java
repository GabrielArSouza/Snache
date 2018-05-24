package domain;

import java.util.ArrayList;
import java.util.List;

import controller.EnumSnakeDirection;

public class Snake
{
	// number of SnakePiece objects in the List<SnakePiece> object
	private int bodySize;
	
	private SnakePiece head;
	private List<SnakePiece> body;
	private EnumSnakeDirection direction;
	
	public Snake(int headRow, int headColumn, int bodySize, EnumSnakeDirection initialDirection)
	{
		this.head = new SnakePiece(headRow, headColumn);
		this.body = new ArrayList<SnakePiece>();
		this.bodySize = bodySize;
		
		for(int i = 0; i < bodySize; ++i)
		{
			if(initialDirection == EnumSnakeDirection.LEFT)
			{
				body.add(new SnakePiece(headRow, headColumn+i+1));
			}
			
			else if(initialDirection == EnumSnakeDirection.RIGHT)
			{
				body.add(new SnakePiece(headRow, headColumn-i-1));
			}
			
			else if(initialDirection == EnumSnakeDirection.DOWN)
			{
				body.add(new SnakePiece(headRow-i-1, headColumn));
			}
			
			else if(initialDirection == EnumSnakeDirection.UP)
			{
				body.add(new SnakePiece(headRow+i+1, headColumn));
			}
			
			this.direction = initialDirection;
		}
	}
	
	public Snake(int headPosition[], List<int[]> bodyPieces, EnumSnakeDirection initialDirection)
	{
		this.head = new SnakePiece(headPosition[0], headPosition[1]);
		this.body = new ArrayList<SnakePiece>();
		
		for(int[] piece : bodyPieces)
		{
			body.add(new SnakePiece(piece[0], piece[1]));
		}
		
		this.bodySize = bodyPieces.size();
		this.direction = initialDirection;
	}
	
	public SnakePiece getHead()
	{
		return head;
	}
	
	public List<SnakePiece> getBody()
	{
		return body;
	}
	
	public SnakePiece getTail()
	{
		return body.get(bodySize-1);
	}
}
