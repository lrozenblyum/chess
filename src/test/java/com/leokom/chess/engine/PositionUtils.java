package com.leokom.chess.engine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Test utilities to check position
 * Author: Leonid
 * Date-time: 25.08.12 18:04
 */
final class PositionUtils {
	private PositionUtils() {}

	//TODO: point to extend! When we introduce new pieces - need to make here randomization
	//over each piece that can be captured (all, except King, except pawns on final rank, except...?)
	static void addCapturable( Position position, Side side, String square ) {
		position.addPawn( side, square );
	}

	//TODO: extend when we introduce new pieces!

	/**
	 * Add any piece to the position's given square.
	 * Note: I expect that the position will be still valid after this addition.
	 * But this note requires deeper thinking about what's 'valid' position
	 * and how must it be kept (e.g. by some validating builder?)
	 * @param position
	 * @param side
	 * @param square
	 */
	static PieceType addAny( Position position, Side side, String square ) {
		position.addPawn( side, square );
		return PieceType.PAWN;
	}

}
