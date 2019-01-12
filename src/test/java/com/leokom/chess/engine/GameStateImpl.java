package com.leokom.chess.engine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//fake implementation for test purposes
public class GameStateImpl implements GameState< GameTransitionImpl > {
    private final Set<GameTransitionImpl> transitions;

    public GameStateImpl(GameTransitionImpl ... transitions ) {
        this( new HashSet<>(Arrays.asList( transitions )));
    }

    private GameStateImpl(Set<GameTransitionImpl> transitions) {
        this.transitions = transitions;
    }

    @Override
    public GameState<GameTransitionImpl> move(GameTransitionImpl move) {
        return null;
    }

    public Set<GameTransitionImpl> getMoves() {
        return transitions;
    }
}
