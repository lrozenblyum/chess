package com.leokom.games.chess.engine;

import com.leokom.games.commons.engine.GameTransition;

//fake implementation for test purposes
public class GameTransitionImpl implements GameTransition {
    private final long id;

    public GameTransitionImpl(long id ) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }
}
