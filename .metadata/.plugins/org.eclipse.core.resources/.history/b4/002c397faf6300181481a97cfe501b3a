package controller;

public class SingletonSnakeDirection
{
	private EnumSnakeDirection direction;
	private static SingletonSnakeDirection instance = new SingletonSnakeDirection();
	
    private SingletonSnakeDirection()
	{
		this.direction = null;
	}
    
    public static SingletonSnakeDirection getInstance()
    {
    	return instance;
    }
	
	public EnumSnakeDirection consume()
	{
		EnumSnakeDirection res = direction;
		
		if(direction != null)
			direction = null;
		
		return res;
	}
	
	public void produce(EnumSnakeDirection newDirection)
	{
		if(direction == null)
			direction = newDirection;
	}
}
