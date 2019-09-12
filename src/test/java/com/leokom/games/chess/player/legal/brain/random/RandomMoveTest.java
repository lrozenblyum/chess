package com.leokom.games.chess.player.legal.brain.random;

import com.leokom.games.chess.engine.Move;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedHashSet;
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

    //deterministic expectations from random approach
    @Test
    public void exactMoveSelectedFromMiddle() {
        Move expected = new Move("g3", "g4");

        LinkedHashSet<Move> moves = new LinkedHashSet<>();
        moves.add( new Move( "e2", "e4" ) );
        moves.add( new Move( "d2", "d4" ) );
        moves.add( expected );
        moves.add( new Move( "a2", "a4" ) );

        Optional<Move> result = new RandomMove(size -> 2).select(moves);

        assertTrue( result.isPresent() );
        assertEquals( expected, result.get() );
    }

    //deterministic expectations from random approach
    @Test
    public void exactMoveSelectedFromMax() {
        Move expected = new Move("a2", "a4");
        LinkedHashSet<Move> moves = new LinkedHashSet<>();
        moves.add( new Move( "e2", "e4" ) );
        moves.add( new Move( "d2", "d4" ) );
        moves.add( new Move("g3", "g4") );
        moves.add( expected );

        Optional<Move> result = new RandomMove(size -> 3).select(moves);

        assertTrue( result.isPresent() );
        assertEquals( expected, result.get() );
    }
}