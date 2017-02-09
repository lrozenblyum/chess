package com.leokom.chess.player.simple;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.evaluator.common.DecisionMaker;

import java.util.Optional;

/**
 * Brain that implements Simple player in Legal player's infrastructure
 * Created by Leonid on 09.02.17.
 */
public class SimpleBrains implements DecisionMaker {
    @Override
    public Optional< Move > findBestMove( Position position ) {
        return Optional.empty();
    }
}
