package com.leokom.chess.engine;

//fake implementation for test purposes
public class GameTransitionImpl implements com.leokom.chess.engine.GameTransition {
    private final long id;

    public GameTransitionImpl(long id ) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }
}
