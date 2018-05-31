package socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import controller.SingletonSnakeDirectionChange;

public class SocketClient
{
	private DatagramSocket socket;
	private SingletonSnakeDirectionChange snakeDirection = SingletonSnakeDirectionChange.getInstance();
	
	public void init()
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
	
	public void sendDataToServer()
	{
		String direction;
		
		try
		{
			InetAddress ip = InetAddress.getByName("localhost");
			
			while(true)
			{
				direction = snakeDirection.consume().toString();
				System.out.println("direction sent to server: " + direction);
				
				byte[] dataToSend = direction.getBytes();
				DatagramPacket packToSend = new DatagramPacket(dataToSend, dataToSend.length, ip, 6666);
				socket.send(packToSend);
				
				Thread.sleep(2000);
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
}
