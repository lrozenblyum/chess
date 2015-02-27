package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;

/**
 * If inside a position there is a bigger variety of moves
 * we consider this as better
 *
 * Author: Leonid
 * Date-time: 23.07.14 21:46
 */
public class MobilityEvaluator implements Evaluator {

	//introduced this 'normalizer' to have
	//result in borders [0 , 1]
	//TODO: however it looks TOO limiting
	//more often than not we'll have result e.g. 0.02
	//and need big multiplier to have serious effect among other evaluators
	//possible solution:
	//execute 'normalizing' on caller's side among ACTUAL moves from the position
	//instead of doing it basing on theoretical information

	//TODO: calculate theoretical max. possible moves count in a position (9 promoted queens + all others? )
	private static final int MAXIMAL_POSSIBLE_MOVES = 1000;

	@Override
	public double evaluateMove( Position position, Move move ) {
		final Position target = position.move( move );

		final int legalMoves = target.toMirror().getMoves().size();
		return (double) legalMoves / MAXIMAL_POSSIBLE_MOVES;
	}
}
