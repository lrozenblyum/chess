package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;

import java.util.Arrays;
import java.util.List;

/**
 * Central evaluator of a move ('brains', 'decision maker')
 */
class MasterEvaluator implements Evaluator {

	@Override
	public double evaluateMove( Position position, Move move ) {
		//we don't need to know that we can execute other moves
		//while evaluating a move, do we?
		//so far no, but from human logic we need that possibility
		//among 2 'equal' moves we would like to select according to some
		//compare 1-to-another logic
		List< Evaluator > evaluators = Arrays.asList( new CastlingSafetyEvaluator(), new CenterControlEvaluator(), new MobilityEvaluator() );
		double estimate = 0;
		for ( Evaluator evaluator : evaluators ) {
			// rather safe if all evaluators keep convention [0 , 1]
			//here we also can multiply by weight of each evaluator for us
			estimate += evaluator.evaluateMove( position, move );
		}

		return estimate;
	}
}
