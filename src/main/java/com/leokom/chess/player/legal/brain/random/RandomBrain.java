package com.leokom.chess.player.legal.brain.random;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.brain.common.Brain;

import java.util.*;

public class RandomBrain implements Brain {
    private final RandomMove randomMove;

    public RandomBrain() {
        this( new RandomMove( new Random()::nextInt ) );
    }

    private RandomBrain( RandomMove randomMove )  {
        this.randomMove = randomMove;
    }

    @Override
    public List<Move> findBestMove( Position position ) {
        return
            randomMove.select( position.getMoves() )
                .map( Collections::singletonList )
                .orElse( Collections.emptyList() );
    }

    //at the moment move for the opponent is not generated, although it's possible and may bring
    //some good results
}
