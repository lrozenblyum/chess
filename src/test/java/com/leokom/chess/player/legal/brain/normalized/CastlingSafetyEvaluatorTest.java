package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.Position;
import org.junit.Before;
import org.junit.Test;

//normalized specific test
public class CastlingSafetyEvaluatorTest {
    private CastlingSafetyEvaluator evaluator;

    @Before
    public void prepare() {
        evaluator = new CastlingSafetyEvaluator();
    }

    @Test
    public void allMovesShouldBeInNormalizedRange() {
        //preparing a position closer to castling
        Position position = Position.getInitialPosition().
                move( "e2", "e4" ).
                move( "e7", "e5" ).
                move( "g1", "f3" ).
                move( "g8", "f6" );

        new NormalizedEvaluatorAssert( evaluator ).assertAllMovesEvaluatedInNormalizedRange( position );
    }
}