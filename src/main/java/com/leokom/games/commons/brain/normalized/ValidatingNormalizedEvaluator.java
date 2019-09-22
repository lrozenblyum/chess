package com.leokom.games.commons.brain.normalized;

import com.leokom.games.commons.engine.GameState;
import com.leokom.games.commons.engine.GameTransition;
import com.leokom.games.commons.brain.GenericEvaluator;

/**
 * Evaluator delegate that ensures [ 0, 1 ] constraint for the move
 * @param <S> game state
 * @param <T> transition type
 */
class ValidatingNormalizedEvaluator < S extends GameState<T, S>, T extends GameTransition>  implements GenericEvaluator<S, T> {
    private final GenericEvaluator<S, T> delegate;

    ValidatingNormalizedEvaluator(GenericEvaluator<S, T> delegate ) {
        this.delegate = delegate;
    }

    @Override
    public double evaluateMove(S position, T move) {
        double result = delegate.evaluateMove(position, move);
        if ( result < 0.0 || result > 1.0 ) {
            throw new IllegalArgumentException( String.format( "The value is outside of supported range: %s", result ) );
        }
        return result;
    }
}
