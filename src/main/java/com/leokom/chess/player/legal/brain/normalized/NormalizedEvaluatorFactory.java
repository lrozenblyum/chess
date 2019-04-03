package com.leokom.chess.player.legal.brain.normalized;

import com.google.common.collect.Maps;
import com.leokom.chess.player.legal.brain.common.Evaluator;
import com.leokom.chess.player.legal.brain.common.EvaluatorFactory;
import com.leokom.chess.player.legal.brain.common.EvaluatorType;
import com.leokom.chess.player.legal.brain.denormalized.DenormalizedEvaluatorFactory;

import java.util.EnumMap;
import java.util.Map;

/**
 * Author: Leonid
 * Date-time: 27.08.16 13:28
 */
public class NormalizedEvaluatorFactory implements EvaluatorFactory {
	private static final Map<EvaluatorType, Evaluator> EVALUATORS;
	private static final DenormalizedEvaluatorFactory DENORMALIZED_EVALUATOR_FACTORY = new DenormalizedEvaluatorFactory();

	static {
		//we keep references to instances of EVALUATORS here
		//so they're practically singletons
		//any valid Evaluator must be stateless and thread-safe

		Map< EvaluatorType, Evaluator > evaluatorsMutable = new EnumMap<>( EvaluatorType.class );
		evaluatorsMutable.put( EvaluatorType.ATTACK, new AttackEvaluator() );
		evaluatorsMutable.put( EvaluatorType.CENTER_CONTROL, new CenterControlEvaluator() );
		evaluatorsMutable.put( EvaluatorType.MATERIAL, new MaterialEvaluator() );
		evaluatorsMutable.put( EvaluatorType.MOBILITY, new MobilityEvaluator() );
		evaluatorsMutable.put( EvaluatorType.PROTECTION, new ProtectionEvaluator() );


		EVALUATORS = Maps.immutableEnumMap( evaluatorsMutable );
	}

	/**
	 * @implNote part of EVALUATORS might be reused from denormalized package if they already
	 * provide the correct values (NOTE: most likely we should move them to normalized package then!)
	 * @param type type of brain to get brain from
	 * @return brain that is normalized [ 0, 1 ]
	 */
	@Override
	public Evaluator get( EvaluatorType type ) {
		return EVALUATORS.get( type ) != null ? EVALUATORS.get( type ) :
			DENORMALIZED_EVALUATOR_FACTORY.get( type );
	}
}
