package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.player.legal.brain.common.EvaluatorType;
import com.leokom.chess.player.legal.brain.internal.common.EvaluatorWeights;

import java.util.HashMap;
import java.util.Map;

/**
 * Create MasterEvaluator instances
 * with custom weights
 *
 * Author: Leonid
 * Date-time: 19.04.16 23:03
 */
public class MasterEvaluatorBuilder {
	//wrapping to a hash map to allow mutability
	private Map<EvaluatorType, Double > weights = new HashMap<>( new EvaluatorWeights().asMap() );

	public MasterEvaluatorBuilder weight( EvaluatorType evaluatorType, double weight ) {
		weights.put( evaluatorType, weight );
		return this;
	}

	public MasterEvaluator build() {
		return new MasterEvaluator( new EvaluatorWeights( weights ) );
	}
}