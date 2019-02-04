package com.leokom.chess;

import com.leokom.chess.player.Player;
import com.leokom.chess.player.legal.LegalPlayer;
import com.leokom.chess.player.legal.brain.normalized.MasterEvaluator;
import com.leokom.chess.player.legal.brain.normalized.NormalizedBrain;

import java.util.function.Supplier;

class LegalPlayerSupplier implements Supplier<Player> {
    //this depth has been used for years
    private static final int DEFAULT_DEPTH = 1;
    private final int depth;

    public LegalPlayerSupplier() {
        this(DEFAULT_DEPTH);
    }

    public LegalPlayerSupplier( int depth ) {
        this.depth = depth;
    }

    public Player get() {
        return new LegalPlayer( new NormalizedBrain<>( new MasterEvaluator(), depth ) );
    }
}
