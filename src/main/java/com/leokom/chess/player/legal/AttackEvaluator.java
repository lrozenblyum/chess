package com.leokom.chess.player.legal;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;

/**
 * Author: Leonid
 * Date-time: 25.08.16 20:54
 */
class AttackEvaluator implements Evaluator {
	private static final double WORST_MOVE = 0.0;

	@Override
	public double evaluateMove( Position position, Move move ) {
		if ( move.isSpecial() ) {
			return WORST_MOVE;
		}

		final Side ourSide = position.getSide( move.getFrom() );
		float attackIndex = AttackIndexCalculator.getAttackIndex(
				position.move( move ), ourSide );
		return attackIndex / MaterialEvaluator.MAXIMAL_VALUE;
	}
}
