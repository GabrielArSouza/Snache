package domain;

import java.util.ArrayList;
import java.util.List;

public class Snake
{
	// number of SnakePiece objects in the List<SnakePiece> object
	private int bodySize;
	
	private SnakePiece head;
	private List<SnakePiece> body;
	
	public Snake(int headPosition[], List<int[]> bodyPieces)
	{
		this.head = new SnakePiece(headPosition[0], headPosition[1]);
		this.body = new ArrayList<SnakePiece>();
		
		for(int[] piece : bodyPieces)
		{
			body.add(new SnakePiece(piece[0], piece[1]));
		}
	}
	
	public SnakePiece getHead()
	{
		return head;
	}
	
	/**
	 * Makes the Snake move.
	 * @param x the new x coordinate of the head
	 * @param y the new y coordinate of the head
	 * @return the old position of the tail (in order to erase it from the board later) 
	 */
	public int[] move(int x, int y)
	{
		SnakePiece lastHead = new SnakePiece(head);
		SnakePiece lastTail = new SnakePiece(body.get(bodySize-1));
		
		// the new position of the piece in the body is the old head position
		head.copy(body.get(0));
		body.get(0).copy(lastHead);
		
		// new position of the other pieces: old position of the piece closer to the head
		for(int i = 1; i < bodySize-1; ++i)
		{
			body.get(i).copy(body.get(i-1));
		}
		
		return new int[]{lastTail.getX(), lastTail.getY()};
	}
}
