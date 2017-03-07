package com.leokom.chess.player.legal.evaluator.normalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.evaluator.common.DecisionMaker;
import com.leokom.chess.player.legal.evaluator.common.Evaluator;

import java.util.*;

/**
 * Initial decision maker based on MasterEvaluator.
 * independent evaluation of each move is delegated to MasterEvaluator.
 * You can inject any custom brains instead of MasterEvaluator via constructor.
 *
 * Author: Leonid
 * Date-time: 23.08.16 22:54
 */
public class NormalizedDecisionMaker implements DecisionMaker {
	private Evaluator brains;

	public NormalizedDecisionMaker() {
		this( new MasterEvaluator() );
	}

	public NormalizedDecisionMaker( Evaluator brains ) {
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
	public List<Move> findBestMove(Position position ) {
		Map< Move, Double > moveRatings = new HashMap<>();

		//filtering Draw offers till #161 is solved
		//this looks safe since Offer draw cannot be a single legal move in a position.

		//the best place to filter is this decision maker because it's used both by Normalized and Denormalized branches
		position.getMoves().stream().filter( move -> move != Move.OFFER_DRAW ).forEach( move ->
			moveRatings.put( move, brains.evaluateMove( position, move ) )
		);

		return getMoveWithMaxRating( moveRatings );
	}

	private List<Move> getMoveWithMaxRating( Map< Move, Double > moveValues ) {
		Optional<Move> bestMove = moveValues.entrySet().stream()
				.sorted(Map.Entry.<Move, Double>comparingByValue().reversed())
				.findFirst()
				.map(Map.Entry::getKey);
		return bestMove.isPresent() ?
				Collections.singletonList( bestMove.get() ) :
				Collections.emptyList();
	}
}
