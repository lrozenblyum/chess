package com.leokom.chess.player.legal.evaluator.normalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.evaluator.common.Evaluator;
import com.leokom.chess.player.legal.evaluator.common.EvaluatorType;
import com.leokom.chess.player.legal.evaluator.internal.common.EvaluatorWeights;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

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

	private final Map<EvaluatorType, Double > evaluatorWeights;

	MasterEvaluator() {
		this( EvaluatorWeights.getStandardWeights() );
	}

	MasterEvaluator( Map<EvaluatorType, Double > weights ) {
		this.evaluatorWeights = weights;
	}

	@Override
	public double evaluateMove( Position position, Move move ) {
		return evaluatorWeights.entrySet().stream().mapToDouble( evaluatorEntry -> {
			final Evaluator evaluator = new NormalizedEvaluatorFactory().get( evaluatorEntry.getKey() );
			final double weight = evaluatorEntry.getValue();
			final double evaluatorResponse = evaluator.evaluateMove( position, move );
			final double moveEstimate = weight * evaluatorResponse;
			LOG.debug( "{} [{}] : {} * {} = {}", move, evaluatorEntry.getKey(), weight, evaluatorResponse, moveEstimate );
			return moveEstimate;
		} ).sum();
	}
}
