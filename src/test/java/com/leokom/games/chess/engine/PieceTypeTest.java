package com.leokom.games.chess.engine;

import org.junit.Test;
import org.mutabilitydetector.unittesting.MutabilityAssert;

/**
 * Check statements about piece type
 * Created by Leonid on 23.05.17.
 */
public class PieceTypeTest {
    @Test
    public void immutable() {
        MutabilityAssert.assertImmutable( PieceType.class );
    }
}