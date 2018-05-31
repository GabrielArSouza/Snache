package socket;

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

	/**
	 * Initializes the socket and calls its main method.
	 */
	public void initSocket()
	{
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
	 */
	public void sendDataToServer()
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
	
	/**
	 * Receive data from the server
	 * @throws ClassNotFoundException 
	 */
	public void receiveFromServer () throws ClassNotFoundException
	{
		try 
		{
			// The data sent by server has a serialized object board matrix
			// Estimated size: 3KB
			byte[] dataBuffFromServer = new byte[3072];
			
			// continuously listens to new data on the socket
			while (true )
			{
				// packet that will be sent by server
				DatagramPacket packFromServer = new DatagramPacket(dataBuffFromServer, dataBuffFromServer.length);
				socket.receive(packFromServer);
				
				// interpreting object
				ByteArrayInputStream bis = new ByteArrayInputStream(packFromServer.getData());
				ObjectInput in = null;
				
				try 
				{
					in = new ObjectInputStream(bis);
					Object boardMatrixObject = in.readObject();
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
				
				
			}
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
}
