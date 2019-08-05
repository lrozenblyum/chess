package com.leokom.chess.player.legal.brain.common;

import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;

@FunctionalInterface
public interface SideEvaluator {
    /**
     * Evaluate position from point of view of the given side
     * @param position position to analyze
     * @param side side of interest
     * @return a position evaluation
     */
    double evaluatePosition( Position position, Side side );
}
