package com.leokom.games.commons.brain;

import com.leokom.games.commons.engine.GameState;
import com.leokom.games.commons.engine.GameTransition;

/**
 * Generic game-agnostic evaluator
 * @param <S> game state
 * @param <T> transition type
 */
public interface GenericEvaluator< S extends GameState<T, S>, T extends GameTransition> {
    double evaluateMove( S position, T move );
}
