package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;

public class FrmBoard extends JFrame
{
	private static final long serialVersionUID = -206230284397533789L;
	private GridLayout layout;
	private Piece pieces[][];
	private int width;
	private int height;
	
	public FrmBoard(int width, int height)
	{
		this(width, height, 30);
	}

	public FrmBoard(int width, int height, int pieceSize)
	{
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(width*pieceSize,height*pieceSize));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		layout = new GridLayout(width, height);
		this.setLayout(layout);
		
		pieces = new Piece[width][height];
		initPieces();
		
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private void initPieces()
	{
		for(int i = 0; i < width; ++i)
		{
			for(int j = 0; j < height; ++j)
			{
				pieces[i][j] = new Piece(Color.WHITE);
				this.add(pieces[i][j]);
			}
		}
		
		pieces[5][5].setColor(Color.RED);
	}
}
