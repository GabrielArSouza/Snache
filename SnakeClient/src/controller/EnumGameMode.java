package controller;

/**
 *  
 * Based on: http://mrmcgeek.blogspot.com/2009/06/custom-string-values-for-enum-in-java.html
 */
public enum EnumGameMode
{
	RANDOM ("1"),
	
	PLAYER ("2");
	
	private String mode;
	
	private EnumGameMode(String mode)
	{
		this.mode = mode;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString()
	{
		return mode;
	}
	
	public static EnumGameMode getValue(String stringMode)
	{
		for(EnumGameMode mode : EnumGameMode.values())
		{
			if(mode.toString().equalsIgnoreCase(stringMode))
			{
				return mode;
			}
		}
		
		return null;
	}
}
