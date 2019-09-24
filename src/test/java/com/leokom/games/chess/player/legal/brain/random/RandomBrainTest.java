package com.leokom.games.chess.player.legal.brain.random;

import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.engine.Position;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.*;

public class RandomBrainTest {
    @Test
    public void brainExistence() {
        Assert.assertEquals( "RandomBrain", new RandomBrain().name() );
    }

    //it's really hard to check our player behavior
    //even if we mock random generator, set of moves has no defined order in general
    //thus it has no clear notion of 'index'
    //NOTE: mocking with some kind of ordered Set might help
    @Test
    public void minimalConsistency() {
        RandomBrain brain = new RandomBrain();
        List<Move> bestMoves = brain.findBestMove( Position.getInitialPosition() );
        assertThat( Position.getInitialPosition().getMoves(), hasItem( bestMoves.get( 0 ) ) );
    }

    @Test
    public void supportTerminalPositions() {
        RandomBrain brain = new RandomBrain();
        List<Move> bestMoves = brain.findBestMove( Position.getInitialPosition().move( Move.RESIGN ) );
        assertEquals( 0, bestMoves.size() );
    }
}