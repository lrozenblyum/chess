package com.leokom.games.chess.player.legal.brain.normalized;

import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.engine.Position;
import com.leokom.games.chess.player.legal.brain.common.Evaluator;
import com.leokom.games.chess.player.legal.brain.common.EvaluatorType;
import com.leokom.games.chess.player.legal.brain.denormalized.DenormalizedEvaluatorFactory;
import com.leokom.games.commons.brain.normalized.range.SymmetricalNormalizedRange;

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
	private static final SymmetricalNormalizedRange RANGE = new SymmetricalNormalizedRange( 0, 4 );

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public double evaluateMove( Position position, Move move ) {
		return RANGE.normalize( new DenormalizedEvaluatorFactory().get( EvaluatorType.CENTER_CONTROL )
				.evaluateMove( position, move ) );
	}
}
