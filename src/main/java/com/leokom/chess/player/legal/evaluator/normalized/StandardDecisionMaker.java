package com.leokom.chess.player.legal.evaluator.normalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.evaluator.common.DecisionMaker;
import com.leokom.chess.player.legal.evaluator.common.Evaluator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Initial decision maker based on MasterEvaluator
 * independent evaluation of each move is delegated to MasterEvaluator
 *
 * Author: Leonid
 * Date-time: 23.08.16 22:54
 */
public class StandardDecisionMaker implements DecisionMaker {
	private Evaluator brains;

	public StandardDecisionMaker() {
		this( new MasterEvaluator() );
	}

	public StandardDecisionMaker( Evaluator brains ) {
		this.brains = brains;
	}

	/**
	 * If there are several 'best' moves, one of them is returned.
	 * (which one of them is chosen is undefined.
	 * This selection is not guaranteed to be the same under same circumstances)

	 * @inheritDoc
	 * @param position
	 * @return the best move
	 */
	/*
	 * Thanks to possibility to have different results for the same possible moves
	 * we have a some kind of randomness without explicitly
	 * creating it.
	 * That means : even in the same position the current algorithm
	 * can select a different move (most likely thanks to unordered
	 * nature of Set)
	 *
	 */
	@Override
	public Optional<Move> findBestMove( Position position ) {
		Map< Move, Double > moveRatings = new HashMap<>();
		for ( Move move : position.getMoves() ) {
			moveRatings.put( move, brains.evaluateMove( position, move ) );
		}

		return getMoveWithMaxRating( moveRatings );
	}

	private Optional<Move> getMoveWithMaxRating( Map< Move, Double > moveValues ) {
		return
				moveValues.entrySet().stream()
						.sorted( Map.Entry.< Move, Double >comparingByValue().reversed() )
						.findFirst().map( Map.Entry::getKey );
	}
}
