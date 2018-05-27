package controller;

public class SharedSnakeDirection
{
	private EnumSnakeDirection direction;

	public SharedSnakeDirection(EnumSnakeDirection direction)
	{
		this.direction = direction;
	}

	public EnumSnakeDirection consume()
	{
		EnumSnakeDirection dir = direction;
		
		if(direction != null)
			direction = null;
		
		return dir;
	}

	public void produce(EnumSnakeDirection direction)
	{
		if(this.direction != null)
			this.direction = direction;
		
		else
		{
			// ignore;
		}
	}
}
