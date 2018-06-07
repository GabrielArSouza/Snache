package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;

import io.UserInput;

/**
 * View for showing the entire board.
 */
public class FrmBoard extends JFrame
{
	/** Default color of the board cells. It's used to represent free cells. */
	private static final Color BACK_COLOR = Color.WHITE;

	/** Class ID for serialization purposes. */
	private static final long serialVersionUID = -206230284397533789L;

	/**
	 * The main layout of the view. GridLayout is used because the board is
	 * matrix-like
	 */
	private GridLayout layout;

	/** Matrix of positions. */
	private FrmBoardPiece frmBoardPieces[][];

	/** Width of the board. */
	private int width;

	/** Height of the board. */
	private int height;

	/**
	 * Instantiates a new view object using the default value for the size of the
	 * pieces
	 *
	 * @param height
	 *            the height
	 * @param width
	 *            the width
	 */
	public FrmBoard(int height, int width)
	{
		this(height, width, 7);
	}

	/**
	 * Instantiates a new view object.
	 *
	 * @param height
	 *            height of the board
	 * @param width
	 *            width of the board
	 * @param pieceSize
	 *            size (in pixels) of each little square in the board
	 */
	public FrmBoard(int height, int width, int pieceSize)
	{
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(height * pieceSize, width * pieceSize));
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

	/**
	 * Initializes the cells of the board. It creates each cell and add them to the
	 * main view.
	 */
	private void initPieces()
	{
		for(int i = 0; i < height; ++i)
		{
			for(int j = 0; j < width; ++j)
			{
				frmBoardPieces[i][j] = new FrmBoardPiece(BACK_COLOR);
				this.add(frmBoardPieces[i][j]);
			}
		}
	}

	/**
	 * Changes the color of a specific cell.
	 *
	 * @param row
	 *            the row of the cell to be changed
	 * @param column
	 *            the column of the cell to be changed
	 * @param color
	 *            the new color of the cell
	 */
	public void setColorAt(int row, int column, Color color)
	{
		frmBoardPieces[row][column].setColor(color);
		System.out.println("frmBoardPieces["+row+"]["+column+"].setColor("+color+")");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * Makes this frame focusable so that key listeners can be attached to it.
	 * 
	 * @see java.awt.Component#isFocusable()
	 */
	public boolean isFocusable()
	{
		return true;
	}

	/**
	 * Sets the color of all cells to the default one.
	 */
	public void clearBoard()
	{
		for(int i = 0; i < this.height; i++)
		{
			for(int j = 0; j < this.width; j++)
			{
				frmBoardPieces[i][j].setColor(BACK_COLOR);
			}
		}
	}

	public FrmBoardPiece[][] getFrmBoardPieces() {
		return frmBoardPieces;
	}

	public void setFrmBoardPieces(FrmBoardPiece[][] frmBoardPieces) {
		this.frmBoardPieces = frmBoardPieces;
	}
	
	
}
