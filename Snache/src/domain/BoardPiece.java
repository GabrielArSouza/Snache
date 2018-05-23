package domain;

public class BoardPiece
{
	private int x;
	private int y;
	private boolean empty;
	
	public BoardPiece(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.empty = true;
	}
	
	public boolean isEmpty()
	{
		return empty;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
}
