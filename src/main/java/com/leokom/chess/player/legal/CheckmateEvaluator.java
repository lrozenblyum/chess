package com.leokom.chess.player.legal;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;

/**
 * Author: Leonid
 * Date-time: 01.03.15 22:32
 *
 * Checkmate is the highest goal of the whole game
 */
class CheckmateEvaluator implements Evaluator {

	private static final int BEST_MOVE = 1;
	private static final int WORST_MOVE = 0;

	@Override
	public double evaluateMove( Position position, Move move ) {
		if ( move.isSpecial() ) {
			return WORST_MOVE;
		}

		final Position result = position.move( move );
		return result.isTerminal() &&
				position.getSide( move.getFrom() ) == result.getWinningSide() ?
				BEST_MOVE : WORST_MOVE;
	}
}
