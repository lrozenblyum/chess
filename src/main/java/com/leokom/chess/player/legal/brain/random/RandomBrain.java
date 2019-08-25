package com.leokom.chess.player.legal.brain.random;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.brain.common.Brain;

import java.util.*;

public class RandomBrain implements Brain {
    private final Random randomGenerator;

    public RandomBrain() {
        this( new Random() );
    }

    private RandomBrain( Random randomGenerator ) {
        this.randomGenerator = randomGenerator;
    }

    @Override
    public List<Move> findBestMove( Position position ) {
        int moveToPeek = randomGenerator.nextInt(position.getMoves().size());
        Iterator<Move> moveIterator = position.getMoves().iterator();

        for ( int moveIndex = 0; moveIndex < moveToPeek; moveIndex++ ) {
            moveIterator.next();
        }

        return Collections.singletonList(moveIterator.next());
    }

    //at the moment move for the opponent is not generated, although it's possible and may bring
    //some good results
}
