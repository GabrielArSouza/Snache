package socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import controller.EnumSnakeDirection;
import controller.Game;
import controller.GameConstants;
import controller.SharedSnakeDirection;
import domain.Snake;
import domain.SnakeConstants;
import domain.SnakePiece;
import presentation.BoardPieceMatrix;
import presentation.FrmBoardPiece;

/**
 * This class implements the server-side of the socket communication between the
 * server and the players. It also converts the commands sent by the users in
 * changes in the game behavior.
 */
public class SocketServerSnake
{
	/** Socket that the server will communicate with the players through. */
	private DatagramSocket socket;

	/** Information about all players currently connected to the game. */
	private Map<InetAddress, ClientInfo> clientInfos;

	/**
	 * Reference to the game object. It's used to change the game behavior based on
	 * the commands the server receives.
	 */
	private Game game;

	private int portClient;
	/**
	 * Instantiates a new server-side communication manager.
	 *
	 * @param game
	 *            the game that the server controls
	 */
	public SocketServerSnake(Game game)
	{
		this.game = game;
		clientInfos = new HashMap<InetAddress, ClientInfo>();
	}

	/**
	 * Initializes the socket that the communication will happen through.
	 */
	public void initSocket()
	{
		Thread boardUpdater = new BoardUpdater();
		boardUpdater.start();

//		Thread updateGameToClients = new UpdateGameToClients();
//		updateGameToClients.start();
		
		try
		{
			socket = new DatagramSocket(SocketConstants.STANDARD_PORT);
			receiveFromClients();
		}

		catch (SocketException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Receive data from the players.
	 */
	public void receiveFromClients()
	{
		try
		{
			// the data sent by the players has only one character: the snake new direction;
			// so the buff has size 1.
			byte[] dataBuffFromClient = new byte[1];

			// continuously listens to new data on the socket
			while(true)
			{
				// packet that will be sent by one player
				DatagramPacket packFromClient = new DatagramPacket(dataBuffFromClient, dataBuffFromClient.length);
				socket.receive(packFromClient);

				// direction sent by the player through the packet
				String snakeDirectionFromClient = new String(packFromClient.getData());

				// player who sent the data
				InetAddress clientIP = packFromClient.getAddress();
				ClientInfo clientInfo = clientInfos.get(clientIP);

				portClient = packFromClient.getPort();
				System.out.println("Received direction from client " + clientIP + ": " + snakeDirectionFromClient);

				// the player wasn't connected to the game: assigns a new snake to him/her
				if(clientInfo == null)
				{
					EnumSnakeDirection directionAsEnum = EnumSnakeDirection.getValue(snakeDirectionFromClient);
					SharedSnakeDirection sharedDirection = new SharedSnakeDirection(directionAsEnum);

					Snake snake = game.createSnake(sharedDirection);

					if(snake != null)
					{
						clientInfo = new ClientInfo(sharedDirection, snake);
						clientInfos.put(clientIP, clientInfo);
					}

					else
					{
						System.out.println("for some reason a snake couldn't be created");
					}
				}

				// player already exists: update the direction of his/her snake if it wasn't
				// updated yet
				else
				{
					clientInfo.updateDirection(snakeDirectionFromClient);
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

	/**
	 * Updates the game's state on a different thread, based on the commands sent by
	 * the players. This update is given every GAME_LATENCY milliseconds.
	 */
	class BoardUpdater extends Thread
	{

		/*
		 * (non-Javadoc)
		 * 
		 * Calls the methods that updates the game state.
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run()
		{
			try
			{
				while(true)
				{
					// the updates occur every GAME_LATENCY milliseconds
					Thread.sleep(GameConstants.GAME_LATENCY);

					update();

					// prints the board on the console
					// TODO remove this call; it's used only for debug purposes
					game.printBoardMatrix();
				}

			}

			catch (InterruptedException e)
			{
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * Updates the game board.
		 * @throws IOException 
		 */
		private void update() throws IOException
		{
			System.out.println("updating snakes...");
			killInactiveClients();

			game.moveSnakes();
			game.drawSnakes();
			
			//Codefy snakes for send to users
			BitSet message = this.codifyMessage();
			
			// sets the "updateDirection" attribute of the players to false so that the next
			// game iteration will consume the commands sent by the players
			for(Map.Entry<InetAddress, ClientInfo> entry : clientInfos.entrySet())
			{
				entry.getValue().setDirectionUpdated(false);

				// builds the package to be sent to the user
				byte[] dataToSend = this.serializeMessage(message, 0);
				
				// wrap the data on board matrix
				DatagramPacket packToSend = new DatagramPacket(dataToSend, dataToSend.length, entry.getKey(),
						portClient);
				
				System.out.println("Sending a pack with " + dataToSend.length
						+ " data bytes to the client " + entry.getKey());

				// sends the packet to the client
				socket.send(packToSend);
				
				System.out.println("Sent data to client");
			}		
		}
		
		/**
		 * Disconnect the inactive clients from the game.
		 */
		private void killInactiveClients()
		{
			// iterator is used so that entries can be removed while iterating the map
			Iterator<Map.Entry<InetAddress, ClientInfo>> entryIterator = clientInfos.entrySet().iterator();

			while(entryIterator.hasNext())
			{
				ClientInfo clientInfo = entryIterator.next().getValue();

				if(!clientInfo.isActive())
				{
					System.out.println("killing an inactive client...");
					game.killInactiveSnake(clientInfo.getSnake());
					entryIterator.remove();
				}
			}
		}
		
		
		private BitSet codifyMessage () throws IOException
		{
			List<Snake> snakes = game.getSnakes();
			
			byte[] snakesPositions = new byte[2* (SnakeConstants.STANDARD_BODY_SIZE+1)  * snakes.size()];
			int cont =0;
			for (Snake s : snakes)
			{
				snakesPositions[cont++] = (byte)s.getHead().getRow();
				snakesPositions[cont++] = (byte)s.getHead().getColumn();
								
				for ( SnakePiece b : s.getBody())
				{
					snakesPositions[cont++] = (byte)b.getRow();
					snakesPositions[cont++] = (byte)b.getColumn();
				}
			}
			
			return BitSet.valueOf(snakesPositions);
		}
		
		
		private byte[] serializeMessage (BitSet message, int posClient)
		{
			
			// each snake piece has 2 bytes (16 bits)
			int bitChange = 16 * (SnakeConstants.STANDARD_BODY_SIZE+1) * posClient;
			message.set(bitChange, true);
			
			byte[] messageSerializated = message.toByteArray();
			message.set(bitChange, false);
			
			return messageSerializated;
		}
	}
}