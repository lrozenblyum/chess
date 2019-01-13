package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.GameStateImpl;
import com.leokom.chess.engine.GameTransitionImpl;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class NormalizedBrainTest {
    @Test
    public void noMovesNoBestMove() {
        GameStateImpl gameState = new GameStateImpl();

        List<GameTransitionImpl> result = new NormalizedBrain< GameStateImpl, GameTransitionImpl >((state, transition) -> 0).findBestMove(gameState);
        assertTrue( result.isEmpty() );
    }

    @Test
    public void singlePossibleMoveReturned() {
        int moveId = 12345;
        GameStateImpl gameState = new GameStateImpl( new GameTransitionImpl(moveId), new GameStateImpl() );

        List<GameTransitionImpl> result = new NormalizedBrain<GameStateImpl, GameTransitionImpl>((state, transition) -> 0).findBestMove(gameState);
        assertEquals( 1, result.size() );
        assertEquals( moveId, result.get(0).getId() );
    }

    @Test
    public void betterMoveFound() {
        GameStateImpl gameState = new GameStateImpl( new GameTransitionImpl(12), new GameStateImpl(),
                new GameTransitionImpl( 20 ), new GameStateImpl() );

        List<GameTransitionImpl> result = new NormalizedBrain< GameStateImpl, GameTransitionImpl >(
            (state, transition) -> transition.getId() // just a simple evaluation - let's say bigger id is better
        ).findBestMove(gameState);
        assertEquals( 1, result.size() );
        assertEquals( 20, result.get(0).getId() );
    }
}