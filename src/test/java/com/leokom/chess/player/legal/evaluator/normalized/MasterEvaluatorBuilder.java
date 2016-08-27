package com.leokom.chess.player.legal.evaluator.normalized;

import com.leokom.chess.player.legal.evaluator.common.EvaluatorType;
import com.leokom.chess.player.legal.evaluator.internal.common.EvaluatorWeights;

import java.util.Map;

/**
 * Create MasterEvaluator instances
 * with custom weights
 *
 * Author: Leonid
 * Date-time: 19.04.16 23:03
 */
public class MasterEvaluatorBuilder {
	private Map<EvaluatorType, Double > weights = EvaluatorWeights.getStandardWeights();

	public MasterEvaluatorBuilder weight( EvaluatorType evaluatorType, double weight ) {
		weights.put( evaluatorType, weight );
		return this;
	}

	public MasterEvaluator build() {
		return new MasterEvaluator( weights );
	}
}