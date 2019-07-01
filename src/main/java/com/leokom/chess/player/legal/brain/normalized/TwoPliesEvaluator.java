package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.GameState;
import com.leokom.chess.engine.GameTransition;
import com.leokom.chess.player.legal.brain.common.GenericEvaluator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;

import java.util.List;

/**
 * Evolution of Evaluator to support thinking for 2 plies
 * @param <S>
 * @param <T>
 */
public class TwoPliesEvaluator< S extends GameState<T, S>, T extends GameTransition> implements GenericEvaluator< S, T > {

    private final GenericEvaluator<S, T> evaluator;
    private final NormalizedBrain<S, T> brain;

    TwoPliesEvaluator(GenericEvaluator<S, T> evaluator) {
        this.evaluator = evaluator;
        this.brain = new NormalizedBrain<>(this.evaluator, 1);
    }

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
}
