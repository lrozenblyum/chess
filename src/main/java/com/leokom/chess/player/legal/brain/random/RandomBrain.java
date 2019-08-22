package com.leokom.chess.player.legal.brain.random;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.brain.common.Brain;

import java.util.List;

public class RandomBrain implements Brain {
    @Override
    public List<Move> findBestMove(Position position) {
        return null;
    }

    //at the moment move for the opponent is not generated, although it's possible and may bring
    //some good results
}
