package com.leokom.chess.player.legal.brain.random;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.*;

public class RandomBrainTest {
    @Test
    public void brainExistence() {
        assertEquals( "RandomBrain", new RandomBrain().name() );
    }

    @Test
    public void minimalConsistency() {
        RandomBrain brain = new RandomBrain();
        List<Move> bestMoves = brain.findBestMove( Position.getInitialPosition() );
        assertThat( Position.getInitialPosition().getMoves(), hasItem( bestMoves.get( 0 ) ) );
    }
}