package platformer;

import java.util.Random;

public enum Move {
	UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT;

	private static final Random RANDOM = new Random();

	public static Move randomMove() {
		Move[] moves = values();
		return moves[RANDOM.nextInt(moves.length)];
	}
}
