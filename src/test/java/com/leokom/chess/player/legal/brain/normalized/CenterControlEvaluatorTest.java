package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.Position;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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

        position.getMoves().forEach(move -> {
            double value = evaluator.evaluateMove( position, move );
            if ( value < 0.0 || value > 1.0 ) {
                Assert.fail( String.format( "Value %s outside of normalized range for move: %s ", value, move ) );
            }
        } );
    }
}