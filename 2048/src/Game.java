import exceptions.*;

public interface Game {
	
	public int[][] getTable();
	
	public void newMove(char direction) throws GameLostException, PointlessMovementException;
}
