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
import java.util.BitSet;

import controller.EnumSnakeDirection;
import controller.GameConstants;
import controller.SingletonSnakeDirectionChange;
import domain.SnakeConstants;
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
	
	private FrmBoard boardClient;
	/**
	 * Initializes the socket and calls its main method.
	 * @throws ClassNotFoundException 
	 */
	public void initSocket( FrmBoard boardClient ) throws ClassNotFoundException
	{
		this.boardClient = boardClient;
	
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

				System.out.println("direction sent to server: " + direction);

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
		public void run ()
		{
			while(true)
			{
				try {
					Thread.sleep(GameConstants.GAME_LATENCY);
					this.receiveFromServer();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		private void receiveFromServer () throws ClassNotFoundException
		{
			try 
			{
				// The data sent by server has a serialized object board matrix
				// Estimated size
				int maxSizeMessage = 2 * (SnakeConstants.STANDARD_BODY_SIZE+1) * GameConstants.MAX_NUMBER_SNAKES;
				
				byte[] dataBuffFromServer = new byte[maxSizeMessage];
				
				// packet that will be sent by server
				DatagramPacket packFromServer = new DatagramPacket(dataBuffFromServer, dataBuffFromServer.length);
				
				System.out.println("Tentando receber pacote do server...");
				socket.receive(packFromServer);
				
				System.out.println("Recebeu Pacote de " + packFromServer.getLength() + " bytes do server");
				
				long startTime = System.nanoTime();  
				// decodes the message sent by the server and updates the board
				this.decodeMessageAndUpdateBoard(packFromServer.getData(), packFromServer.getLength());
				System.out.println("ellapsed time (ms): "+ (System.nanoTime() - startTime)/1000000);
			
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

		private void decodeMessageAndUpdateBoard ( byte[] message, int realSize )
		{
			// first, clear the current board
			boardClient.clearBoard();
			
			System.out.println("Decodificando...");
			
			int posColumn = 0;
			int posRow = 0;
			
			/**
			 * This message contains all game's snakes 
			 * Each byte is a snake position
			 * A snake contains 20 bytes (160 bits)
			 * The snake that start in bit '1' is a client's snake
			 */
			boolean flagIsClientSanke = false;
			Color colorInPieceBord;
			int cont = 0;
			for (int i=0; i < realSize; i++)
			{
				// For each snake, verify that it belongs to the client
				if (i%20 == 0)
				{
					// verify if first bit is setting in 1
					if ( (message[i] & (1 << 7)) != 0 )
					{
						flagIsClientSanke = true;
						// unset a MSB
						message[i] &= ((1 << 7)-1);					
 					
					}else flagIsClientSanke = false;
				}
				
				// Define snake's color
				if (flagIsClientSanke)
					colorInPieceBord = GameConstants.CLIENT_SNAKE_COLOR;
				else 
					colorInPieceBord = GameConstants.ENEMY_SNAKE_COLOR;
							
				if (i%2 == 0)
				{
					// A number between 0 and 127
					posRow = Byte.toUnsignedInt(message[i]);
				}
				else 
				{
					// A number between 0 and 127
					posColumn = Byte.toUnsignedInt(message[i]);
					boardClient.setColorAt(posRow, posColumn, colorInPieceBord);
					System.out.println(posRow + " " + posColumn);
					
				}
			}
				
		}
	}
}
