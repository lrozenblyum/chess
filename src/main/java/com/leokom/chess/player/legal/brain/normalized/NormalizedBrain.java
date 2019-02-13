package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.GameState;
import com.leokom.chess.engine.GameTransition;
import com.leokom.chess.engine.Move;
import com.leokom.chess.player.legal.brain.common.GenericBrain;
import com.leokom.chess.player.legal.brain.common.GenericEvaluator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * Initial decision maker.
 *
 * Historically it was based on MasterEvaluator.
 * Now it has become generic (actually even not depending on chess-related notions).
 * You can inject any custom evaluator that acts as a normalized one via constructor.
 *
 * We assume that the evaluator always evaluates the move from the side to move the next ply
 * It was a logical assumption when we developed a 1-ply engine.
 * It can still be kept.
 * The alternative could be: stable evaluator that returns positive/negative result depending on color of the side to move
 *
 * @param <S> game state
 * @param <T> transition type
 *
 * Author: Leonid
 * Date-time: 23.08.16 22:54
 */
public class NormalizedBrain < S extends GameState<T, S>, T extends GameTransition> implements GenericBrain<S, T> {
	//this constant will increase with chess evolution
	private static final int MAXIMAL_SUPPORTED_DEPTH = 2;
	//this is an absolute constant
	private static final int MINIMAL_POSSIBLE_DEPTH = 1;
	private final GenericEvaluator<S, T> evaluator;
	private final int pliesDepth;

	/**
	 * Create normalized brain
	 * @param evaluator evaluator with results in [ 0, 1 ] range
	 */
	public NormalizedBrain( GenericEvaluator<S, T> evaluator ) {
		this( evaluator, 1 );
	}

	/**
	 * Create brain with custom plies depth
	 * @param evaluator evaluator with results in [ 0, 1 ] range
	 * @param pliesDepth depth to think (1 or 2 are supported)
	 */
	public NormalizedBrain(GenericEvaluator<S, T> evaluator, int pliesDepth ) {
		if ( pliesDepth < MINIMAL_POSSIBLE_DEPTH) {
			throw new IllegalArgumentException( String.format( "This depth is wrong: %s", pliesDepth ) );
		}

		if ( pliesDepth > MAXIMAL_SUPPORTED_DEPTH) {
			throw new IllegalArgumentException( String.format( "This depth is not supported yet: %s", pliesDepth ) );
		}

		this.evaluator = new ValidatingNormalizedEvaluator<>(evaluator);
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
	public List<T> findBestMove( S position ) {
		//just 1 or 2 is supported now
		ToDoubleBiFunction< S, T > moveEvaluator = pliesDepth == 1 ?
			this::evaluateMoveViaSinglePly :
			this::evaluateMoveViaTwoPlies;

		//1. filtering Draw offers till #161 is solved
		//this looks safe since Offer draw cannot be a single legal move in a position.
		//the best place to filter is this decision maker because it's used both by Normalized and Denormalized branches

		//2. in future we may even not materialize the map and continue the Stream API chain to find the best move
		Map<T, Double> moveRatings =
			getMovesWithoutDrawOffer( position ).collect(
				toMap(
					identity(),
					move -> moveEvaluator.applyAsDouble( position, move )
				)
			);
		List<T> bestMove = getMoveWithMaxRating( moveRatings );
		LogManager.getLogger().info( "Best move(s): {}", bestMove );
		return bestMove;
	}

	private double evaluateMoveViaTwoPlies( S position, T move ) {
		ThreadContext.put( "moveBeingAnalyzed", move.toString() );

		S target = position.move( move );
		List<T> bestMove = new NormalizedBrain<>(this.evaluator, 1).findBestMove(target);

		//can be empty in case of terminal position
		if ( bestMove.isEmpty() ) {
			LogManager.getLogger().info( "Evaluating just the current level" );
		}

		double moveRating = bestMove.isEmpty() ?
				//falling back to the 1'st level
				//trick: moving our evaluation results from [ 0, 1 ] to [ -1, 0 ] range
				//where all the second level moves exist
				// highly depends on evaluator range [ 0, 1 ] which is guaranteed by ValidatingNormalizedEvaluator
				evaluator.evaluateMove(position, move) - 1 :
				//negating because bigger for the opponents means worse for the current player
				//composite moves handling split to https://github.com/lrozenblyum/chess/issues/291
				-evaluator.evaluateMove(target, bestMove.get(0));

		LogManager.getLogger().info( "result = {}", moveRating );
		ThreadContext.clearAll();
		return moveRating;
	}

	private double evaluateMoveViaSinglePly( S position, T move ) {
		return evaluator.evaluateMove( position, move );
	}

	@Override
	public String name() {
		return String.format( "NormalizedBrain: %s depth", pliesDepth );
	}

	private Stream<T> getMovesWithoutDrawOffer(S position) {
		return position.getMoves().stream().filter(move -> move != Move.OFFER_DRAW);
	}

	private List<T> getMoveWithMaxRating( Map<T, Double > moveValues ) {
		return moveValues.entrySet().stream()
				.max(Map.Entry.comparingByValue())
				.map(Map.Entry::getKey)
				.map( Collections::singletonList )
				.orElseGet( Collections::emptyList );
	}
}
