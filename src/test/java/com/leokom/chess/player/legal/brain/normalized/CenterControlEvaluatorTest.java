package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.Position;
import org.junit.Before;
import org.junit.Test;

//normalized specific test
public class CenterControlEvaluatorTest {
    private CenterControlEvaluator evaluator;

    @Before
    public void prepare() {
        evaluator = new CenterControlEvaluator();
    }

    @Test
    public void allMovesShouldBeInNormalizedRange() {
        //executing start move because it will create center control for whites
        //thus blacks evaluation may become negative
        Position position = Position.getInitialPosition().move( "e2", "e4" );

        new NormalizedEvaluatorAssert( evaluator ).assertAllMovesEvaluatedInNormalizedRange( position );
    }
}