package com.leokom.chess.engine;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Set;

//fake implementation for test purposes
public class GameStateImpl implements GameState< GameTransitionImpl > {
    private final Map<GameTransitionImpl, GameStateImpl> tree;

    //a few constructors for simplicity
    public GameStateImpl() {
        this(ImmutableMap.of());
    }

    public GameStateImpl(GameTransitionImpl gameTransition, GameStateImpl gameState) {
        this( ImmutableMap.of( gameTransition, gameState ) );
    }

    public GameStateImpl(GameTransitionImpl gameTransition, GameStateImpl gameState, GameTransitionImpl gameTransition2, GameStateImpl gameState2) {
        this( ImmutableMap.of( gameTransition, gameState, gameTransition2, gameState2 ) );
    }

    private GameStateImpl(Map<GameTransitionImpl, GameStateImpl> tree ) {
        this.tree = tree;
    }

    @Override
    public GameState<GameTransitionImpl> move(GameTransitionImpl move) {
        return tree.get(move);
    }

    public Set<GameTransitionImpl> getMoves() {
        return tree.keySet();
    }
}
