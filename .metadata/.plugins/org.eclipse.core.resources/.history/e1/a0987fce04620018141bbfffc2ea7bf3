package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;

import io.UserInput;

public class FrmBoard extends JFrame
{
	private static final long serialVersionUID = -206230284397533789L;
	private GridLayout layout;
	private FrmBoardPiece frmBoardPieces[][];
	private int width;
	private int height;
	
	public FrmBoard(int height, int width)
	{
		this(height, width, 10);
	}

	public FrmBoard(int height, int width, int pieceSize)
	{
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(height*pieceSize,width*pieceSize));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addKeyListener(new UserInput());
		this.setFocusable(true);
		
		layout = new GridLayout(height, width);
		this.setLayout(layout);
		
		frmBoardPieces = new FrmBoardPiece[height][width];
		initPieces();
		
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private void initPieces()
	{
		for(int i = 0; i < height; ++i)
		{
			for(int j = 0; j < width; ++j)
			{
				frmBoardPieces[i][j] = new FrmBoardPiece(Color.WHITE);
				this.add(frmBoardPieces[i][j]);
			}
		}
	}
	
	public void setColorAt(int x, int y, Color color)
	{
		System.out.println("frmBoardPieces["+x+"]["+y+"].setColor("+color+")");
		frmBoardPieces[x][y].setColor(color);
	}
	
	public boolean isFocusable ()
	{
		return true;
	}
	
	public void clearBoard ()
	{
		for (int i=0; i < this.height; i++)
		{
			for (int j=0; j < this.width; j++)
			{
				frmBoardPieces[i][j].setColor(Color.WHITE);
			}
		}
	}
}
