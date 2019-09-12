package com.leokom.games.chess.player.legal.brain.common;

import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.engine.Position;

/**
 * Estimate a move from a position.
 * A valid Evaluator implementation must be stateless
 * (not depend on evaluation of past moves).
 * Author: Leonid
 * Date-time: 14.07.14 22:57
 */
@FunctionalInterface
public interface Evaluator extends GenericEvaluator< Position, Move > {

	/**
	 * Get 'rating' of a move
	 * @param position chess position for which the move should be evaluated
	 * @param move move that we potentially could execute
	 * @return double number which means 'BIGGER'='BETTER'
	 *
	 * Specific categories of evaluators can introduce some limits on range of values supported
	 */
	@Override
	double evaluateMove( Position position, Move move );
}
