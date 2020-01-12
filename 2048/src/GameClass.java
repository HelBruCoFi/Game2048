import java.util.concurrent.ThreadLocalRandom;

import exceptions.*;

public class GameClass implements Game {

	private int[][] table;
	private int size;
	private int emptyCells;

	public GameClass(int size) {
		this.size = size;
		emptyCells = size * size;
		table = new int[size][size];
		generateTable(size);
	}

	@Override
	public int[][] getTable() {
		return table;
	}

	@Override
	public void newMove(char direction) throws GameLostException, PointlessMovementException {
		int[][] newTable = null;
		switch (direction) {
		case 'W':
			newTable = moveUp(table);
			break;
		case 'A':
			newTable = moveLeft(table);
			break;
		case 'S':
			newTable = moveDown(table);
			break;
		case 'D':
			newTable = moveRight(table);
			break;
		}
		table = newTable;
		fillEmptyWith2or4();

		if (gameIsLost())
			throw new GameLostException();
	}

	private void generateTable(int size) {
		fillEmptyWith2or4();
		fillEmptyWith2or4();
	}

	private boolean gameIsLost() {
		if (emptyCells != 0)
			return false;

		for (int i = 0; i < size - 1; i++)
			for (int j = 0; j < size - 1; j++)
				if (table[i][j] == table[i + 1][j] || table[i][j] == table[i][j + 1])
					return false;

		return true;
	}

	private void fillEmptyWith2or4() {
		int countEmpty = 0;
		int cellToFill = ThreadLocalRandom.current().nextInt(1, emptyCells + 1);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (table[i][j] == 0) {
					countEmpty++;
					if (countEmpty == cellToFill) {
						table[i][j] = add2or4();
						System.out.printf("filled (%d,%d) with %d\n", i, j, table[i][j]);
						i = size;
						break;
					}
				}
			}
		}
		emptyCells--;
	}

	/**
	 * Adds a 2 or 4 in a empty cell of the table. A 2 is generated with 75% chance.
	 * and a 4 with 25% chance.
	 * 
	 * @return 2 or 4
	 */
	private int add2or4() {
		double r = Math.random();
		if (r < 0.75)
			return 2;
		else
			return 4;
	}

	/**
	 * Moves all the pieces up and sum all the common 2 pieces, one above the other
	 * without others in between
	 * 
	 * @param table
	 * @return
	 * @throws PointlessMovementException
	 */
	private int[][] moveUp(int[][] table) throws PointlessMovementException {
		boolean hadEffect = false;

		for (int j = 0; j < size; j++) {
			// move all non zero cells up
			for (int i = 0; i < size - 1; i++) {
				if (table[i][j] == 0) {
					int k = i;
					boolean hasNumbers = false;
					while (table[i][j] == 0 && k < size - 1) {
						// if number, move all one cell up
						if (table[k + 1][j] != 0) {
							hasNumbers = true;
							table[k][j] = table[k + 1][j];
							table[k + 1][j] = 0;
							hadEffect = true;
						}
						k++;
						// repeat until the top position i is filled
						if (k == size - 1 && hasNumbers)
							k = i;
					}
				}
			}
			// sum common cells
			for (int i = 0; i < size - 1; i++) {
				if (table[i][j] != 0 && table[i][j] == table[i + 1][j]) {
					table[i][j] *= 2;
					table[i + 1][j] = 0;
					emptyCells++;
					for (int k = i + 1; k < size - 1; k++) { // move the rest one cell up
						table[k][j] = table[k + 1][j];
						table[k + 1][j] = 0;
					}
					hadEffect = true;
				}
			}
		}

		if (!hadEffect)
			throw new PointlessMovementException();
		return table;
	}

	private int[][] moveLeft(int[][] table) throws PointlessMovementException {
		boolean hadEffect = false;

		for (int i = 0; i < size; i++) {
			// move all non zero cells left
			for (int j = 0; j < size - 1; j++) {
				if (table[i][j] == 0) {
					int k = j;
					boolean hasNumbers = false;
					while (table[i][j] == 0 && k < size - 1) {
						// if number, move all one cell to the left
						if (table[i][k + 1] != 0) {
							hasNumbers = true;
							table[i][k] = table[i][k + 1];
							table[i][k + 1] = 0;
							hadEffect = true;
						}
						k++;
						// repeat until the leftmost position j is filled
						if (k == size - 1 && hasNumbers)
							k = j;
					}
				}
			}
			// sum common cells
			for (int j = 0; j < size - 1; j++) {
				if (table[i][j] != 0 && table[i][j] == table[i][j + 1]) {
					table[i][j] *= 2;
					table[i][j + 1] = 0;
					emptyCells++;
					for (int k = j + 1; k < size - 1; k++) { // move the rest one cell to the left
						table[i][k] = table[i][k + 1];
						table[i][k + 1] = 0;
					}
					hadEffect = true;
				}
			}
		}

		if (!hadEffect)
			throw new PointlessMovementException();
		return table;
	}

	private int[][] moveDown(int[][] table) throws PointlessMovementException {
		boolean hadEffect = false;

		for (int j = 0; j < size; j++) {
			// move all non zero cells down
			for (int i = size - 1; i > 0; i--) {
				if (table[i][j] == 0) {
					int k = i;
					boolean hasNumbers = false;
					while (table[i][j] == 0 && k > 0) {
						// if number, move all one cell down
						if (table[k - 1][j] != 0) {
							hasNumbers = true;
							table[k][j] = table[k - 1][j];
							table[k - 1][j] = 0;
							hadEffect = true;
						}
						k--;
						// repeat until the bottom position i is filled
						if (k == 0 && hasNumbers)
							k = i;
					}
				}
			}
			// sum common cells
			for (int i = size - 1; i > 0; i--) {
				if (table[i][j] != 0 && table[i][j] == table[i - 1][j]) {
					table[i][j] *= 2;
					table[i - 1][j] = 0;
					emptyCells++;
					for (int k = i - 1; k > 0; k--) { // move the rest one cell down
						table[k][j] = table[k - 1][j];
						table[k - 1][j] = 0;
					}
					hadEffect = true;
				}
			}
		}

		if (!hadEffect)
			throw new PointlessMovementException();
		return table;
	}

	private int[][] moveRight(int[][] table) throws PointlessMovementException {
		boolean hadEffect = false;

		for (int i = 0; i < size; i++) {
			// move all non zero cells right
			for (int j = size - 1; j > 0; j--) {
				if (table[i][j] == 0) {
					int k = j;
					boolean hasNumbers = false;
					while (table[i][j] == 0 && k > 0) {
						// if number, move all one cell to the right
						if (table[i][k - 1] != 0) {
							hasNumbers = true;
							table[i][k] = table[i][k - 1];
							table[i][k - 1] = 0;
							hadEffect = true;
						}
						k--;
						// repeat until the rightmost position j is filled
						if (k == 0 && hasNumbers)
							k = j;
					}
				}
			}
			// sum common cells
			for (int j = size - 1; j > 0; j--) {
				if (table[i][j] != 0 && table[i][j] == table[i][j - 1]) {
					table[i][j] *= 2;
					table[i][j - 1] = 0;
					emptyCells++;
					for (int k = j - 1; k > 0; k--) { // move the rest one cell to the right
						table[i][k] = table[i][k - 1];
						table[i][k - 1] = 0;
					}
					hadEffect = true;
				}
			}
		}

		if (!hadEffect)
			throw new PointlessMovementException();
		return table;
	}

}
