package socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import controller.EnumSnakeDirection;
import controller.Game;
import controller.GameConstants;
import controller.SharedSnakeDirection;
import domain.Snake;
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

		Thread updateGameToClients = new UpdateGameToClients();
		updateGameToClients.start();
		
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
			
			// sets the "updateDirection" attribute of the players to false so that the next
			// game iteration will consume the commands sent by the players
			for(Map.Entry<InetAddress, ClientInfo> entry : clientInfos.entrySet())
			{
				entry.getValue().setDirectionUpdated(false);
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
	}
	
	class UpdateGameToClients extends Thread
	{
	
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
		
		private void update () throws IOException
		{
			for(Map.Entry<InetAddress, ClientInfo> entry : clientInfos.entrySet())
			{
				// builds the package to be sent to the user
				byte[] dataToSend = this.codifyMessage();
				
				// wrap the data on board matrix
				DatagramPacket packToSend = new DatagramPacket(dataToSend, dataToSend.length, entry.getKey(),
						SocketConstants.STANDARD_PORT);
				
				System.out.println("Eviando um pacote com " + dataToSend.length
						+ " bytes de dados para o cliente " + entry.getKey());
				// sends the packet to the client
				socket.send(packToSend);
			}		
		}
		
		private byte[] codifyMessage () throws IOException
		{
			// original data board
			BoardPieceMatrix boardGame = game.getBoardMatrix();
			
			// board matrix game
			FrmBoardPiece boardMessage[][] = boardGame.getMatrix();
			
			// message to sent to the user
			PieceCodMessage boardCodefy[][] = 
					new PieceCodMessage[boardGame.getHeight()][boardGame.getWidth()];
			
			/**
			 * If color is a background color, so assigned code 00 in message
			 * If color is a user snake, so assigned code 01 in message
			 * If color is other snake, so assigned code 10 in message
			 */
			
			for (int i=0; i < boardGame.getHeight(); i++)
			{
				for (int j=0; j < boardGame.getWidth(); j++)
				{
					// backgroud color
					if ( boardMessage[i][j].getBackgroundColor() == GameConstants.BACK_COLOR )
						boardCodefy[i][j] = new PieceCodMessage(false, false);
					// snake user color
					else boardCodefy[i][j] = new PieceCodMessage(false, true);
				}
			}
		
			return this.serializeBoardMatrix(boardCodefy);
		}
		
		/**
		 * Serializes the object board matrix 
		 * @return a byte array containing a board matrix serialization
		 * @throws IOException 
		 * 
		 * Based in:
		 * https://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array
		 */
		private byte[] serializeBoardMatrix (PieceCodMessage boardCodefy[][]) throws IOException
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput out = null;
			byte[] boardMatrix;
			
			try {
				out = new ObjectOutputStream(bos);   
				out.writeObject(boardCodefy);
				out.flush();
				boardMatrix = bos.toByteArray();
			} 
			finally {
				try {
					bos.close();
				} catch (IOException ex) {
					// ignore close exception
				}
			}
			return boardMatrix;
		}
	}
}