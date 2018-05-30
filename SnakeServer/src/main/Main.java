package main;

import controller.Game;
import domain.Board;
import presentation.BoardPieceMatrix;
import socket.SocketServerSnake;

public class Main 
{
	public static void main(String[] args)
	{
		BoardPieceMatrix boardPieceMatrix = new BoardPieceMatrix(50, 50);
		Board board = new Board(50, 50);
		Game game = new Game(boardPieceMatrix, board);
		SocketServerSnake socketServer = new SocketServerSnake(game);
		socketServer.init();
	}
}
