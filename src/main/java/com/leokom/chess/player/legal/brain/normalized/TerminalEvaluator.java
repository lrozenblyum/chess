package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.brain.common.Evaluator;

/**
 * Author: Leonid
 * Date-time: 01.03.15 22:32
 *
 * Evaluate final game state.
 * Checkmate is the highest goal of the whole game and will mean win.
 */
public class TerminalEvaluator implements Evaluator {

	private static final int BEST_MOVE = 1;
	private static final int WORST_MOVE = 0;

	/**
	 *
	 * {@inheritDoc}
	 * @return 0 or 1
	 */
	@Override
	public double evaluateMove( Position position, Move move ) {
		//TODO: what about accepted draw and other terminal conditions?
		final Position result = position.move( move );
		return result.isTerminal() &&
				result.getWinningSide() != null && //this excludes draws
				position.getSide( move.getFrom() ) == result.getWinningSide() ?
				BEST_MOVE : WORST_MOVE;
	}
}
