package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.GameState;
import com.leokom.chess.engine.GameTransition;
import com.leokom.chess.player.legal.brain.common.GenericEvaluator;

/**
 * Evaluator delegate that ensures [ 0, 1 ] constraint for the move
 * @param <StateType>
 * @param <TransitionType>
 */
class ValidatingNormalizedEvaluator < StateType extends GameState< TransitionType, StateType >, TransitionType extends GameTransition>  implements GenericEvaluator< StateType, TransitionType > {
    private final GenericEvaluator<StateType, TransitionType> delegate;

    ValidatingNormalizedEvaluator(GenericEvaluator<StateType, TransitionType> delegate ) {
        this.delegate = delegate;
    }

    @Override
    public double evaluateMove(StateType position, TransitionType move) {
        double result = delegate.evaluateMove(position, move);
        if ( result < 0.0 || result > 1.0 ) {
            throw new IllegalArgumentException( String.format( "The value is outside of supported range: %s", result ) );
        }
        return result;
    }
}
