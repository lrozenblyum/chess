package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.GameState;
import com.leokom.chess.engine.GameTransition;
import com.leokom.chess.engine.GameTransitionImpl;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class NormalizedBrainTest {
    //these abstract classes are introduced to provide explicit generics and avoid type cast for Mockito
    //as per https://stackoverflow.com/a/23349216/1429367
    abstract class GenericState implements GameState< GameTransition > {
    }

    abstract class GenericImplState implements GameState<GameTransitionImpl> {
    }

    @Test
    public void noMovesNoBestMove() {
        GenericState gameState = Mockito.mock( GenericState.class );
        when( gameState.getMoves() ).thenReturn( new HashSet<>());

        List<GameTransition> result = new NormalizedBrain<>((state, transition) -> 0).findBestMove(gameState);
        assertTrue( result.isEmpty() );
    }

    @Test
    public void singlePossibleMoveReturned() {
        GenericImplState gameState = Mockito.mock( GenericImplState.class );
        int moveId = 12345;
        when( gameState.getMoves() ).thenReturn( new HashSet<>(Collections.singletonList(new GameTransitionImpl(moveId))));

        List<GameTransitionImpl> result = new NormalizedBrain< GameState< GameTransitionImpl >, GameTransitionImpl >((state, transition) -> 0).findBestMove(gameState);
        assertEquals( 1, result.size() );
        assertEquals( moveId, result.get(0).getId() );
    }

    @Test
    public void betterMoveFound() {
        GenericImplState gameState = Mockito.mock( GenericImplState.class );
        when( gameState.getMoves() ).thenReturn( new HashSet<>(Arrays.asList(
            new GameTransitionImpl(12),
            new GameTransitionImpl( 20 )
        )));

        List<GameTransitionImpl> result = new NormalizedBrain< GameState< GameTransitionImpl >, GameTransitionImpl >(
            (state, transition) -> transition.getId() // just a simple evaluation - let's say bigger id is better
        ).findBestMove(gameState);
        assertEquals( 1, result.size() );
        assertEquals( 20, result.get(0).getId() );
    }
}