package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;

/**
 * Author: Leonid
 * Date-time: 21.10.14 23:03
 */
public class ProtectionEvaluator implements Evaluator {
	@Override
	public double evaluateMove( Position position, Move move ) {
		return 0;
	}
}
