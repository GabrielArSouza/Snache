package presentation;

import java.awt.Color;

import javax.swing.JFrame;

import io.UserInput;

public class BoardFrame extends JFrame
{
	/** ID for serialization purposes */
	private static final long serialVersionUID = 535815750157046780L;
	
	private BoardCanvas canvas;

	public BoardFrame()
	{
		canvas = new BoardCanvas(50, 50, 10);
		super.add(canvas);  
		super.setLayout(null);  
		super.setSize(500, 500);  
		this.addKeyListener(new UserInput());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setFocusable(true);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	public void setColorAt(int row, int column, Color color)
	{
		canvas.setColorAt(row, column, color);
	}
	
	public void repaintCanvas()
	{
		canvas.repaint();
	}
	
	public boolean isFocusable()
	{
		return true;
	}
	
	
}
