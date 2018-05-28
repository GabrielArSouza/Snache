package controller;

public class SingletonSnakeDirection
{
	private String direction;
	private static SingletonSnakeDirection instance = new SingletonSnakeDirection();
	
    private SingletonSnakeDirection()
	{
		this.direction = "";
	}
    
    public static SingletonSnakeDirection getInstance()
    {
    	return instance;
    }
	
	public String consume()
	{
		String res = direction;
		
		if(!direction.isEmpty())
			direction = "";
		
		return res;
	}
	
	public void produce(String newDirection)
	{
		if(direction.isEmpty())
			direction = newDirection;
	}
}
