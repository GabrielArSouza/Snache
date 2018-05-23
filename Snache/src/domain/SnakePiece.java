package domain;

public class SnakePiece extends BoardPiece
{
	private int x;
	private int y;
	
	public SnakePiece(SnakePiece old)
	{
		super(old.getX(), old.getY());
	}
	
	public SnakePiece(int x, int y)
	{
		super(x, y);
	}
	
	public void copy(SnakePiece otherSnakePiece)
	{
		this.x = otherSnakePiece.getX();
		this.y = otherSnakePiece.getY();
	}
}
