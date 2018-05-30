package socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SocketClient
{
	private DatagramSocket socket;
	private String direction;
	
	public SocketClient()
	{
		direction = "";
		initSocket();
		sendToServer();
	}
	
	public void setDirection(String direction)
	{
		this.direction = direction;
	}
	
	private void initSocket()
	{
		try
		{
			socket = new DatagramSocket();
		} 
		
		catch (SocketException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendToServer()
	{
		try
		{
			InetAddress ip;
			
			while(true)
			{
				ip = InetAddress.getByName("localhost");
				
				byte[] dataToSend = direction.getBytes();
				DatagramPacket packToSend = new DatagramPacket(dataToSend, dataToSend.length, ip, 66666);
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
