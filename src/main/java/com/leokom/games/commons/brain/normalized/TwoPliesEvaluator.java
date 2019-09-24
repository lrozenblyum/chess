package com.leokom.games.commons.brain.normalized;

import com.leokom.games.commons.brain.GenericBrain;
import com.leokom.games.commons.engine.GameState;
import com.leokom.games.commons.engine.GameTransition;
import com.leokom.games.commons.brain.GenericEvaluator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;

import java.util.List;
import java.util.function.Function;

/**
 * Evolution of GenericEvaluator to support thinking for 2 plies.
 * @param <S>
 * @param <T>
 */
public class TwoPliesEvaluator< S extends GameState<T, S>, T extends GameTransition> implements GenericEvaluator< S, T > {

    private final GenericEvaluator<S, T> evaluator;
    private final GenericBrain<S, T> brain;

    public < E extends GenericEvaluator< S, T >> TwoPliesEvaluator( E evaluator, Function< E, GenericBrain< S, T > > singlePlyBrainCreator ) {
        this.evaluator = evaluator;
        this.brain = singlePlyBrainCreator.apply( evaluator );
    }

    /**
     * Evaluate move from the position, keeping normalized constraints
     * @param position source position
     * @param move move to evaluate
     * @return move estimation in [0, 1]
     */
    @Override
    public double evaluateMove(S position, T move) {
        ThreadContext.put( "moveBeingAnalyzed", move.toString() );

        S target = position.move( move );

        List<T> bestMove = brain.findBestMove(target);

        //can be empty in case of terminal position
        if ( bestMove.isEmpty() ) {
            LogManager.getLogger().info( "Evaluating just the current level" );
        }

        double moveRating = bestMove.isEmpty() ?
                //falling back to the 1'st level which is already normalized
                evaluator.evaluateMove( position, move ):
                //subtracting from 1 because bigger for the opponent means worse for the current player
                //this subtraction keeps us in [0, 1] range which is guaranteed by ValidatingNormalizedEvaluator
                //composite moves handling split to https://github.com/lrozenblyum/chess/issues/291
                1 - evaluator.evaluateMove(target, bestMove.get(0));

        LogManager.getLogger().info( "result = {}", moveRating );
        ThreadContext.clearAll();
        return moveRating;
    }
}
