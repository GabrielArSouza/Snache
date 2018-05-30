package presentation;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

/**
 * The FrmBoardPiece class represents a unique cell in the board view. It
 * extends a JButton but some properties, like focus and borders, are discarded
 * so that the view, in the player's perspective, is just a static filled square.
 */
public class FrmBoardPiece extends JButton
{
	/** Serial ID for serialization purposes. */
	private static final long serialVersionUID = 7706353659170141551L;

	/** Color of the cell. */
	private Color backgroundColor;

	/**
	 * Instantiates a new cell view.
	 *
	 * @param backgroundColor
	 *            color of the cell
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
	 * Changes the color of the cell.
	 *
	 * @param background
	 *            the new color of the cell
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
}
