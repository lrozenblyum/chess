package com.leokom.games.chess.player.legal.brain.normalized;

import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.engine.Position;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;

//this test is based on Chess notions (in contrary to NormalizedBrainTest)
//so it can be considered being more integrations with Position, Move
public class NormalizedBrainPositionMoveTest {

    @Test
    public void integrationWithPosition() {
        List<Move> bestMove = new NormalizedBrain<Position, Move>(((position, move) -> 0), 2)
                .findBestMove(Position.getInitialPosition());
        assertFalse( bestMove.isEmpty() );
    }
}
