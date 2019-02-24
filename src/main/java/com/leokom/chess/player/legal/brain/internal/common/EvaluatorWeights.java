package com.leokom.chess.player.legal.brain.internal.common;

import com.leokom.chess.player.legal.brain.common.EvaluatorType;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static com.leokom.chess.player.legal.brain.common.EvaluatorType.*;

/**
 * Author: Leonid
 * Date-time: 27.08.16 21:54
 */
public final class EvaluatorWeights {
	private static final double HIGHEST_PRIORITY = 1.0;
	//historically it was a minimal multiplier that causes GOOD results for DenormalizedBrain, it may be irrelevant now
	private static final double INCREASED_PRIORITY = 0.03;
	private static final double NORMAL_PRIORITY = 0.01;
	private static final double LOWEST_POSSIBLE = 0.0;

	private final Map<EvaluatorType, Double> weights;

	public EvaluatorWeights() {
		this( getStandardWeights() );
	}

	/**
	 * Create weights object, ensure constraint: every weight is in [ 0,1 ] range
	 * @param weights weights
	 */
	public EvaluatorWeights(Map<EvaluatorType, Double> weights) {
		verifyRange( weights );
		this.weights = Collections.unmodifiableMap( new HashMap<>( weights ) );
	}

	//alternative would be: normalize during processing
	//decided not to do that for simplicity
	private void verifyRange(Map<EvaluatorType, Double> weights) {
		if ( weights.values().stream().anyMatch( weight -> weight < 0.0 || weight > 1.0 ) ) {
			throw new IllegalArgumentException( String.format( "Illegal weight outside of allowed range detected. Map: %s", weights ) );
		}
	}

	private static Map<EvaluatorType, Double > getStandardWeights() {
		//TODO: refactor to constant immutable map
		Map<EvaluatorType, Double > result = new EnumMap<>( EvaluatorType.class	);
		//terminal evaluator is still here till https://github.com/lrozenblyum/chess/issues/290
		result.put( TERMINAL, HIGHEST_PRIORITY );
		result.put( CASTLING_SAFETY, NORMAL_PRIORITY );
		result.put( CENTER_CONTROL, NORMAL_PRIORITY );
		result.put( MOBILITY, NORMAL_PRIORITY );
		result.put( MATERIAL, INCREASED_PRIORITY );
		result.put( PROTECTION, NORMAL_PRIORITY );
		result.put( ATTACK, NORMAL_PRIORITY );
		//this disabling is not absolute. Those moves anyway have chance
		//e.g. if LegalPlayer selects moves in reverse order (from worse to best)
		result.put( SPECIAL_MOVE, LOWEST_POSSIBLE );
		return result;
	}

	public Stream<Entry< EvaluatorType, Double >> stream() {
		return this.weights.entrySet().stream();
	}

	public int size() {
		return this.weights.size();
	}

	public Map<EvaluatorType, Double> asMap() {
		return this.weights;
	}
}
