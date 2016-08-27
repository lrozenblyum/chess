package com.leokom.chess.player.legal.evaluator.common;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;

/**
 * Estimate a move from a position.
 * A valid Evaluator implementation must be stateless
 * (not depend on evaluation of past moves).
 * Author: Leonid
 * Date-time: 14.07.14 22:57
 */
public interface Evaluator {

	/**
	 * Get 'rating' of a move
	 * @param position chess position for which the move should be evaluated
	 * @param move move that we potentially could execute
	 * @return double number which means 'BIGGER'='BETTER'
	 *
	 * Specific categories of evaluators can introduce some limits on range of values supported
	 */
	double evaluateMove( Position position, Move move );
}
