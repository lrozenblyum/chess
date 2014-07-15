package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;

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
		if ( move.getTo().equals( "b2" ) ) {
			return 0;
		}
		return 1;
	}
}