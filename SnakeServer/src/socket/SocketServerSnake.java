package socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
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

// TODO: Auto-generated Javadoc
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

		// Thread updateGameToClients = new UpdateGameToClients();
		// updateGameToClients.start();

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
			// the data sent by the players has only one character (the snake new direction)
			// or zero (the package for connection request), so the buffer has 1 as max size
			byte[] dataBuffFromClient = new byte[1];

			// continuously listens to new data on the socket
			while(true)
			{
				System.out.println("-----------------------------------------------------");
				System.out.println("while(true) on receiveFromClients()");

				// packet that will be received from a player
				DatagramPacket packFromClient = new DatagramPacket(dataBuffFromClient, dataBuffFromClient.length);
				socket.receive(packFromClient);

				// player who sent the data
				InetAddress clientIP = packFromClient.getAddress();

				// new client trying to connect
				if(packFromClient.getLength() == 0)
				{
					System.out.println("client " + clientIP + " trying to connect");
					SharedSnakeDirection sharedDirection = new SharedSnakeDirection(EnumSnakeDirection.SAME);

					Snake snake = game.createSnake(sharedDirection);

					if(snake != null)
					{
						ClientInfo clientInfo = new ClientInfo(sharedDirection, snake, packFromClient.getPort());
						clientInfos.put(clientIP, clientInfo);
					}

					else
					{
						System.out.println("for some reason a snake couldn't be created");
					}
				}

				// the client is in the game already
				else
				{
					// data sent by the player
					byte[] dataFromClient = packFromClient.getData();

					// direction sent by the player through the socket
					String snakeDirectionFromClient = new String(dataFromClient);
					System.out.println("Received direction from client " + clientIP + ": " + snakeDirectionFromClient);

					ClientInfo clientInfo = clientInfos.get(clientIP);
					clientInfo.updateDirection(snakeDirectionFromClient);
				}

				System.out.println("endwhile receive()");
				System.out.println("-----------------------------------------------------");
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
					System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
					System.out.println("while(true) on update");
					// the updates occur every GAME_LATENCY milliseconds
					Thread.sleep(GameConstants.GAME_LATENCY);

					update();

					// prints the board on the console
					// TODO remove this call; it's used only for debug purposes
					// game.printBoardMatrix();
					System.out.println("endwhile update()");
					System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");

				}

			}

			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * Updates the game board.
		 *
		 * @throws IOException
		 *             Signals that an I/O exception has occurred.
		 */
		private void update() throws IOException
		{
			System.out.println("updating snakes...");
			killInactiveClients();

			// moves and draws the snakes
			Map<Snake, Integer> snakePositions = game.moveSnakes();
			game.drawSnakes();

			// Message to be sent to the players. The snakes in this message are in the
			// *same* order of the snake list in the Game object
			BitSet message = this.snakeListToBitSet();

			// Iterates over the players and send the board to them.
			Iterator<Map.Entry<InetAddress, ClientInfo>> entryIterator = clientInfos.entrySet().iterator();
			
			while(entryIterator.hasNext())
			{
				Map.Entry<InetAddress, ClientInfo> entry = entryIterator.next();
				ClientInfo client = entry.getValue();
				Snake snake = client.getSnake();

				// sets the "updateDirection" attribute of the player to false so that the next
				// game iteration will consume the commands sent by him/her
				client.setDirectionUpdated(false);

				// position of the client's snake in the snake list of the game
				Object posOfClientSnake = snakePositions.get(snake);

				// client's snake has died
				if(posOfClientSnake == null)
				{
					entryIterator.remove();
					continue;
				}

				// builds the byte array that will be sent through socket
				byte[] dataToSend = this.serializeBitSet(message, (int) posOfClientSnake);

				// builds the package to be sent to the user
				DatagramPacket packToSend = new DatagramPacket(dataToSend, dataToSend.length, entry.getKey(),
						client.getPort());

				System.out.println("Sending a pack with " + dataToSend.length + " data bytes to the client "
						+ entry.getKey() + " on the port " + client.getPort());

				// sends the packet to the client
				socket.send(packToSend);

				System.out.println("the data was (supposely succesfully) sent to the client");
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

		/**
		 * Encapsulates *all* the snakes in a single BitSet object. For each snake, all
		 * of its SnakePiece components are converted to two bytes, one for each
		 * coordinate (row and column). Then these bytes are added at the end of the
		 * BitSet.
		 *
		 * @return BitSet object containing all of the snakes
		 */
		private BitSet snakeListToBitSet()
		{
			List<Snake> snakes = game.getSnakes();

			byte[] snakesPositions = new byte[2 * (SnakeConstants.STANDARD_BODY_SIZE + 1) * snakes.size()];
			int cont = 0;
			for(Snake s : snakes)
			{
				snakesPositions[cont++] = (byte) s.getHead().getRow();
				snakesPositions[cont++] = (byte) s.getHead().getColumn();

				for(SnakePiece b : s.getBody())
				{
					snakesPositions[cont++] = (byte) b.getRow();
					snakesPositions[cont++] = (byte) b.getColumn();
				}
			}

			return BitSet.valueOf(snakesPositions);
		}

		/**
		 * Builds the byte array to be sent to a single client through socket.
		 * 
		 * For each snake, its leftmost bit in the BitSet will be the leftmost bit of
		 * the row of its head. Since the board is 100x100, this bit is, at first, zero
		 * for all the snakes because of the two-complement representation. It will be
		 * set to 1 *only* to the current client's snake, so that the client's snake
		 * differs from the others.
		 *
		 * @param message
		 *            BitSet containing all of the snakes
		 * @param posClient
		 *            The position of the client's snake in the BitSet snakes order
		 * @return the byte array that will be sent to that client
		 */
		private byte[] serializeBitSet(BitSet message, int posClient)
		{
			// each snake piece has 2 bytes (16 bits)
			// MSB is a right
			int bitChange = 16 * (SnakeConstants.STANDARD_BODY_SIZE + 1) * posClient + 7;

			// sets the differing bit of the snake of this specific client
			message.set(bitChange, true);

			byte[] messageSerializated = message.toByteArray();

			// The differing bit is set only in the coding of the BitSet to a byte array.
			// After that, it must be re-set to 0.
			message.set(bitChange, false);

			return messageSerializated;
		}
	}
}