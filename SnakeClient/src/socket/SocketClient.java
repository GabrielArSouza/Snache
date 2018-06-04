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
	
	private void receiveFromServer () throws ClassNotFoundException
	{
		try 
		{
			// The data sent by server has a serialized object board matrix
			// Estimated size: 100 bytes
			byte[] dataBuffFromServer = new byte[100];
			
			// packet that will be sent by server
			DatagramPacket packFromServer = new DatagramPacket(dataBuffFromServer, dataBuffFromServer.length);

			System.out.println("Tentando receber pacote do server...");
			socket.receive(packFromServer);
			
			System.out.println("Recebeu Pacote de " + packFromServer.getLength() + " bytes do server");
			
			this.decodeMessage(packFromServer.getData());
		
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
	
	private void decodeMessage ( byte[] message )
	{
		/**
		 * If color is a background color, so assigned code 00 in message
		 * If color is a user snake, so assigned code 01 in message
		 * If color is other snake, so assigned code 10 in message
		 */	
	
		System.out.println("Decodificando...");
		int contLine = 0;
		int contCol = 0;
		
		for (int i=0; i < message.length; i++)
		{
			int value = message[i];
			String binaryValue = Integer.toBinaryString(value);
			
			char[] bits = binaryValue.toCharArray();
			for (int j=0; j < 8; j+=2)
			{
				char bit1 = bits[j];
				char bit2 = bits[j+1];
			
				if ( bit1 == '0' && bit2=='0')
					boardClient[contLine][contCol].setBackground(Color.WHITE);
				else if (bit1 == '0' && bit2 == '1')
					boardClient[contLine][contCol].setBackground(Color.GREEN);
				else 
					boardClient[contLine][contCol].setBackground(Color.BLACK);
			
				contCol++;
				
				if (contCol > (widthBoard-1) )
				{
					contLine++;
					contCol = 0;
				}
			}
			
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
				// Estimated size: 100 bytes
				byte[] dataBuffFromServer = new byte[100];
				
				// packet that will be sent by server
				DatagramPacket packFromServer = new DatagramPacket(dataBuffFromServer, dataBuffFromServer.length);
				
				System.out.println("Tentando receber pacote do server...");
				socket.receive(packFromServer);
				
				System.out.println("Recebeu Pacote de " + packFromServer.getLength() + " bytes do server");
				this.decodeMessage(packFromServer.getData());
			
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
		
		private void decodeMessage ( byte[] message )
		{
			System.out.println("Decodificando...");
			boolean[] messageDecode = new boolean[message.length * 8];
			int cont =0;
			
			boolean[] array = new boolean[8];
			// Decode message
			for (int i=0; i < message.length; i++)
			{
				// Transform a byte in a boolean array
				for (int j=0; j<8 ;j++){
					//array[j] = (message[i] & (1 << j)) != 0;
					messageDecode[cont] = (message[i] & (1 << j)) != 0;
					cont++;
				}	
	
			}
			
			int markCol = 0;
			int markLine = 0;
			
			char[][] boardCode = new char[heithBoard][widthBoard];
			
			for (int i=0; i < messageDecode.length; i+=2)
			{
				if (messageDecode[i] == false && messageDecode[i+1] == false)
				{
					boardClient[markLine][markCol].setBackground(Color.WHITE);
					boardCode[markLine][markCol] = '.';
				}					
				else if(messageDecode[i] == false && messageDecode[i+1] == true)
				{
					boardClient[markLine][markCol].setBackground(Color.GREEN);
					boardCode[markLine][markCol] = '@';
				}else
				{
					boardClient[markLine][markCol].setBackground(Color.BLACK);
					boardCode[markLine][markCol] = 'O';
				}
				markCol++;
				
				// verify board limits
				if (markCol >= widthBoard)
				{
					markLine++;
					markCol = 0;
				}
			}

			for (int i =0; i< heithBoard; i++)
			{
				String linha = "";
				for (int j=0; j < widthBoard; j++)
				{
					linha+=boardCode[i][j];
				}
				System.out.println(linha);
			}
		}
	}
}
