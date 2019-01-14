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
}
