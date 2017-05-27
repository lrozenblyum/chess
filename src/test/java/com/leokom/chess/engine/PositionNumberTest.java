package com.leokom.chess.engine;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PositionNumberTest {
    @Test
    public void initialPosition() {
        assertEquals( 1, Position.getInitialPosition().getMoveNumber() );
    }

    @Test
    public void nextIsStillFirst() {
        assertEquals( 1, Position.getInitialPosition().move( new Move( "e2", "e4" ) ).getMoveNumber() );
    }

    /*
     * Single change
     * Special moves to check
     */
}
