package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.brain.common.GenericEvaluator;

import static org.junit.Assert.assertTrue;

class NormalizedEvaluatorAssert {
    private static final Range RANGE = new Range( 0.0, 1.0 );
    private final GenericEvaluator< Position, Move> evaluator;

    NormalizedEvaluatorAssert( GenericEvaluator< Position, Move> evaluator ) {
        this.evaluator = evaluator;
    }

    void assertAllMovesEvaluatedInNormalizedRange( Position position ) {
        position.getMoves().forEach(move -> {
            double value = evaluator.evaluateMove( position, move );
            assertTrue( String.format( "Evaluator %s returned value %s outside of normalized range for move: %s ",
                    evaluator.getClass().getSimpleName(), value, move ),
                    RANGE.contains( value ));
        } );
    }
}
