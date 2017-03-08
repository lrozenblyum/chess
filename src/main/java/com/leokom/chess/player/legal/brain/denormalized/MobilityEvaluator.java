package com.leokom.chess.player.legal.brain.denormalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.brain.common.Evaluator;

/**
 * If inside a position there is a bigger variety of moves
 * we consider this as better
 *
 * Author: Leonid
 * Date-time: 23.07.14 21:46
 */
class MobilityEvaluator implements Evaluator {
	private static final double WORST_MOVE = 0.0;

	/**
	 * {@inheritDoc}
	 * @return [ 0, max amount of legal moves in a position ]
	 */
	@Override
	public double evaluateMove( Position position, Move move ) {
		final Position target = position.move( move );
		//TODO: all evaluators must take into account
		//possibility that the position is terminal
		//most likely MasterEvaluator might take preventive actions.

		//proved need in LegalPlayerTest and in (temporarily ignored) LegalPlayerSelfTest
		if ( target.isTerminal() ) {
			return WORST_MOVE;
		}

		return target.toMirror().getMoves().size();
	}
}
