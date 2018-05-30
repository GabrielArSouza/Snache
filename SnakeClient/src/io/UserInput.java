package io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import controller.EnumSnakeDirection;
import controller.SingletonSnakeDirection;

/**
 * @author gabriel
 *
 */
public class UserInput implements KeyListener
{
	private SingletonSnakeDirection sharedDirection = SingletonSnakeDirection.getInstance();
	
	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		// Seta P/ baixo
		if(e.getKeyCode() == 40)
		{
			sharedDirection.produce(EnumSnakeDirection.DOWN.toString());
		}
		// Seta P/ cima
		if(e.getKeyCode() == 38)
		{
			sharedDirection.produce(EnumSnakeDirection.UP.toString());
		}
		// Seta P/ direita
		if(e.getKeyCode() == 39)
		{
			sharedDirection.produce(EnumSnakeDirection.RIGHT.toString());
		}
		// Seta P/ esquerda
		if(e.getKeyCode() == 37)
		{
			sharedDirection.produce(EnumSnakeDirection.LEFT.toString());
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub
	}
}
