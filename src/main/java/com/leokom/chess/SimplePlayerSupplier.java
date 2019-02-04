package com.leokom.chess;

import com.leokom.chess.player.Player;
import com.leokom.chess.player.legal.LegalPlayer;
import com.leokom.chess.player.legal.brain.simple.SimpleBrain;

import java.util.function.Supplier;

public class SimplePlayerSupplier implements Supplier<Player> {

    @Override
    public Player get() {
        return new LegalPlayer( new SimpleBrain() );
    }
}
