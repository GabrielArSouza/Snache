package main;

import presentation.BoardFrame;

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
		//FrmBoard frmBoard = new FrmBoard(50, 50);
		//frmBoard.setVisible(true);
		//SocketClient socketClient = new SocketClient();
		//socketClient.initSocket(frmBoard);
		BoardFrame frm = new BoardFrame();
		frm.setVisible(true);
	}
}
