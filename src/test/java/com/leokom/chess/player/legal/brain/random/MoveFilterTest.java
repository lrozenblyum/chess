package com.leokom.chess.player.legal.brain.random;

import com.leokom.chess.engine.Move;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class MoveFilterTest {
    @Test
    public void supportEmptyInput() {
        Set<Move> output = new MoveFilter().apply(new HashSet<>());
        assertTrue( output.isEmpty() );
    }

    @Test
    public void resignFiltered() {
        Set<Move> output = new MoveFilter().apply(new HashSet<>(Arrays.asList( Move.RESIGN, new Move( "e2", "e4" ) ) ));
        assertEquals( new HashSet<>(Collections.singletonList( new Move( "e2", "e4" ) ) ), output );
    }
}