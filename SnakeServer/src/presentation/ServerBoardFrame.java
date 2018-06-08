package presentation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import domain.Snake;
import domain.SnakePiece;
import socket.SocketServerSnake;

public class ServerBoardFrame extends JFrame
{
	/** ID for serialization purposes */
	private static final long serialVersionUID = 535815750157046780L;
	
	private BoardCanvas canvas;
	private Map<InetAddress, Color> clientColors;
	private SortedSet<InetAddress> clients;
	private AbstractTableModel tableModel;
	private SocketServerSnake socketServer;

	public ServerBoardFrame(int nRows, int nColumns, int squareSize)
	{
		canvas = new BoardCanvas(nRows, nColumns, squareSize);
		JPanel mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(2*nColumns*squareSize, nRows * squareSize));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(canvas);
		
		clientColors = new HashMap<InetAddress, Color>();
		
		clients = new TreeSet<InetAddress>(new Comparator<InetAddress>() 
		{
			@Override
			public int compare(InetAddress o1, InetAddress o2)
			{
				return o1.getHostAddress().compareTo(o2.getHostAddress());
			}
		});		
		tableModel = new ClientTableModel(clients);
		
		mainPanel.add(new ControlPanel(nColumns*squareSize, nRows * squareSize));
		
		super.add(mainPanel);  
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	public void setSocketServer(SocketServerSnake socketServer)
	{
		this.socketServer = socketServer;
	}
	
	public void drawSnake(Snake snake, Color color)
	{
		SnakePiece head = snake.getHead();
		List<SnakePiece> body = snake.getBody();
		
		this.setColorAt(head.getRow(), head.getColumn(), color);
		
		for(SnakePiece bodyPiece : body)
			this.setColorAt(bodyPiece.getRow(), bodyPiece.getColumn(), color);
	}
	
	public void setColorAt(int row, int column, Color color)
	{
		canvas.setColorAt(row, column, color);
	}
	
	public void repaintCanvas()
	{
		canvas.repaint();
	}

	public void clearBoard()
	{
		canvas.clearCanvas();
	}
	
	public void addClient(InetAddress ip, Color color)
	{
		clients.add(ip);
		clientColors.put(ip, color);
		
		int pos = clients.headSet(ip).size();
		tableModel.fireTableRowsInserted(pos, pos);
	}
	
	public void removeClient(InetAddress ip)
	{
		clientColors.remove(ip);
		clients.remove(ip);
		tableModel.fireTableDataChanged();
	}
	
	class ControlPanel extends JPanel
	{
		private static final long serialVersionUID = 8802241510756079347L;
		private JLabel lblClients;
		private JButton btnKill;
		private JTable jTable;
		
		public ControlPanel(int width, int height)
		{
			super();
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setPreferredSize(new Dimension(width, height));
			initComponents();
		}
		
		private void initComponents()
		{
			this.lblClients = new JLabel("Players currently connected to the game");
			this.btnKill = new JButton("Kill selected player");
			this.jTable = new JTable(tableModel)
			{
				private static final long serialVersionUID = -4810838642787505331L;

				@Override
			    public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
					Component comp = super.prepareRenderer(renderer, row, col);
					System.out.println("color " + clientColors.get((InetAddress) getModel().getValueAt(row, col)));
					comp.setBackground( clientColors.get((InetAddress) getModel().getValueAt(row, col)));
					
			        return comp;
			    }
			};
			
			lblClients.setAlignmentX(Component.CENTER_ALIGNMENT);
			btnKill.setAlignmentX(Component.CENTER_ALIGNMENT);
			btnKill.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e)
				{
					int row = jTable.getSelectedRow();
					
					if(row != -1)
					{
						InetAddress ip = null;
						Iterator<InetAddress> it = clients.iterator();
						
						for(int i = 0; i <= row; ++i) ip = it.next();
						
						socketServer.killSnake(ip);
					}
					
				}
			});
			
			add(lblClients);
			add(btnKill);
			add(jTable);
		}
	}
}
