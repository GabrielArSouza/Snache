package io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import controller.EnumSnakeDirection;
import controller.SharedSnakeDirection;

/**
 * @author gabriel
 *
 */
public class UserInput implements KeyListener{

	private SharedSnakeDirection direction;
	
	public UserInput(SharedSnakeDirection direction)
	{
		super();
		this.direction = direction;
	}
	
	public void setSharedDirection(SharedSnakeDirection direction)
	{
		this.direction = direction;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) { 
		System.out.println("Pressed");
		//Seta P/ baixo
        if (e.getKeyCode()==40)
        {
        	direction.produce(EnumSnakeDirection.DOWN);
        }
        //Seta P/ cima        
        if (e.getKeyCode()==38)
        {
        	direction.produce(EnumSnakeDirection.UP);
        }
        //Seta P/ direita
        if (e.getKeyCode()==39)
        {
        	direction.produce(EnumSnakeDirection.RIGHT);
        }
        //Seta P/ esquerda
        if (e.getKeyCode()==37)
        {
        	direction.produce(EnumSnakeDirection.LEFT);
        }
    }

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}
