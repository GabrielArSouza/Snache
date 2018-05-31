package main;

import controller.Game;
import domain.Board;
import presentation.BoardPieceMatrix;
import socket.SocketServerSnake;

/**
 * Driver class of the server-side component of the game.
 */
public class Main 
{
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args)
	{
		BoardPieceMatrix boardPieceMatrix = new BoardPieceMatrix(20, 20);
		Board board = new Board(20, 20);
		Game game = new Game(boardPieceMatrix, board);
		SocketServerSnake socketServer = new SocketServerSnake(game);
		socketServer.init();
	}
}
