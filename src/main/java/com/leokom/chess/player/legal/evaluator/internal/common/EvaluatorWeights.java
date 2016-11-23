package com.leokom.chess.player.legal.evaluator.internal.common;

import com.leokom.chess.player.legal.evaluator.common.EvaluatorType;

import java.util.HashMap;
import java.util.Map;

import static com.leokom.chess.player.legal.evaluator.common.EvaluatorType.*;

/**
 * Author: Leonid
 * Date-time: 27.08.16 21:54
 */
public final class EvaluatorWeights {
	private EvaluatorWeights() {}

	private static final double HIGHEST_PRIORITY = 100.0;
	private static final double INCREASED_PRIORITY = 3.0;
	private static final double NORMAL_PRIORITY = 1.0;
	private static final double DISABLED = 0.0;

	public static Map<EvaluatorType, Double > getStandardWeights() {
		//TODO: refactor to constant immutable map
		Map<EvaluatorType, Double > result = new HashMap<>();
		result.put( CHECKMATE, HIGHEST_PRIORITY );
		result.put( CASTLING_SAFETY, NORMAL_PRIORITY );
		result.put( CENTER_CONTROL, NORMAL_PRIORITY );
		result.put( MOBILITY, NORMAL_PRIORITY );
		//empirically found minimal multiplier that causes GOOD results for DenormalizedDecisionMaker
		result.put( MATERIAL, INCREASED_PRIORITY );
		result.put( PROTECTION, NORMAL_PRIORITY );
		result.put( ATTACK, NORMAL_PRIORITY );
		//this disabling is not absolute. Those moves anyway have chance
		//e.g. if LegalPlayer selects moves in reverse order (from worse to best)
		result.put( SPECIAL_MOVE, DISABLED );
		return result;
	}
}
