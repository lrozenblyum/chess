package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.brain.common.Evaluator;
import com.leokom.chess.player.legal.brain.common.EvaluatorType;
import com.leokom.chess.player.legal.brain.denormalized.CheckmateEvaluator;
import com.leokom.chess.player.legal.brain.internal.common.EvaluatorWeights;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Central brain of a move ('brains')
 */
public class MasterEvaluator implements Evaluator {
	private static final Logger LOG = LogManager.getLogger();

	//we don't need to know that we can execute other moves
	//while evaluating a move, do we?
	//so far no, but from human logic we need that possibility
	//among 2 'equal' moves we would like to select according to some
	//compare 1-to-another logic

	private final Map<EvaluatorType, Double > evaluatorWeights;

	public MasterEvaluator() {
		this( EvaluatorWeights.getStandardWeights() );
	}

	MasterEvaluator( Map<EvaluatorType, Double > weights ) {
		this.evaluatorWeights = weights;
	}

	@Override
	public double evaluateMove( Position position, Move move ) {
		if ( position.move( move ).isTerminal() ) {
			return new CheckmateEvaluator().evaluateMove( position, move );
		}

		double result = evaluatorWeights.entrySet().stream().mapToDouble(evaluatorEntry -> {
			final Evaluator evaluator = new NormalizedEvaluatorFactory().get(evaluatorEntry.getKey());
			final double weight = evaluatorEntry.getValue();
			final double evaluatorResponse = evaluator.evaluateMove(position, move);
			final double moveEstimate = weight * evaluatorResponse;
			LOG.debug( "{} [{}] : {} * {} = {}", move, evaluatorEntry.getKey(), weight, evaluatorResponse, moveEstimate );
			return moveEstimate;
		}).sum();

		//result that is in [ 0, 1 ] range
		//it should work assuming the weights themselves are in [ 0, 1 ]
		double normalizedResult = result / evaluatorWeights.size();

		LOG.info("{} ===> {} ===> {}", move, result, normalizedResult);
		return normalizedResult;
	}
}
