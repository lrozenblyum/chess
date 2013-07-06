package com.leokom.chess.engine;

/**
 * Encapsulate some knowledge about moves.
 * So far it's just statics but
 * it may evolve in some real move storage
 *
 * Author: Leonid
 * Date-time: 06.07.13 22:38
 */
final class Move {
	/**
	 * Size of promotion move (e.g. "h1Q")
	 */
	static final int PROMOTION_MOVE_SIZE = 3;

	private Move() {}

	static boolean isPromotion( String move ) {
		return move.length() == PROMOTION_MOVE_SIZE;
	}

	/**
	 * Get destination square from the move
	 *
	 * @param move in format like e2 or f1Q
	 * @return destination square (e2 or f1 correspondingly)
	 */
	static String getDestinationSquare( String move ) {
		return isPromotion( move ) ?
				move.substring( 0, 2 ) :
				move;
	}
}
