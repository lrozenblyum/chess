package com.leokom.chess.player.legal.brain.normalized;

import com.google.common.collect.ImmutableSet;
import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.brain.common.Evaluator;
import com.leokom.chess.player.legal.brain.common.EvaluatorType;
import com.leokom.chess.player.legal.brain.denormalized.DenormalizedEvaluatorFactory;

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
	//TODO: duplication with brain from another package, should we generalize?
	private static final Set<String> CENTER_SQUARES = ImmutableSet.of( "e5", "e4", "d4", "d5" );

	private static final SymmetricalNormalizedRange range = new SymmetricalNormalizedRange( 0, 4 );

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public double evaluateMove( Position position, Move move ) {
		return range.normalize( new DenormalizedEvaluatorFactory().get( EvaluatorType.CENTER_CONTROL )
				.evaluateMove( position, move ) );
	}
}
