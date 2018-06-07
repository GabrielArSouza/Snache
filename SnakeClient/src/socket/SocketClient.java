package socket;

import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import controller.GameConstants;
import controller.SingletonSnakeDirectionChange;
import domain.SnakeConstants;
import presentation.BoardFrame;

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

	private BoardFrame frmClient;

	private InetAddress serverIp;

	public SocketClient(BoardFrame frmClient)
	{
		this.frmClient = frmClient;
	}

	/**
	 * Initializes the socket and calls its main method.
	 * 
	 * @throws ClassNotFoundException
	 */
	public void init()
	{
		try
		{
			socket = new DatagramSocket();

			connectToServer();
			run();
		}

		catch (SocketException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void connectToServer()
	{
		try
		{
			serverIp = InetAddress.getByName("localhost");

			byte[] dataToSend = new byte[0];

			DatagramPacket packToSend = new DatagramPacket(dataToSend, 0, serverIp,
					SocketConstants.STANDARD_PORT);

			socket.send(packToSend);
		}

		catch (UnknownHostException e)
		{
			System.out.println("NAO ACHOU O IP");
			e.printStackTrace();
		}

		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void run()
	{
		while(true)
		{
			if(!receiveDataFromServer())
				break;

			sendDataToServer();
		}
	}

	/**
	 * Sends data to the server. Every "GAME_LATENCY" milliseconds it sends a snake
	 * direction change to the server. It doesn't need to send a dummy package to
	 * request connection, so the first package is also a snake direction. The
	 * server is responsible for identifying that the player is, in practice,
	 * requesting a connection.
	 * 
	 * @throws ClassNotFoundException
	 */
	private void sendDataToServer()
	{
		try
		{
			// catches the direction change from the singleton
			String direction = snakeDirection.consume().toString();

			System.out.println("direction sent to server: " + direction);

			// builds the package to be sent to the server
			byte[] dataToSend = direction.getBytes();
			DatagramPacket packToSend = new DatagramPacket(dataToSend, dataToSend.length, serverIp,
					SocketConstants.STANDARD_PORT);

			// sends the packet
			socket.send(packToSend);
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
	}

	private boolean receiveDataFromServer()
	{
		try
		{
			// The data sent by server has a serialized object board matrix
			// Estimated size
			int maxSizeMessage = 2 * (SnakeConstants.STANDARD_BODY_SIZE + 1) * GameConstants.MAX_NUMBER_SNAKES;

			byte[] dataBuffFromServer = new byte[maxSizeMessage];

			// packet that will be sent by server
			DatagramPacket packFromServer = new DatagramPacket(dataBuffFromServer, dataBuffFromServer.length);

			System.out.println("Tentando receber pacote do server...");
			socket.receive(packFromServer);

			System.out.println("Recebeu Pacote de " + packFromServer.getLength() + " bytes do server");

			long startTime = System.nanoTime();

			// kill signal!
			if(packFromServer.getData().equals("K"))
			{
				return false;
			}

			// decodes the message sent by the server and updates the board
			this.decodeMessageAndUpdateBoard(packFromServer.getData(), packFromServer.getLength());

			System.out.println("ellapsed time (ms): " + (System.nanoTime() - startTime) / 1000000);

			return true;

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

		return false;
	}

	private void decodeMessageAndUpdateBoard(byte[] message, int realSize)
	{
		System.out.println("Decodificando...");
		frmClient.clearBoard();

		int posColumn = 0;
		int posRow = 0;

		/**
		 * This message contains all game's snakes Each byte is a snake position A snake
		 * contains 20 bytes (160 bits) The snake that start in bit '1' is a client's
		 * snake
		 */
		boolean flagIsClientSnake = false;

		Color colorInPieceBord;

		for(int i = 0; i < realSize; i++)
		{
			// For each snake, verify that it belongs to the client
			if(i % 20 == 0)
			{
				// verify if first bit is setting in 1
				if((message[i] & (1 << 7)) != 0)
				{
					flagIsClientSnake = true;
					// unset a MSB
					message[i] &= ((1 << 7) - 1);

				} else
					flagIsClientSnake = false;
			}

			// Define snake's color
			if(flagIsClientSnake)
				colorInPieceBord = GameConstants.CLIENT_SNAKE_COLOR;
			else
				colorInPieceBord = GameConstants.ENEMY_SNAKE_COLOR;

			if(i % 2 == 0)
			{
				// A number between 0 and 127
				posRow = Byte.toUnsignedInt(message[i]);
			} else
			{
				// A number between 0 and 127
				posColumn = Byte.toUnsignedInt(message[i]);
				frmClient.setColorAt(posRow, posColumn, colorInPieceBord);
				System.out.println(posRow + " " + posColumn);

			}
		}

		frmClient.repaintCanvas();

	}
}