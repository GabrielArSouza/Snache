package presentation;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

import javax.swing.JButton;

import controller.GameConstants;

/**
 * This class represents a unique cell in the board UI. It is a JButton that
 * doesn't change its layout on player events (like click and focus). To the
 * player, it looks like a simple filled square. It implements the Serializable
 * interface since it will be passed as a stream through socket.
 */
public class FrmBoardPiece extends JButton implements Serializable
{
	/** Serial ID for serialization purposes. */
	private static final long serialVersionUID = 7706353659170141551L;

	/** The static color of the piece. */
	private transient Color backgroundColor;

	/**
	 * Instantiates a new piece.
	 *
	 * @param backgroundColor
	 *            the background color
	 */
	public FrmBoardPiece(Color backgroundColor)
	{
		super();
		super.setContentAreaFilled(false);
		this.backgroundColor = backgroundColor;
		setBackground(backgroundColor);
		setBorder(null);
	}

	/**
	 * Sets the color of the piece.
	 *
	 * @param background
	 *            the new color of the piece
	 */
	public void setColor(Color background)
	{
		this.backgroundColor = background;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(backgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * The string representation of a board piece UI is a "." if it has the
	 * background standard color, and "@" otherwise.
	 * 
	 * @see java.awt.Component#toString()
	 */
	@Override
	public String toString()
	{
		// the cell is empty
		if(backgroundColor == GameConstants.BACK_COLOR)
		{
			return ".";
		}

		// the cell isn't empty; it contains a snake piece
		else
		{
			return "@";
		}
	}
}
