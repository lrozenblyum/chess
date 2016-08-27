package com.leokom.chess.player.legal.evaluator.normalized;

import com.google.common.collect.ImmutableSet;
import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.evaluator.common.Evaluator;
import com.leokom.chess.player.legal.evaluator.common.EvaluatorType;
import com.leokom.chess.player.legal.evaluator.denormalized.DenormalizedEvaluatorFactory;

import java.util.Set;

/**
 * If after the move we control centre squares
 *
 * [ currently we mean d4, d5, e4, e5 ]
 *
 * then a move is considered 'Good'
 *
 * Author: Leonid
 * Date-time: 14.07.14 23:11
 */
class CenterControlEvaluator implements Evaluator {
	//TODO: duplication with evaluator from another package, should we generalize?
	private static final Set<String> CENTER_SQUARES = ImmutableSet.of( "e5", "e4", "d4", "d5" );

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public double evaluateMove( Position position, Move move ) {
		return new DenormalizedEvaluatorFactory().get( EvaluatorType.CENTER_CONTROL )
				.evaluateMove( position, move ) / (float) CENTER_SQUARES.size();
	}
}
