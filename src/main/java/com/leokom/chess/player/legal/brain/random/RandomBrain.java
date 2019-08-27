package com.leokom.chess.player.legal.brain.random;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.brain.common.Brain;

import java.util.*;
import java.util.function.UnaryOperator;

public class RandomBrain implements Brain {
    private final RandomMove randomMove;
    private final UnaryOperator<Set<Move>> moveFilter;

    public RandomBrain() {
        this( new RandomMove( new Random()::nextInt ), new MoveFilter() );
    }

    private RandomBrain(RandomMove randomMove, UnaryOperator<Set< Move >> moveFilter )  {
        this.randomMove = randomMove;
        this.moveFilter = moveFilter;
    }

    @Override
    public List<Move> findBestMove( Position position ) {
        return
            randomMove.select(
                moveFilter.apply( position.getMoves() )
            )
            .map( Collections::singletonList )
            .orElse( Collections.emptyList() );
    }

    //at the moment move for the opponent is not generated, although it's possible and may bring
    //some good results
}
