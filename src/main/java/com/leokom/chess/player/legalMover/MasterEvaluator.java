package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.leokom.chess.player.legalMover.EvaluatorType.*;

/**
 * Central evaluator of a move ('brains')
 */
class MasterEvaluator implements Evaluator {
	private static final Logger LOG = LogManager.getLogger();

	//we don't need to know that we can execute other moves
	//while evaluating a move, do we?
	//so far no, but from human logic we need that possibility
	//among 2 'equal' moves we would like to select according to some
	//compare 1-to-another logic


	private static final double HIGHEST_PRIORITY = 100.0;
	private static final double NORMAL_PRIORITY = 1.0;
	private static final double DISABLED = 0.0;

	private final Map<EvaluatorType, Double > evaluatorWeights;

	MasterEvaluator() {
		this( getStandardWeights() );
	}

	MasterEvaluator( Map<EvaluatorType, Double > weights ) {
		this.evaluatorWeights = weights;
	}

	static Map<EvaluatorType, Double > getStandardWeights() {
		Map<EvaluatorType, Double > result = new HashMap<>();
		result.put( CHECKMATE, HIGHEST_PRIORITY );
		result.put( CASTLING_SAFETY, NORMAL_PRIORITY );
		result.put( CENTER_CONTROL, NORMAL_PRIORITY );
		result.put( MOBILITY, NORMAL_PRIORITY );
		result.put( MATERIAL, NORMAL_PRIORITY );
		result.put( PROTECTION, NORMAL_PRIORITY );
		//this disabling is not absolute. Those moves anyway have chance
		//e.g. if LegalPlayer selects moves in reverse order (from worse to best)
		result.put( SPECIAL_MOVE, DISABLED );
		return result;
	}

	@Override
	public double evaluateMove( Position position, Move move ) {
		return evaluatorWeights.entrySet().stream().mapToDouble( evaluatorEntry -> {
			final Evaluator evaluator = evaluatorEntry.getKey().getEvaluator();
			final double weight = evaluatorEntry.getValue();
			final double evaluatorResponse = evaluator.evaluateMove( position, move );
			final double moveEstimate = weight * evaluatorResponse;
			LOG.debug( "{} [{}] : {} * {} = {}", move, evaluatorEntry.getKey(), weight, evaluatorResponse, moveEstimate );
			return moveEstimate;
		} ).sum();
	}
}
