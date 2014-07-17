package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;

import java.util.Arrays;
import java.util.HashSet;
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
	 *
	 */
	@Override
	public double evaluateMove( Position position, Move move ) {
		//TODO: if we're already in central square
		//does it mean control now?
		//e.g. Knight on e5 cannot attack e4, d4, d5
		//but blocks the center


		final Side ourSide = position.getSide( move.getFrom() );

		final Position targetPosition = position.move( move );
		//technically it's naive check - since the situation
		//can change drastically after the opponent's move
		//however we look now only at 1/2 depth
		final Set< String > squaresAttackedByUs = targetPosition.getSquaresAttackedBy( ourSide );
		final Set< String > centerSquares = new HashSet<>( Arrays.asList(
			"e5", "e4", "d4", "d5"
		) );

		final Set< String > intersection = squaresAttackedByUs;
		intersection.retainAll( centerSquares );

		return intersection.isEmpty() ? 0 : 1;
	}
}
