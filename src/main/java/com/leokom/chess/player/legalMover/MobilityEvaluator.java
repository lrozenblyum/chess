package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;

/**
 * Author: Leonid
 * Date-time: 23.07.14 21:46
 */
public class MobilityEvaluator implements Evaluator {
	@Override
	public double evaluateMove( Position position, Move move ) {
		return move.getTo().equals( "d4" ) ? 1 : 0;
	}
}
