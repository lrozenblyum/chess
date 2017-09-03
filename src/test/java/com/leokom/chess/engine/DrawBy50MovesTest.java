package com.leokom.chess.engine;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DrawBy50MovesTest {
    @Test
    public void claimDrawImpossibleFromInitialPosition() {
        PositionAsserts.assertAllowedMovesOmit(
            Position.getInitialPosition(),
            Move.CLAIM_DRAW
        );
    }

    @Test
    public void claimDrawStringRepresentation() {
        assertEquals( "CLAIM_DRAW", Move.CLAIM_DRAW.toString() );
    }
}
