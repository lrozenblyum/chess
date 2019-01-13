package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.GameState;
import com.leokom.chess.engine.GameTransition;
import com.leokom.chess.engine.Move;
import com.leokom.chess.player.legal.brain.common.Brain;
import com.leokom.chess.player.legal.brain.common.GenericEvaluator;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Initial decision maker.
 *
 * Historically it was based on MasterEvaluator.
 * Now it has become generic (actually even not depending on chess-related notions)/
 * You can inject any custom brains via constructor.
 *
 * Author: Leonid
 * Date-time: 23.08.16 22:54
 */
public class NormalizedBrain < StateType extends GameState< TransitionType >, TransitionType extends GameTransition> implements Brain< StateType, TransitionType > {
	private final GenericEvaluator< StateType, TransitionType > brains;
	private final int pliesDepth;

	public NormalizedBrain( GenericEvaluator< StateType, TransitionType > brains ) {
		this( brains, 1 );
	}

	public NormalizedBrain( GenericEvaluator< StateType, TransitionType > brains, int pliesDepth ) {
		this.brains = brains;
		this.pliesDepth = pliesDepth;
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
	public List<TransitionType> findBestMove(StateType position ) {
		Map< TransitionType, Double > moveRatings = new HashMap<>();

		//filtering Draw offers till #161 is solved
		//this looks safe since Offer draw cannot be a single legal move in a position.

		//the best place to filter is this decision maker because it's used both by Normalized and Denormalized branches
		position.getMoves().stream().filter( move -> move != Move.OFFER_DRAW ).forEach( move ->
			moveRatings.put( move, brains.evaluateMove( position, move ) )
		);

		return getMoveWithMaxRating( moveRatings );
	}

	private List<TransitionType> getMoveWithMaxRating( Map< TransitionType, Double > moveValues ) {
		return moveValues.entrySet().stream()
				.max(Map.Entry.comparingByValue())
				.map(Map.Entry::getKey)
				.map( Collections::singletonList )
				.orElseGet( Collections::emptyList );
	}
}
