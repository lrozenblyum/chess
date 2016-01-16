package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;

/**
 * Estimate a move from a position.
 * A valid Evaluator implementation must be stateless
 * (not depend on evaluation of past moves).
 * Author: Leonid
 * Date-time: 14.07.14 22:57
 */
interface Evaluator {

	/**
	 * Get 'rating' of a move
	 * @param position chess position for which the move should be evaluated
	 * @param move move that we potentially could execute
	 * @return double number which means 'BIGGER'='BETTER'
	 * in range [ 0, 1 ]
	 * 0 is the least recommended move
	 * 1 is the most recommended one
	 */
	double evaluateMove( Position position, Move move );
}
