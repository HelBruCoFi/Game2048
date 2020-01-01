import java.util.Scanner;

import exceptions.*;

/**
 * @author HB
 */
public class Main {

	private static final String UNKNOWN_COMMAND /*							*/ = "Unkown command.";
	private static final String EXITING /*									*/ = "Saving and exiting...";

	static final long serialVersionUID = 0L;

	/* possible commands */
	private enum Commands {
		W, A, S, D, XS
	}

	public static void main(String[] args) {
		interpreter();
	}

	private static void interpreter() {
		Scanner in = new Scanner(System.in);
		Game game = load(in);
		printTable(game);
		System.out.println("Enter your play:");

		String comm = getCommand(in);

		while (!comm.equals(Commands.XS.toString())) {

			try {
				switch (Commands.valueOf(comm)) {
				case W:
				case A:
				case S:
				case D:
					processMovement(comm, game);
					break;
				default:
					break;
				}
				System.out.println();
				System.out.println("Enter your move:");
				comm = getCommand(in);
			} catch (IllegalArgumentException e) {
				System.out.println("Wrong command, try again.");
				comm = getCommand(in);
			} catch (GameLostException e) {
				System.out.println("You lost. This is how you finished.");
				printTable(game);
				break;
			}
		}
		System.out.println(EXITING);
		System.out.println();
		in.close();
	}

	/**
	 * Reads input command
	 * 
	 * @param in - reading scanner
	 * @return input command
	 */
	private static String getCommand(Scanner in) {
		String input;
		input = in.next().toUpperCase();
		return input;
	}

	/**
	 * Process movement in the direction given in the input
	 * 
	 * @param direction    - input commands
	 * @param classObjName -
	 */
	private static void processMovement(String direction, Game game) throws GameLostException {
		try {
			game.newMove(direction.trim().charAt(0));
		} catch (PointlessMovementException e) {
			System.out.println("Your movement was pointless, nothing happened. Try another one.");
		}
		printTable(game);
	}

	/**
	 * Prints the current table of the game
	 * 
	 * @param game
	 */
	private static void printTable(Game game) {
		int[][] table = game.getTable();
		int length = table.length;
		System.out.println("Current table:");
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length - 1; j++)
				System.out.print(table[i][j] + " \t");
			System.out.println(table[i][length - 1]);
		}
	}

	/**
	 * Loads all the data stored on the last execution
	 * 
	 * @return object class data stored
	 */
	private static Game load(Scanner in) {
		System.out.println("Enter the size of the table for the game:");
		int size = Integer.parseInt(getCommand(in));
		Game game = new GameClass(size);
		System.out.println("Game instructions:");
		System.out.println("W - Move up");
		System.out.println("A - Move left");
		System.out.println("S - Move down");
		System.out.println("D - Move right");
		System.out.println("XS - Exit game");
		System.out.println();
		return game;
	}

}