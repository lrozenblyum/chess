package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.GameState;
import com.leokom.chess.engine.GameTransition;
import com.leokom.chess.engine.Move;
import com.leokom.chess.player.legal.brain.common.GenericBrain;
import com.leokom.chess.player.legal.brain.common.GenericEvaluator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
public class NormalizedBrain < StateType extends GameState< TransitionType, StateType >, TransitionType extends GameTransition> implements GenericBrain< StateType, TransitionType > {
	//this constant will increase with chess evolution
	private static final int MAXIMAL_SUPPORTED_DEPTH = 2;
	//this is an absolute constant
	private static final int MINIMAL_POSSIBLE_DEPTH = 1;
	private final GenericEvaluator< StateType, TransitionType > brains;
	private final int pliesDepth;

	/**
	 * Create normalized brain
	 * @param brains evaluator with results in [ 0, 1 ] range
	 */
	public NormalizedBrain( GenericEvaluator< StateType, TransitionType > brains ) {
		this( brains, 1 );
	}

	/**
	 * We assume that brains always evaluate the move from the side to move the next ply
	 * It was a logical assumption when we developed a 1-ply engine.
	 * It can still be kept.
	 * The alternative could be: stable evaluator that returns positive/negative result depending on color of the side to move
	 *
	 * @param brains evaluator with results in [ 0, 1 ] range
	 * @param pliesDepth depth to think
	 */
	public NormalizedBrain( GenericEvaluator< StateType, TransitionType > brains, int pliesDepth ) {
		if ( pliesDepth < MINIMAL_POSSIBLE_DEPTH) {
			throw new IllegalArgumentException( String.format( "This depth is wrong: %s", pliesDepth ) );
		}

		if ( pliesDepth > MAXIMAL_SUPPORTED_DEPTH) {
			throw new IllegalArgumentException( String.format( "This depth is not supported yet: %s", pliesDepth ) );
		}

		this.brains = new ValidatingNormalizedEvaluator<>( brains );
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
		Map<TransitionType, Double> moveRatings = new HashMap<>();

		if ( pliesDepth == 1 ) {
			//filtering Draw offers till #161 is solved
			//this looks safe since Offer draw cannot be a single legal move in a position.

			//the best place to filter is this decision maker because it's used both by Normalized and Denormalized branches
			getMovesWithoutDrawOffer(position).forEach( move ->
				moveRatings.put(move, brains.evaluateMove( position, move ) )
			);
		}
		else { //just 2 is supported now
			getMovesWithoutDrawOffer( position ).forEach( move -> {
				ThreadContext.put( "moveBeingAnalyzed", move.toString() );

				StateType target = position.move( move );
				List<TransitionType> bestMove = new NormalizedBrain<>(this.brains, 1).findBestMove(target);

				//can be empty in case of terminal position
				if ( ! bestMove.isEmpty() ) {
					//negating because bigger for the opponents means worse for the current player
					//TODO: what if > 1
					moveRatings.put(move, -brains.evaluateMove(target, bestMove.get(0)));
				} else {
					LogManager.getLogger().info( "Evaluating just the current level" );
					//trick: moving our evaluation results from [ 0, 1 ] to [ -1, 0 ] range
					// highly depends on MasterEvaluator [ 0, 1 ]!
					//where all the second level moves exist
					moveRatings.put( move, brains.evaluateMove( position, move ) - 1 ); //falling back to 1'st level
				}

				LogManager.getLogger().info( "result = {}", moveRatings.get( move ) );
				ThreadContext.clearAll();
			} );
		}

		return getMoveWithMaxRating(moveRatings);
	}

	@Override
	public String name() {
		return String.format( "NormalizedBrain: %s depth", pliesDepth );
	}

	private Stream<TransitionType> getMovesWithoutDrawOffer(StateType position) {
		return position.getMoves().stream().filter(move -> move != Move.OFFER_DRAW);
	}

	private List<TransitionType> getMoveWithMaxRating( Map< TransitionType, Double > moveValues ) {
		return moveValues.entrySet().stream()
				.max(Map.Entry.comparingByValue())
				.map(Map.Entry::getKey)
				.map( Collections::singletonList )
				.orElseGet( Collections::emptyList );
	}
}
