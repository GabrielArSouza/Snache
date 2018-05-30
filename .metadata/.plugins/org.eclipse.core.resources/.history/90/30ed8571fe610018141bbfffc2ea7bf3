package controller;

public class SingletonDir {

	private static final SingletonDir instance = new SingletonDir();
	private EnumSnakeDirection direction;
	
	private SingletonDir ()
	{
		this.direction = null;
	}
	
	public static SingletonDir getInstance()
	{
		return instance;
	}
	
	/**
	 * Consome o comando de direção
	 * @return o último comando de direção informado
	 */
	public EnumSnakeDirection getDirection ()
	{
		EnumSnakeDirection temp = direction;
		direction = null;
		return temp;
	}
	
	/**
	 * Atualiza direção informada pelo usuário
	 * Só atualiza se a última direção atualizada tiver
	 * sido consumida.
	 * @param direction Nova direção
	 */
	public void setDirection (EnumSnakeDirection direction)
	{
		if (this.direction == null)
			this.direction = direction;
		else 
		{
			//Ignore
		}
	}
}
