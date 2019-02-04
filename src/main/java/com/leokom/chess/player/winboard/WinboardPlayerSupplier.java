package com.leokom.chess.player.winboard;

import com.leokom.chess.player.Player;
import com.leokom.chess.player.winboard.WinboardPlayer;

import java.util.function.Supplier;

public class WinboardPlayerSupplier implements Supplier<Player> {

    @Override
    public Player get() {
        return WinboardPlayer.create();
    }
}
