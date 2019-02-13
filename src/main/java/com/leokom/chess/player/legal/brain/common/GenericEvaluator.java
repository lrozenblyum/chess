package com.leokom.chess.player.legal.brain.common;

import com.leokom.chess.engine.GameState;
import com.leokom.chess.engine.GameTransition;

/**
 * Generic evaluator, actually game-agnostic
 * @param <S> game state
 * @param <T> transition type
 */
public interface GenericEvaluator< S extends GameState<T, S>, T extends GameTransition > {
    double evaluateMove( S position, T move );
}
