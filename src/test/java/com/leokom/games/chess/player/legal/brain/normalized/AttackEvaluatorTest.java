package com.leokom.games.chess.player.legal.brain.normalized;

import com.leokom.games.chess.engine.Position;
import org.junit.Before;
import org.junit.Test;

//normalized specific test
public class AttackEvaluatorTest {
    private AttackEvaluator evaluator;

    @Before
    public void prepare() {
        evaluator = new AttackEvaluator();
    }

    @Test
    public void allMovesShouldBeInNormalizedRange() {
        Position position = Position.getInitialPosition().move( "e2", "e4" );

        new NormalizedEvaluatorAssert( evaluator ).assertAllMovesEvaluatedInNormalizedRange( position );
    }
}
