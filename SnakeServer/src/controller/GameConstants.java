package controller;

import java.awt.Color;

/**
 * Constants to be used in the game.
 */
public class GameConstants
{
	/** Time between two updates of the board. */
	public static final int GAME_LATENCY = 200;

	/** Color to be used in the board background */
	public static final Color BACKGROUND_COLOR = Color.WHITE;

	/** Max connections */
	public static final int MAX_NUMBER_SNAKES = 10;

	/** All colors that are used in the snakes in the server view. */
	public static final Color[] ALL_COLORS = new Color[] { Color.RED, Color.BLUE, Color.YELLOW,
			Color.MAGENTA, Color.LIGHT_GRAY, Color.ORANGE, Color.CYAN};
}
