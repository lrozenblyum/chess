package com.leokom.chess.engine;

/**
 * Pawn test utilities
 * Author: Leonid
 * Date-time: 25.08.12 18:25
 */
final class PawnUtils {
	private PawnUtils() {}

	/**
	 * Add a pawn to the position,
	 * check if allowed moves are equal to allMoves
	 * @param position
	 * @param pawnPosition
	 * @param side
	 * @param allMoves
	 */
	static void testPawn( Position position, String pawnPosition, Side side, String... allMoves ) {
		position.addPawn( side, pawnPosition );
		PositionUtils.assertAllowedMoves( position, pawnPosition, allMoves );
	}
}
