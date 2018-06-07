package presentation;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import io.UserInput;

public class BoardFrame extends JFrame
{
	/** ID for serialization purposes */
	private static final long serialVersionUID = 535815750157046780L;
	
	private BoardCanvas canvas;

	public BoardFrame(int nRows, int nColumns, int squareSize)
	{
		canvas = new BoardCanvas(nRows, nColumns, squareSize);
		super.add(canvas);  
		super.setLayout(null);  
		this.setPreferredSize(new Dimension(nRows * squareSize, nColumns * squareSize));
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
	
	public void clearBoard()
	{
		canvas.clearCanvas();
	}
}
