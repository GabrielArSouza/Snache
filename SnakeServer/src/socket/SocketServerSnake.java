package socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import controller.EnumSnakeDirection;
import controller.Game;
import controller.SharedSnakeDirection;

public class SocketServerSnake
{
	private DatagramSocket socket;
	private Map<InetAddress, ClientInfo> clientInfos;
	private Game game;
	
	public SocketServerSnake(Game game)
	{
		this.game = game;
		clientInfos = new HashMap<InetAddress, ClientInfo>();
	}
	
	public void init()
	{
		try
		{
			socket = new DatagramSocket(6666);
			receiveFromClients();
		} 
		
		catch (SocketException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void update()
	{
		
	}

	public void receiveFromClients() 
	{
		try 
		{
			// TODO change buff size
			byte[] dataBuffFromClient = new byte[1024];

			while (true) 
			{
				DatagramPacket packFromClient = new DatagramPacket(dataBuffFromClient, dataBuffFromClient.length);
				socket.receive(packFromClient);
				String snakeDirectionFromClient = new String(packFromClient.getData());
				
				InetAddress clientIP = packFromClient.getAddress();
				ClientInfo clientInfo = clientInfos.get(clientIP);
				
				System.out.println("Received direction from client" + clientIP+ ": " + snakeDirectionFromClient);
				
				// new player
				if(clientInfo == null)
				{
					EnumSnakeDirection directionAsEnum = EnumSnakeDirection.getValue(snakeDirectionFromClient);
					SharedSnakeDirection sharedDirection = new SharedSnakeDirection(directionAsEnum);
					
					if(game.createSnake(sharedDirection))
					{
						clientInfo = new ClientInfo(sharedDirection);
						clientInfos.put(clientIP, clientInfo);
					}
					
					else
					{
						System.out.println("for some reason a snake couldn't be created");
					}
				}
				
				// player already exists: update the direction of his/her snake
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
}