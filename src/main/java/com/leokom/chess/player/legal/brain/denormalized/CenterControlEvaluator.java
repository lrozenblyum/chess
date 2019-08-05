package com.leokom.chess.player.legal.brain.denormalized;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.legal.brain.common.Evaluator;
import com.leokom.chess.player.legal.brain.common.SideEvaluator;
import com.leokom.chess.player.legal.brain.internal.common.SymmetricEvaluator;

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
	/**
	 * {@inheritDoc}
	 * @return [ -4, 4 ]
	 */
	@Override
	public double evaluateMove( Position position, Move move ) {
		//TODO: if we're already in central square
		//does it mean control now?
		//e.g. Knight on e5 cannot attack e4, d4, d5
		//but blocks the center

		final Position targetPosition = position.move( move );
		return new SymmetricEvaluator( new CenterControlSideEvaluator() ).evaluate( targetPosition );
	}

	private static class CenterControlSideEvaluator implements SideEvaluator {
		private static final Set<String> CENTER_SQUARES = ImmutableSet.of( "e5", "e4", "d4", "d5" );

		@Override
		public double evaluatePosition(Position position, Side side) {
			final Set< String > squaresAttacked = position.getSquaresAttackedBy(side);

			final Set< String > intersection = Sets.intersection( squaresAttacked, CENTER_SQUARES );

			return intersection.size();
		}
	}
}
