package socket;

import java.io.Serializable;

/**
 * A board peace, represents a color with
 * 2 bits. Four possibilities are available
 * @author gabriel
 *
 */
public class PieceCodMessage implements Serializable
{
	private boolean bit1;
	private boolean bit2;
	
	/**
	 * Basic constructor
	 */
	public PieceCodMessage ()
	{
		this.bit1 = false;
		this.bit2 = true;
	}
	
	/**
	 * Normal construct
	 */
	public PieceCodMessage (boolean bit1, boolean bit2)
	{
		this.bit1 = bit1;
		this.bit2 = bit2;
	}
	
	/**
	 * Getters and Setters Methods 
	 */
	
	public boolean isBit1() {
		return bit1;
	}
	public void setBit1(boolean bit1) {
		this.bit1 = bit1;
	}
	public boolean isBit2() {
		return bit2;
	}
	public void setBit2(boolean bit2) {
		this.bit2 = bit2;
	}
	
	
}
