package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.brain.common.Evaluator;
import com.leokom.chess.player.legal.brain.common.EvaluatorFactory;
import com.leokom.chess.player.legal.brain.common.EvaluatorType;
import com.leokom.chess.player.legal.brain.internal.common.EvaluatorWeights;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.stream.Stream;

/**
 * Main normalized evaluator. Delegates evaluation to other normalized evaluators.
 */
public class MasterEvaluator implements Evaluator {
	private static final Logger LOG = LogManager.getLogger();

	//we don't need to know that we can execute other moves
	//while evaluating a move, do we?
	//so far no, but from human logic we need that possibility
	//among 2 'equal' moves we would like to select according to some
	//compare 1-to-another logic

	private final EvaluatorWeights evaluatorWeights;
    private final EvaluatorFactory evaluatorFactory;

    public MasterEvaluator() {
		//standard weights
		this( new EvaluatorWeights() );
	}

	/**
	 * create evaluator with custom weights
	 * @param weights evaluator -> weight
	 */
	MasterEvaluator( EvaluatorWeights weights ) {
		//standard evaluator factory
		this( weights, new NormalizedEvaluatorFactory() );
	}

	MasterEvaluator( EvaluatorWeights evaluatorWeights, EvaluatorFactory evaluatorFactory ) {
		this.evaluatorWeights = evaluatorWeights;
		this.evaluatorFactory = evaluatorFactory;
	}

	@Override
	public double evaluateMove( Position position, Move move ) {
		if ( position.move( move ).isTerminal() ) {
			double result = evaluatorFactory.get( EvaluatorType.TERMINAL ).evaluateMove(position, move);
			LOG.info( "{} ===> {}", move, result );
			return result;
		}

		// Terminal evaluator excluded because it's used above.
		// NOTE: it's still in evaluatorWeights until DenormalizedBrain uses it (till https://github.com/lrozenblyum/chess/issues/290)
		double result = evaluatorsExceptTerminal().mapToDouble(evaluatorEntry -> {
			final Evaluator evaluator = evaluatorFactory.get(evaluatorEntry.getKey());
			final double weight = evaluatorEntry.getValue();
			final double evaluatorResponse = evaluator.evaluateMove(position, move);
			final double moveEstimate = weight * evaluatorResponse;
			LOG.debug( "{} [{}] : {} * {} = {}", move, evaluatorEntry.getKey(), weight, evaluatorResponse, moveEstimate );
			return moveEstimate;
		}).sum();

		//result that is in [ 0, 1 ] range
		//depends on the fact that the weights themselves are in [ 0, 1 ]
		double normalizedResult = result / evaluatorsExceptTerminal().count();

		LOG.info("{} ===> {} ===> {}", move, result, normalizedResult);
		return normalizedResult;
	}

	private Stream<Map.Entry<EvaluatorType, Double>> evaluatorsExceptTerminal() {
		return evaluatorWeights.stream().filter( evaluatorEntry ->
			evaluatorEntry.getKey() != EvaluatorType.TERMINAL
		);
	}
}
