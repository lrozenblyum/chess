package com.leokom.chess.player.legal.evaluator.normalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.evaluator.common.Evaluator;
import com.leokom.chess.player.legal.evaluator.common.EvaluatorType;
import com.leokom.chess.player.legal.evaluator.denormalized.DenormalizedEvaluatorFactory;

/**
 * Author: Leonid
 * Date-time: 01.03.15 22:32
 *
 * Checkmate is the highest goal of the whole game
 */
class CheckmateEvaluator implements Evaluator {
	@Override
	public double evaluateMove( Position position, Move move ) {
		return new DenormalizedEvaluatorFactory().get( EvaluatorType.CHECKMATE )
				.evaluateMove( position, move );
	}
}
