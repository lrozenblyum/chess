package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.Position;
import org.junit.Assert;
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

        position.getMoves().forEach(move -> {
            double value = evaluator.evaluateMove( position, move );
            if ( value < 0.0 || value > 1.0 ) {
                Assert.fail( String.format( "Value %s outside of normalized range for move: %s ", value, move ) );
            }
        } );
    }
}