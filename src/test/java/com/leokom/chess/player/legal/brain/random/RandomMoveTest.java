package com.leokom.chess.player.legal.brain.random;

import com.leokom.chess.engine.Move;
import org.junit.Test;

import java.util.HashSet;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

public class RandomMoveTest {
    @Test
    public void singleMoveSelectable() {
        Move expectedMove = new Move("e2", "e4");
        Optional<Move> result = new RandomMove(max -> 0).select(new HashSet<>(singletonList(expectedMove)));

        assertTrue( result.isPresent() );
        assertEquals( expectedMove, result.get() );
    }
}