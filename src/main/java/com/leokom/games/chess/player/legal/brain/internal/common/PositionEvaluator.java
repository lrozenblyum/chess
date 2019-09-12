package com.leokom.games.chess.player.legal.brain.internal.common;

import com.leokom.games.chess.engine.Position;

/*
 * Start for https://github.com/lrozenblyum/chess/issues/323
 */
public interface PositionEvaluator {
    double evaluate(Position target);
}
