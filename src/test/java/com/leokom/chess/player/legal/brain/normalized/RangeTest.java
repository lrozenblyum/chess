package com.leokom.chess.player.legal.brain.normalized;

import org.junit.Test;

import static org.junit.Assert.*;

public class RangeTest {
    @Test
    public void convertValue() {
        //middle value
        assertEquals( 150, new Range( 0.0, 1.0 ).convert( new Range( 100.0, 200.0 ), 0.5 ), 0.0 );
    }

    @Test( expected = IllegalArgumentException.class )
    public void convertingValueOutOfRangeUnsupported() {
        //11.0 is not inside [0.0, 1.0]
        new Range( 0.0, 1.0 ).convert( new Range( 10.0, 20.0 ), 11.0 );
    }

    @Test( expected = IllegalArgumentException.class )
    public void zeroRangeNotSupported() {
        new Range( 1.0, 1.0 );
    }
}