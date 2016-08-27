package com.leokom.chess.player.legal.evaluator.denormalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.evaluator.common.DecisionMaker;

import java.util.Optional;
import java.util.Set;

/**
 * Make decisions based on evaluators from this package
 *
 * Author: Leonid
 * Date-time: 27.08.16 21:51
 */
public class DenormalizedDecisionMaker implements DecisionMaker {
	@Override
	public Optional< Move > findBestMove( Position position ) {
		final Set<Move> legalMoves = position.getMoves();
		return legalMoves.isEmpty() ? Optional.empty() :
				Optional.of( legalMoves.iterator().next() );
	}
}
