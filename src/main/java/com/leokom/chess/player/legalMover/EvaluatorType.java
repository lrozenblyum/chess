package com.leokom.chess.player.legalMover;

/**
 * Represent known way to analyze a position.
 * Every instance of it is some part of our brain we'd like to use to analyze a particular
 * position move.
 * Author: Leonid
 * Date-time: 19.04.16 22:46
 */
public enum EvaluatorType {
	//we keep references to instances of evaluators here
	//so they're practically singletons
	//any valid Evaluator must be stateless and thread-safe
	CHECKMATE( new CheckmateEvaluator() ),
	CASTLING_SAFETY( new CastlingSafetyEvaluator() ),
	CENTER_CONTROL( new CenterControlEvaluator() ),
	MATERIAL( new MaterialEvaluator() ),
	MOBILITY( new MobilityEvaluator() ),
	PROTECTION( new ProtectionEvaluator() ),
	SPECIAL_MOVE( new SpecialMoveEvaluator() );

	private final Evaluator evaluator;

	Evaluator getEvaluator() {
		return evaluator;
	}

	EvaluatorType( Evaluator evaluator ) {
		this.evaluator = evaluator;
	}
}
