package com.leokom.games.chess.player.legal.brain.normalized;

import com.leokom.games.chess.engine.Position;
import org.junit.Before;
import org.junit.Test;


//specific test for normalized mobility evaluator
public class MobilityEvaluatorTest {
    private MobilityEvaluator evaluator;

    @Before
    public void prepare() {
        evaluator = new MobilityEvaluator();
    }

    @Test
    public void allMovesShouldBeInNormalizedRange() {
        Position position = Position.getInitialPosition();

        new NormalizedEvaluatorAssert( evaluator ).assertAllMovesEvaluatedInNormalizedRange( position );
    }
}
