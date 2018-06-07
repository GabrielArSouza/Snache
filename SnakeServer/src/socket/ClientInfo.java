package socket;

import controller.EnumSnakeDirection;
import domain.Snake;

/**
 * This class contains information about a single player connected to the game.
 */
public class ClientInfo
{
	/**
	 * The max number of time steps that the player may not respond to the server.
	 */
	private final int MAX_ITERATIONS = 3;

	/**
	 * Number of chances that the player has to connect to the server. It receives
	 * initially the value of MAX_ITERATIONS. If it becomes 0, the player is
	 * disconnected by the server and its snake is killed.
	 */
	private int deadCont;

	private EnumSnakeDirection newDirection;

	/** The player's snake. */
	private Snake snake;
	
	private int port;

	/**
	 * Instantiates a new object.
	 *
	 * @param sharedSnakeDirection
	 *            the snake direction wrapper
	 * @param snake
	 *            the snake
	 */
	public ClientInfo(int port)
	{
		this.newDirection = null;
		deadCont = MAX_ITERATIONS;
		this.port = port;
	}
	
	public void setSnake(Snake snake)
	{
		this.snake = snake;
	}
	
	public EnumSnakeDirection consumeDirection()
	{
		EnumSnakeDirection dir = newDirection;
		newDirection = null;
		return dir;
	}
	
	public int getPort()
	{
		return port;
	}

	/**
	 * Gets the player's snake.
	 *
	 * @return the player's snake
	 */
	public Snake getSnake()
	{
		return snake;
	}

	/**
	 * Checks if the player is active. A player is considered active if his/her
	 * deadCont isn't 0, i.e., the server shouldn't disconnect him/her.
	 *
	 * @return true if the player is active, false otherwise
	 */
	public boolean isActive()
	{
		return deadCont != 0;
	}

	/**
	 * Decreases the counter of how many chances the player has to connect to the
	 * server.
	 */
	public void decreaseDeadCont()
	{
		--deadCont;
	}

	/**
	 * It sets the content of the EnumSnakeDirection shared wrapper to a new
	 * direction. The direction is updated only if the argument will make the
	 * current snake's direction change (i.e., if it's nor null neither SAME). It
	 * resets the counter of remaining chances since if this method is called, it
	 * means that the player is active.
	 *
	 * @param direction
	 *            the new direction to be consumed by the player's snake
	 */
	public void updateDirection(String direction)
	{
		// it has a direction already
		if(newDirection != null && newDirection != EnumSnakeDirection.SAME) 
		{
			return;
		}

		else
		{
			deadCont = MAX_ITERATIONS;
			System.out.println("updating player's snake direction to " + direction + "...");
			newDirection = EnumSnakeDirection.getValue(direction);
		}

	}
}
