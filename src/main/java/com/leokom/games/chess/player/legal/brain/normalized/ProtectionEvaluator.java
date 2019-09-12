package com.leokom.games.chess.player.legal.brain.normalized;

import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.engine.Position;
import com.leokom.games.chess.player.legal.brain.common.Evaluator;
import com.leokom.games.chess.player.legal.brain.common.EvaluatorType;
import com.leokom.games.chess.player.legal.brain.denormalized.DenormalizedEvaluatorFactory;

/**
 * Author: Leonid
 * Date-time: 21.10.14 23:03
 */
class ProtectionEvaluator implements Evaluator {
	//we may use a more precise dynamic maximal value (count of our pieces physically on board * (count-1) )
	//may be improved in https://github.com/lrozenblyum/chess/issues/327

	private static final int MAX_COUNT_OF_PIECES_FOR_SIDE = 16;
	//the formula is based on the fact that we're calculating sum (protection indices for every piece) which in turn may be equal maximally (count-1)
	private static final int MAX_PROTECTION_INDEX = MAX_COUNT_OF_PIECES_FOR_SIDE * (MAX_COUNT_OF_PIECES_FOR_SIDE - 1);
	private static final SymmetricalNormalizedRange RANGE = new SymmetricalNormalizedRange( 0.0, MAX_PROTECTION_INDEX );

	@Override
	public double evaluateMove( Position position, Move move ) {
		return RANGE.normalize(
				new DenormalizedEvaluatorFactory().get( EvaluatorType.PROTECTION )
				.evaluateMove( position, move ) );
	}
 }