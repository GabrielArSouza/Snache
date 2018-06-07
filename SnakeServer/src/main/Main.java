package main;

import controller.Game;
import domain.Board;
import presentation.BoardPieceMatrix;
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
		BoardPieceMatrix boardPieceMatrix = new BoardPieceMatrix(100, 100);
		Board board = new Board(50, 50);
		Game game = new Game(boardPieceMatrix, board);
		SocketServerSnake socketServer = new SocketServerSnake(game);
		socketServer.initSocket();
	}
}
