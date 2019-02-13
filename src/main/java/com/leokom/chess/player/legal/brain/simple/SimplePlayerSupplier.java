package com.leokom.chess.player.legal.brain.simple;

import com.leokom.chess.player.Player;
import com.leokom.chess.player.legal.LegalPlayer;

import java.util.function.Supplier;

public class SimplePlayerSupplier implements Supplier<Player> {

    @Override
    public Player get() {
        return new LegalPlayer( new SimpleBrain() );
    }
}
