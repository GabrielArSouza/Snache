package presentation;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

public class BoardPiece extends JButton
{
	private static final long serialVersionUID = 7706353659170141551L;
	private Color backgroundColor;
	
	public BoardPiece(Color backgroundColor) 
	{
        super();
        super.setContentAreaFilled(false);
        this.backgroundColor = backgroundColor;
        setBackground(backgroundColor);
        setBorder(null);
    }
	
	public void setColor(Color background)
	{
		this.backgroundColor = background;
	}
	 
	 @Override
     protected void paintComponent(Graphics g) 
	 {
         g.setColor(backgroundColor);
         g.fillRect(0, 0, getWidth(), getHeight());
         super.paintComponent(g);
     }
}
