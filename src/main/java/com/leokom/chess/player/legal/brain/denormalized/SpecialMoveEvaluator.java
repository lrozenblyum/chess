package com.leokom.chess.player.legal.brain.denormalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.brain.common.Evaluator;

/**
 * At first : for testing &amp; demo purposes
 * to allow forcing our engine offer draws thus having
 * visual feedback
 *
 * Author: Leonid
 * Date-time: 03.08.15 22:15
 */
class SpecialMoveEvaluator implements Evaluator {

	private static final int PREFERRED_MOVE = 1;
	private static final int USUAL_MOVE = 0;

	/**
	 * {@inheritDoc}
	 * @return 0 or 1
	 */
	@Override
	public double evaluateMove( Position position, Move move ) {
		return move == Move.OFFER_DRAW ? PREFERRED_MOVE : USUAL_MOVE;
	}
}
