package main;

import presentation.FrmBoard;
import socket.SocketClient;

public class Main
{
	public static void main(String[] args)
	{
		FrmBoard frmBoard = new FrmBoard(50, 50);
		frmBoard.setVisible(true);
		SocketClient socketClient = new SocketClient();
		socketClient.initSocket();
	}
}
