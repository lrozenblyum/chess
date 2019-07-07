package com.leokom.chess.player.legal.brain.denormalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.legal.brain.common.Evaluator;

/**
 * Author: Leonid
 * Date-time: 25.08.16 20:54
 */
class AttackEvaluator implements Evaluator {
	@Override
	public double evaluateMove( Position position, Move move ) {
		final Side ourSide = position.getSideToMove();
		Position targetPosition = position.move(move);
		return AttackIndexCalculator.getAttackIndex( targetPosition, ourSide )
				- AttackIndexCalculator.getAttackIndex( targetPosition, ourSide.opposite() );
	}
}
