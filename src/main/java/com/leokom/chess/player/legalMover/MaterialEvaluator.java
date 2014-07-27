package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;

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
		final Position target = position.move( move );

		Side ourSide = position.getSide( move.getFrom() );

		//funny detection of capture
		int squaresOccupiedByOpponentBeforeMove = position.getSquaresOccupiedBySide( ourSide.opposite() ).size();
		int squaresOccupiedByOpponentAfterMove = target.getSquaresOccupiedBySide( ourSide.opposite() ).size();
		boolean pieceCaptured = squaresOccupiedByOpponentAfterMove - squaresOccupiedByOpponentBeforeMove < 0;

		return pieceCaptured ? 1 : 0;
	}
}
