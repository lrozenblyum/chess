package com.leokom.games.chess.player.legal.brain.common;

import com.leokom.games.chess.engine.GameState;
import com.leokom.games.chess.engine.GameTransition;

/**
 * Generic evaluator, actually game-agnostic
 * @param <S> game state
 * @param <T> transition type
 */
public interface GenericEvaluator< S extends GameState<T, S>, T extends GameTransition> {
    double evaluateMove( S position, T move );
}
