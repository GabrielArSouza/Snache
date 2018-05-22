package domain;

public class SnakePiece
{
	private int x;
	private int y;
	
	public SnakePiece(SnakePiece old)
	{
		this.x = old.x;
		this.y = old.y;
	}
	
	public SnakePiece(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void copy(SnakePiece otherPiece)
	{
		this.x = otherPiece.x;
		this.y = otherPiece.y;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public void setY(int y)
	{
		this.y = y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
}
