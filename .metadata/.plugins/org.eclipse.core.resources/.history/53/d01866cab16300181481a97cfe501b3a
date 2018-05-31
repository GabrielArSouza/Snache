package controller;

// TODO: Auto-generated Javadoc
/**
 * Manages the snake direction change given by the user. This singleton ensures
 * that if the user type many commands before they are sent to the server, only
 * the first one is considered. The class follows the producer-consumer logic.
 */
public class SingletonSnakeDirectionChange
{
	/** Direction given by the user. */
	private EnumSnakeDirection direction;

	/** The unique instance of the class. */
	private static SingletonSnakeDirectionChange instance = new SingletonSnakeDirectionChange();

	/**
	 * Instantiates a new singleton object. It starts with "SAME" since the player,
	 * in the beginning, didn't send any commands.
	 */
	private SingletonSnakeDirectionChange()
	{
		this.direction = EnumSnakeDirection.SAME;
	}

	/**
	 * Gets the single instance of SingletonSnakeDirection.
	 *
	 * @return single instance of SingletonSnakeDirection
	 */
	public static SingletonSnakeDirectionChange getInstance()
	{
		return instance;
	}

	/**
	 * Gets the direction change given by the user.
	 *
	 * @return direction change
	 */
	public EnumSnakeDirection consume()
	{
		EnumSnakeDirection res = direction;

		if(direction != EnumSnakeDirection.SAME)
			direction = EnumSnakeDirection.SAME;

		return res;
	}

	/**
	 * Sets the direction change given by the user, making it available to whoever
	 * needs it. If there was a previous nontrivial (i.e., not equal to SAME)
	 * direction change, it means it wasn't consumed yet. In that case, the
	 * direction change isn't modified.
	 *
	 * @param newDirection
	 *            the new direction change
	 */
	public void produce(EnumSnakeDirection newDirection)
	{
		if(direction == EnumSnakeDirection.SAME)
			direction = newDirection;
	}
}
