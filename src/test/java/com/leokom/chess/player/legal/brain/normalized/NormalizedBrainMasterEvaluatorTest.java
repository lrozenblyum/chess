package com.leokom.chess.player.legal.brain.normalized;


import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotEquals;

//it's move integration than NormalizedBrainPositionMoveTest
public class NormalizedBrainMasterEvaluatorTest {
    @Test
    public void resignIsNotTheBest() {
        List<Move> bestMove = new NormalizedBrain<>(new MasterEvaluator(), 2)
                .findBestMove(Position.getInitialPosition());
        assertNotEquals( Move.RESIGN, bestMove.get(0) );
    }

    @Test
    public void drawOfferIsNotTheBest() {
        List<Move> bestMove = new NormalizedBrain<>(new MasterEvaluator(), 2)
                .findBestMove(Position.getInitialPosition());
        assertNotEquals( Move.OFFER_DRAW, bestMove.get(0) );
    }

    //it's not a strict requirement. I just want to exclude draw offer from the algorithm
    //we're not ready for it yet.
    //TODO: extract draw offer 2'nd level evaluation to a separate ticket
    @Test
    public void drawOfferIsNotTheBestResponse() {
        List<Move> bestMove = new NormalizedBrain<>(new MasterEvaluator(), 2)
                .findBestMove(Position.getInitialPosition().move( "e2", "e4" ));
        assertNotEquals( Move.OFFER_DRAW, bestMove.get(0) );
    }
}
