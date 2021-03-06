package io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import controller.EnumSnakeDirection;
import controller.SingletonSnakeDirectionChange;

/**
 * Manages the "low-level" communication between the player and the keyboard.
 * This class converts the interaction of the player with the keyboard into
 * actual commands in the game.
 */
public class UserInput implements KeyListener
{

	/**
	 * (Unique) instance of the singleton where the player commands will be stored.
	 */
	private SingletonSnakeDirectionChange sharedDirection = SingletonSnakeDirectionChange.getInstance();
	
	/*
	 * (non-Javadoc) This event isn't handled in this game.
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e)
	{
		// doesn't matter in this game
	}

	/*
	 * (non-Javadoc) When the player type an arrow key, this event is translated
	 * into a snake direction change. That change is passed to the singleton to be
	 * (possibly) sent to the server later
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e)
	{	
		// down arrow
		if(e.getKeyCode() == 40)
		{
			sharedDirection.produce(EnumSnakeDirection.DOWN);
		}

		// up arrow
		else if(e.getKeyCode() == 38)
		{
			sharedDirection.produce(EnumSnakeDirection.UP);
		}
		
		// right arrow
		if(e.getKeyCode() == 39)
		{
			sharedDirection.produce(EnumSnakeDirection.RIGHT);
		}
		
		// left arrow
		if(e.getKeyCode() == 37)
		{
			sharedDirection.produce(EnumSnakeDirection.LEFT);
		}
	}

	/*
	 * (non-Javadoc) This event isn't handled in this application.
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e)
	{
		// doesn't matter in this game
	}
}
