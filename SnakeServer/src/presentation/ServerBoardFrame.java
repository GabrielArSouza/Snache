package presentation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import domain.Snake;
import domain.SnakePiece;

public class ServerBoardFrame extends JFrame
{
	/** ID for serialization purposes */
	private static final long serialVersionUID = 535815750157046780L;
	
	private BoardCanvas canvas;

	public ServerBoardFrame(int nRows, int nColumns, int squareSize)
	{
		canvas = new BoardCanvas(nRows, nColumns, squareSize);
		JPanel mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(2*nColumns*squareSize, nRows * squareSize));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(canvas);
		mainPanel.add(new ControlPanel(nColumns*squareSize, nRows * squareSize));
		
		super.add(mainPanel);  
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
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
	
	class ControlPanel extends JPanel
	{
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
			this.jTable = new JTable(new ClientTableModel());
			
			lblClients.setAlignmentX(Component.CENTER_ALIGNMENT);
			btnKill.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			add(lblClients);
			add(btnKill);
			add(jTable);
			
		}
		
		public void updateClients()
		{
			
		}
	}
}
