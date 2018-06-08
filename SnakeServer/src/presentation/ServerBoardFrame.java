package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;

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
		super.add(canvas);  
		super.setLayout(null);  
		this.setPreferredSize(new Dimension(nRows * squareSize, nColumns * squareSize));
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
}
