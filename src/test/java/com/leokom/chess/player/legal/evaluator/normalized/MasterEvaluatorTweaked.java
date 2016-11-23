package com.leokom.chess.player.legal.evaluator.normalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;

/**
 * Ensure LegalPlayer test deal with evaluator
 * which doesn't try doing special moves like OFFER_DRAW/RESIGN
 *
 * Author: Leonid
 * Date-time: 16.01.16 21:04
 */
public class MasterEvaluatorTweaked extends MasterEvaluator {
	@Override
	public double evaluateMove( Position position, Move move ) {
		//trying to eliminate risks of 'OFFER_DRAW' polluting
		//test results when MasterEvaluator estimates are changed

		//it doesn't completely remove the risk
		//e.g. if all evaluators are set to 0 we still might generate
		//a special move
		//can be fixed by breaking a contract and returning some -1
		//I don't want to go that way
		return move.isSpecial() ?
				0 :
				super.evaluateMove( position, move );
	}
}
