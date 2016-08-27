package com.leokom.chess.player.legal.evaluator.normalized;


import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.evaluator.common.Evaluator;
import com.leokom.chess.player.legal.evaluator.common.EvaluatorType;
import com.leokom.chess.player.legal.evaluator.denormalized.DenormalizedEvaluatorFactory;

/**
 * Author: Leonid
 * Date-time: 14.07.14 22:58
 */
class CastlingSafetyEvaluator implements Evaluator {
	@Override
	public double evaluateMove( Position position, Move move ) {
		//de-normalized edition is already in [0,1] boundaries
		return new DenormalizedEvaluatorFactory().get( EvaluatorType.CASTLING_SAFETY )
			.evaluateMove( position, move );
	}
}
