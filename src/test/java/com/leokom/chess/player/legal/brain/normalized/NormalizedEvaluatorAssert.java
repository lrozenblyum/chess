package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.brain.common.Evaluator;
import org.junit.Assert;

class NormalizedEvaluatorAssert {
    private final Evaluator evaluator;

    NormalizedEvaluatorAssert( Evaluator evaluator ) {
        this.evaluator = evaluator;
    }

    void assertAllMovesEvaluatedInNormalizedRange( Position position ) {
        position.getMoves().forEach(move -> {
            double value = evaluator.evaluateMove( position, move );
            if ( value < 0.0 || value > 1.0 ) {
                Assert.fail( String.format( "Evaluator %s returned value %s outside of normalized range for move: %s ",
                    evaluator.getClass().getSimpleName(), value, move ) );
            }
        } );
    }
}
