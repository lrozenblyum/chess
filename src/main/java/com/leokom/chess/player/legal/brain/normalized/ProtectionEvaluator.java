package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.brain.common.Evaluator;
import com.leokom.chess.player.legal.brain.common.EvaluatorType;
import com.leokom.chess.player.legal.brain.denormalized.DenormalizedEvaluatorFactory;

/**
 * Author: Leonid
 * Date-time: 21.10.14 23:03
 */
class ProtectionEvaluator implements Evaluator {
	//TODO: we should use a more precise dynamic maximal value (count of our pieces * (count-1) )
	private static final SymmetricalNormalizedRange RANGE = new SymmetricalNormalizedRange( 0.0, MaterialEvaluator.MAXIMAL_VALUE );

	@Override
	public double evaluateMove( Position position, Move move ) {
		return RANGE.normalize(
				new DenormalizedEvaluatorFactory().get( EvaluatorType.PROTECTION )
				.evaluateMove( position, move ) );
	}
 }