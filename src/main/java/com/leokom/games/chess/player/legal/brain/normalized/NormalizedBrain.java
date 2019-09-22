package com.leokom.games.chess.player.legal.brain.normalized;

import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.engine.Position;
import com.leokom.games.chess.player.legal.brain.common.Evaluator;
import com.leokom.games.commons.brain.GenericBrain;
import com.leokom.games.commons.brain.normalized.GenericNormalizedBrain;

import java.util.List;

public class NormalizedBrain implements GenericBrain<Position, Move> {
    private final GenericNormalizedBrain<Position, Move> engine;

    public NormalizedBrain(Evaluator evaluator) {
        this( evaluator, 1 );
    }

    public NormalizedBrain(Evaluator evaluator, int depth) {
        this(
            new GenericNormalizedBrain<>(
                evaluator,
                depth,
                move -> move != Move.OFFER_DRAW //we're not supporting draw offers so far
            )
        );
    }

    private NormalizedBrain(GenericNormalizedBrain<Position, Move> engine) {
        this.engine = engine;
    }

    @Override
    public List<Move> findBestMove(Position position) {
        return engine.findBestMove( position );
    }

    @Override
    public Move findBestMoveForOpponent(Position position) {
        return engine.findBestMoveForOpponent( position );
    }

    @Override
    public String name() {
        return engine.name();
    }
}
