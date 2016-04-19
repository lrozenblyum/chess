package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;

import java.util.HashMap;
import java.util.Map;

import static com.leokom.chess.player.legalMover.BasicEvaluator.*;

/**
 * Central evaluator of a move ('brains', 'decision maker')
 */
class MasterEvaluator implements Evaluator {
	//we don't need to know that we can execute other moves
	//while evaluating a move, do we?
	//so far no, but from human logic we need that possibility
	//among 2 'equal' moves we would like to select according to some
	//compare 1-to-another logic

	private Map< BasicEvaluator, Double > evaluatorWeights = new HashMap<>();

	private static final double HIGHEST_PRIORITY = 100.0;
	private static final double NORMAL_PRIORITY = 1.0;
	private static final double DISABLED = 0.0;

	{
		evaluatorWeights.put( CHECKMATE, HIGHEST_PRIORITY );
		evaluatorWeights.put( CASTLING_SAFETY, NORMAL_PRIORITY );
		evaluatorWeights.put( CENTER_CONTROL, NORMAL_PRIORITY );
		evaluatorWeights.put( MOBILITY, NORMAL_PRIORITY );
		evaluatorWeights.put( MATERIAL, NORMAL_PRIORITY );
		evaluatorWeights.put( PROTECTION, NORMAL_PRIORITY );
		//this disabling is not absolute. Those moves anyway have chance
		//e.g. if LegalPlayer selects moves in reverse order (from worse to best)
		evaluatorWeights.put( SPECIAL_MOVE, DISABLED );
	}

	@Override
	public double evaluateMove( Position position, Move move ) {
		return evaluatorWeights.entrySet().stream().mapToDouble( evaluatorEntry -> {
			final Evaluator evaluator = evaluatorEntry.getKey().getEvaluator();
			final double weight = evaluatorEntry.getValue();
			return weight * evaluator.evaluateMove( position, move );
		} ).sum();
	}
}
