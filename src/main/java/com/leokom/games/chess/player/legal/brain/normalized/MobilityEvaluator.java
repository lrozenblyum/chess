package com.leokom.games.chess.player.legal.brain.normalized;

import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.engine.Position;
import com.leokom.games.chess.player.legal.brain.common.Evaluator;
import com.leokom.games.chess.player.legal.brain.common.EvaluatorType;
import com.leokom.games.chess.player.legal.brain.denormalized.DenormalizedEvaluatorFactory;
import com.leokom.games.commons.brain.normalized.range.SymmetricalNormalizedRange;

/**
 * If inside a position there is a bigger variety of moves
 * we consider this as better
 *
 * Author: Leonid
 * Date-time: 23.07.14 21:46
 */
class MobilityEvaluator implements Evaluator {
	//TODO: calculate theoretical max. possible moves count in a position (9 promoted queens + all others? )
	private static final int MAXIMAL_POSSIBLE_MOVES = 1000;
	private static final int MINIMAL_POSSIBLE_MOVES = 0;

	private static final SymmetricalNormalizedRange RANGE = new SymmetricalNormalizedRange( MINIMAL_POSSIBLE_MOVES, MAXIMAL_POSSIBLE_MOVES );

	@Override
	public double evaluateMove( Position position, Move move ) {
		double denormalized = new DenormalizedEvaluatorFactory().get( EvaluatorType.MOBILITY )
				.evaluateMove(position, move);
		return RANGE.normalize( denormalized );
	}
}
