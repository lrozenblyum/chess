package com.leokom.chess.player.legal.brain.internal.common;

import com.leokom.chess.engine.Position;

/*
 * Start for https://github.com/lrozenblyum/chess/issues/323
 */
public interface PositionEvaluator {
    double evaluate(Position target);
}
