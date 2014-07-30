package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * Central evaluator of a move ('brains', 'decision maker')
 */
class MasterEvaluator implements Evaluator {
	//we don't need to know that we can execute other moves
	//while evaluating a move, do we?
	//so far no, but from human logic we need that possibility
	//among 2 'equal' moves we would like to select according to some
	//compare 1-to-another logic

	private Map< Evaluator, Double > evaluatorWeights = new HashMap<>();
	{
		evaluatorWeights.put( new CastlingSafetyEvaluator(), 1.0 );
		evaluatorWeights.put( new CenterControlEvaluator(), 1.0 );
		evaluatorWeights.put( new MobilityEvaluator(), 1.0 );
		evaluatorWeights.put( new MaterialEvaluator(), 1.0 );
	}

	@Override
	public double evaluateMove( Position position, Move move ) {
		double estimate = 0;
		for ( Map.Entry< Evaluator, Double > evaluatorEntry : evaluatorWeights.entrySet() ) {
			// rather safe if all evaluators keep convention [0 , 1]
			final Evaluator evaluator = evaluatorEntry.getKey();
			final double weight = evaluatorEntry.getValue();
			estimate += weight * evaluator.evaluateMove( position, move ) ;
		}

		return estimate;
	}
}
