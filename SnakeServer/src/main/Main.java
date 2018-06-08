package main;

import controller.Game;
import domain.Board;
import presentation.ServerBoardFrame;
import socket.SocketServerSnake;

/**
 * Driver class of the server-side component of the game. It contains the main method only.
 */
public class Main 
{
	/**
	 * Driver method of the applications.
	 *
	 * @param args the arguments passed in the game execution on terminal
	 */
	public static void main(String[] args)
	{
		Board board = new Board(50, 50);
		ServerBoardFrame serverFrm = new ServerBoardFrame(50, 50, 10);
		serverFrm.setVisible(true);
		Game game = new Game(board);
		SocketServerSnake socketServer = new SocketServerSnake(game, serverFrm);
		socketServer.initSocket();
	}
}
