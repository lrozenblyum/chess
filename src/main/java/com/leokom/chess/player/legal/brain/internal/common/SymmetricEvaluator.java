package com.leokom.chess.player.legal.brain.internal.common;

import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.legal.brain.common.SideEvaluator;

public class SymmetricEvaluator implements PositionEvaluator {
    private final SideEvaluator sideEvaluator;

    public SymmetricEvaluator(SideEvaluator sideEvaluator) {
        this.sideEvaluator = sideEvaluator;
    }

    /**
     * Evaluate the position 'symmetrically' from point of view of the player
     * who reached this position (by evaluating BOTH sides)
     * @param target target position
     * @return symmetrical position evaluation
     */
    @Override
    public double evaluate( Position target ) {
        Side ourSide = target.getSideToMove().opposite();

        return sideEvaluator.evaluatePosition( target, ourSide ) - sideEvaluator.evaluatePosition( target, ourSide.opposite() );
    }
}
