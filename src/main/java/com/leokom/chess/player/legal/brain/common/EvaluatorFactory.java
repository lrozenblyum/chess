package com.leokom.chess.player.legal.brain.common;

/**
 * Author: Leonid
 * Date-time: 27.08.16 13:28
 */
public interface EvaluatorFactory {
	Evaluator get( EvaluatorType type );
}
