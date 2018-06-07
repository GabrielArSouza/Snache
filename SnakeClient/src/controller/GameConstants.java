package controller;

import java.awt.Color;

/**
 * Constants to be used in the game.
 */
public class GameConstants
{
	/** Time between two updates of the board. */
	public static final int GAME_LATENCY = 200;
	
	/** Max connections */
	public static final int MAX_NUMBER_SNAKES = 10;
	
	/** Client snake color */
	public static final Color CLIENT_SNAKE_COLOR = Color.GREEN;
	
	/** Enemy snake color */
	public static final Color ENEMY_SNAKE_COLOR = Color.BLACK;
	
	public static final Color BACKGROUND_COLOR = Color.WHITE;
}
