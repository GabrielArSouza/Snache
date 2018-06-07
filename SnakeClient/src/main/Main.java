package main;

import presentation.BoardFrame;
import socket.SocketClient;

/**
 * Driver class of the client-side of the application. It contains the main method only.
 */
public class Main
{	
	/**
	 * The main method of the program.
	 *
	 * @param args the arguments passed in the game execution on terminal
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException
	{
		BoardFrame frm = new BoardFrame(50, 50, 10);
		frm.setVisible(true);
		SocketClient socketClient = new SocketClient();
		socketClient.initSocket(frm);
		
	}
}
