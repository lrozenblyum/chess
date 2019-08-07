package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.legal.brain.common.Brain;
import com.leokom.chess.player.legal.brain.common.Evaluator;
import com.leokom.chess.player.legal.brain.common.GenericBrain;
import com.leokom.chess.player.legal.brain.normalized.MasterEvaluator;
import com.leokom.chess.player.legal.brain.normalized.NormalizedBrain;

import java.util.function.Supplier;

public class NormalizedBrainSupplier implements Supplier<GenericBrain<Position, Move>> {
    //this depth has been used for years
    private static final int DEFAULT_DEPTH = 1;
    private final int depth;

    public NormalizedBrainSupplier() {
        this(DEFAULT_DEPTH);
    }

    public NormalizedBrainSupplier(int depth ) {
        this.depth = depth;
    }

    public GenericBrain<Position, Move> get() {
        return new NormalizedBrain<>( new MasterEvaluator(), depth );
    }
}
