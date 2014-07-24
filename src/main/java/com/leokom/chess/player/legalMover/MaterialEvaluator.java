package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;

/**
 * Evaluate material domination
 *
 * This is the first evaluator
 * for which I think symmetric counter-part
 * has no sense (since it's a difference between us & opponent)
 *
 */
public class MaterialEvaluator implements Evaluator {
	@Override
	public double evaluateMove( Position position, Move move ) {
		return move.getTo().equals( "d5" ) ? 1 : 0;
	}
}
