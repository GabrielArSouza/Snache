package main;

import presentation.FrmBoard;
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
	 */
	public static void main(String[] args)
	{
		FrmBoard frmBoard = new FrmBoard(20, 20);
		frmBoard.setVisible(true);
		SocketClient socketClient = new SocketClient();
		socketClient.initSocket();
	}
}
