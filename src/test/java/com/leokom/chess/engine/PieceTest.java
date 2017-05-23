package com.leokom.chess.engine;

import org.junit.Test;
import org.mutabilitydetector.unittesting.MutabilityAssert;

import static org.junit.Assert.*;

/**
 * Make Piece immutable, with help of tests
 * Created by Leonid on 23.05.17.
 */
public class PieceTest {
    @Test
    public void pieceIsImmutable() {
        MutabilityAssert.assertImmutable( Piece.class );
    }
}