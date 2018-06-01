package socket;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import controller.EnumSnakeDirection;
import controller.GameConstants;
import controller.SingletonSnakeDirectionChange;
import presentation.FrmBoard;
import presentation.FrmBoardPiece;


/**
 * Makes the client-side communication between the player's game, that behaves
 * as a client, and the game's server. The client sends commands to the server
 * to change the direction of a snake, and receives a copy of the current state
 * of the board.
 */
public class SocketClient
{
	/** Socket by which the communication will be made. */
	private DatagramSocket socket;

	/** Singleton instance whose snake direction changes will be caught of. */
	private SingletonSnakeDirectionChange snakeDirection = SingletonSnakeDirectionChange.getInstance();
	
	private FrmBoardPiece boardClient[][];
	private int widthBoard;
	private int heithBoard;

	/**
	 * Initializes the socket and calls its main method.
	 * @throws ClassNotFoundException 
	 */
	public void initSocket( FrmBoardPiece boardClient[][], int w, int h ) throws ClassNotFoundException
	{
		this.boardClient = boardClient;
		this.widthBoard = w;
		this.heithBoard = h;
		
		/**
		 * Create Thread for update client direction 
		 */
		Thread  gameUpdate = new GameUpdate();
		gameUpdate.start();
		
		try
		{
			socket = new DatagramSocket();
			sendDataToServer();
		}

		catch (SocketException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends data to the server. Every "GAME_LATENCY" milliseconds it sends a snake
	 * direction change to the server. It doesn't need to send a dummy package to
	 * request connection, so the first package is also a snake direction. The
	 * server is responsible for identifying that the player is, in practice,
	 * requesting a connection.
	 * @throws ClassNotFoundException 
	 */
	public void sendDataToServer() throws ClassNotFoundException
	{
		String direction;

		try
		{
			// TODO change it to something other than the localhost
			// ip address of the player
			InetAddress ip = InetAddress.getByName("localhost");

			while(true)
			{
				// catches the direction change from the singleton
				direction = snakeDirection.consume().toString();

				//System.out.println("direction sent to server: " + direction);

				// builds the package to be sent to the server
				byte[] dataToSend = direction.getBytes();
				DatagramPacket packToSend = new DatagramPacket(dataToSend, dataToSend.length, ip,
						SocketConstants.STANDARD_PORT);

				// sends the packet
				socket.send(packToSend);
				
				// the next package is sent after GAME_LATENCY milliseconds
				Thread.sleep(GameConstants.GAME_LATENCY);
				
			}

		}

		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class GameUpdate extends Thread
	{
		@Override
		public void run()
		{
			try{
				while(true)
				{
					// the updates occur every GAME_LATENCY milliseconds
					Thread.sleep(GameConstants.GAME_LATENCY);
					receiveFromServer();
				}

			}

			catch (InterruptedException e)
			{
				e.printStackTrace();
			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		/**
		 * Receive data from the server
		 * @throws ClassNotFoundException 
		 */
		private void receiveFromServer () throws ClassNotFoundException
		{
			//System.out.println("Recebeu dados do server");
			try 
			{
				// The data sent by server has a serialized object board matrix
				// Estimated size: 4KB
				byte[] dataBuffFromServer = new byte[4096];
				
				// continuously listens to new data on the socket
				// packet that will be sent by server
				DatagramPacket packFromServer = new DatagramPacket(dataBuffFromServer, dataBuffFromServer.length);
				socket.receive(packFromServer);
					
				// interpreting object
				ByteArrayInputStream bis = new ByteArrayInputStream(packFromServer.getData());
				ObjectInput in = null;
					
				Object boardMatrixObject;
				try 
				{
					in = new ObjectInputStream(bis);
					boardMatrixObject = in.readObject();
				}
				finally {
					try {
						if (in != null) {
							in.close();
					    }
					} catch (IOException ex) {
					    // ignore close exception
					}
				}
					
				PieceCodMessage messageCodefy[][] = (PieceCodMessage[][]) boardMatrixObject;	
				this.decodeMessage(messageCodefy);
			
			}
			catch (SocketException e)
			{
				socket.close();
				e.printStackTrace();
			}

			catch (IOException i)
			{
				socket.close();
				i.printStackTrace();
			}
		}
		
		private void decodeMessage ( PieceCodMessage message[][] )
		{
			/**
			 * If color is a background color, so assigned code 00 in message
			 * If color is a user snake, so assigned code 01 in message
			 * If color is other snake, so assigned code 10 in message
			 */	
		
			System.out.println("Decodificando...");
			String linha;
			
			for (int i=0; i < heithBoard; i++)
			{
				linha = ".";
				for (int j=0; j < widthBoard; j++)
				{
					if ((!message[i][j].isBit1()) && (!message[i][j].isBit2()) )
						linha += ".";
					else linha += "@";
				}
				System.out.println(linha);
			}
		}
	}
}
