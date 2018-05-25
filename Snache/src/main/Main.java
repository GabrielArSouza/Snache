package main;

import controller.Game;
import controller.EnumSnakeDirection;
import domain.Board;
import domain.Snake;
import presentation.FrmBoard;

public class Main 
{
	public static void main(String[] args)
	{
		FrmBoard frmBoard = new FrmBoard(50, 50);
		frmBoard.setVisible(true);
		Board board = new Board(50, 50);
		Game controller = new Game(frmBoard, board);
		
		// create and draw 3 random snakes
		for(int i = 0; i < 3; ++i)
		{
			System.out.println("---------------------------");
			Snake snake = controller.createSnake();
			
			if(snake != null)
			{
				controller.drawSnake(snake);
			}
		}		
	}
}
