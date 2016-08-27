package com.leokom.chess.player.legal;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;

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

	@Override
	public double evaluateMove( Position position, Move move ) {
		return move == Move.OFFER_DRAW ? PREFERRED_MOVE : USUAL_MOVE;
	}
}
