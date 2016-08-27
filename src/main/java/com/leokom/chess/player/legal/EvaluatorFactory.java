package com.leokom.chess.player.legal;

/**
 * Author: Leonid
 * Date-time: 27.08.16 13:28
 */
interface EvaluatorFactory {
	Evaluator get( EvaluatorType type );
}
