package com.leokom.chess.player.legal.brain.denormalized;

import com.google.common.collect.Maps;
import com.leokom.chess.player.legal.brain.common.Evaluator;
import com.leokom.chess.player.legal.brain.common.EvaluatorFactory;
import com.leokom.chess.player.legal.brain.common.EvaluatorType;
import com.leokom.chess.player.legal.brain.normalized.TerminalEvaluator;

import java.util.EnumMap;
import java.util.Map;

/**
 * Author: Leonid
 * Date-time: 27.08.16 15:54
 */
public class DenormalizedEvaluatorFactory implements EvaluatorFactory {
	private static final Map<EvaluatorType, Evaluator> EVALUATORS;
	static {
		//we keep references to instances of EVALUATORS here
		//so they're practically singletons
		//any valid Evaluator must be stateless and thread-safe

		Map< EvaluatorType, Evaluator > evaluatorsMutable = new EnumMap<>( EvaluatorType.class );
		evaluatorsMutable.put( EvaluatorType.ATTACK, new AttackEvaluator() );
		evaluatorsMutable.put( EvaluatorType.CASTLING_SAFETY, new CastlingSafetyEvaluator() );
		evaluatorsMutable.put( EvaluatorType.CENTER_CONTROL, new CenterControlEvaluator() );
		evaluatorsMutable.put( EvaluatorType.TERMINAL, new TerminalEvaluator() );
		evaluatorsMutable.put( EvaluatorType.MATERIAL, new MaterialEvaluator() );
		evaluatorsMutable.put( EvaluatorType.MOBILITY, new MobilityEvaluator() );
		evaluatorsMutable.put( EvaluatorType.PROTECTION, new ProtectionEvaluator() );
		evaluatorsMutable.put( EvaluatorType.SPECIAL_MOVE, new SpecialMoveEvaluator() );

		EVALUATORS = Maps.immutableEnumMap( evaluatorsMutable );
	}

	@Override
	public Evaluator get( EvaluatorType type ) {
		return EVALUATORS.get( type );
	}
}
