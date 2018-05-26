package io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import controller.EnumSnakeDirection;
import controller.SingletonDir;

/**
 * @author gabriel
 *
 */
public class UserInput implements KeyListener{

	SingletonDir dir = SingletonDir.getInstance();
	
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
        	dir.setDirection(EnumSnakeDirection.DOWN);
        }
        //Seta P/ cima        
        if (e.getKeyCode()==38)
        {
        	dir.setDirection(EnumSnakeDirection.UP);
        }
        //Seta P/ direita
        if (e.getKeyCode()==39)
        {
        	dir.setDirection(EnumSnakeDirection.RIGHT);
        }
        //Seta P/ esquerda
        if (e.getKeyCode()==37)
        {
        	dir.setDirection(EnumSnakeDirection.LEFT);
        }
    }

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}
