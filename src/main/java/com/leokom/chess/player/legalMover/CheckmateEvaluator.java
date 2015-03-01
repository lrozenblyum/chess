package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;

/**
 * Author: Leonid
 * Date-time: 01.03.15 22:32
 *
 * Checkmate is the highest goal of the whole game
 */
public class CheckmateEvaluator implements Evaluator {
	@Override
	public double evaluateMove( Position position, Move move ) {
		return position.move( move ).isTerminal() ? 1 : 0;
	}
}
