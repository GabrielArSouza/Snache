package controller;

/**
 * Wraps a EnumSnakeDirection object to be shared between a snake and a player.
 * This wrapper follows the producer-consumer paradigm. When the shared
 * direction is used, it becomes null
 */
public class SharedSnakeDirection
{
	/** The direction to be shared. */
	private EnumSnakeDirection direction;

	/**
	 * Instantiates a new wrapper.
	 *
	 * @param direction
	 *            the object to be shared
	 */
	public SharedSnakeDirection(EnumSnakeDirection direction)
	{
		this.direction = direction;
	}

	/**
	 * Consumes the current direction. The direction is set to null when this method
	 * is called. It doesn't consume the "SAME" direction change.
	 *
	 * @return the current snake direction
	 */
	public EnumSnakeDirection consume()
	{
		EnumSnakeDirection dir = direction;

		if(direction != null && direction != EnumSnakeDirection.SAME)
		{
			System.out.println("consuming...");
			direction = null;
		}
			
		return dir;
	}

	/**
	 * Sets the current direction to a specific value if it is null.
	 *
	 * @param direction
	 *            *the* string representation of the EnumSnakeDirection object
	 *            corresponding to the new direction
	 */
	public void produce(String direction)
	{
		produce(EnumSnakeDirection.getValue(direction));
	}

	/**
	 * Sets the current direction to a specific value if it is null.
	 *
	 * @param direction
	 *            the EnumSnakeDirection object corresponding to the new direction
	 */
	public void produce(EnumSnakeDirection direction)
	{
		System.out.println("trying to produce " + direction + " but I am " + this.direction);
		
		// it doesn't produce null or SAME directions
		if(direction == null || direction == EnumSnakeDirection.SAME)
		{
			return;
		}
		
		// it produces a direction only if there is not one already
		if(this.direction != null && this.direction != EnumSnakeDirection.SAME)
		{
			return;
		}
		
		this.direction = direction;
		System.out.println("produced direction " + direction);
	}
}
