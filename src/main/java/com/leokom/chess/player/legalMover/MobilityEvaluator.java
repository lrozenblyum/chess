package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;

/**
 * If inside a position there is a bigger variety of moves
 * we consider this as better
 *
 * Author: Leonid
 * Date-time: 23.07.14 21:46
 */
public class MobilityEvaluator implements Evaluator {
	@Override
	public double evaluateMove( Position position, Move move ) {
		final Position target = position.move( move );

		final Side ourSide = position.getSide( move.getFrom() );

		return target.getMoves( ourSide ).size();
	}
}
