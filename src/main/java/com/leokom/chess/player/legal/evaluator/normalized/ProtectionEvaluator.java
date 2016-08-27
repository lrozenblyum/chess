package com.leokom.chess.player.legal.evaluator.normalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.evaluator.common.Evaluator;
import com.leokom.chess.player.legal.evaluator.common.EvaluatorType;
import com.leokom.chess.player.legal.evaluator.denormalized.DenormalizedEvaluatorFactory;

/**
 * Author: Leonid
 * Date-time: 21.10.14 23:03
 */
class ProtectionEvaluator implements Evaluator {
	@Override
	public double evaluateMove( Position position, Move move ) {
		return 1 +
				new DenormalizedEvaluatorFactory().get( EvaluatorType.PROTECTION )
				.evaluateMove( position, move ) / MaterialEvaluator.MAXIMAL_VALUE;
	}
 }