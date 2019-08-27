package com.leokom.chess.player.legal.brain.random;

import com.leokom.chess.engine.Move;

import java.util.*;
import java.util.function.Function;

/**
 * Peeks move from a given Set by policy:
 * random generator defines 'index' in the Set.
 * Since Set is not an ordered Collection by default that means we'll iterate the given
 * count of items until the desired index.
 */
class RandomMove {
    private final Function<Integer, Integer> randomGenerator;

    /**
     *
     * @param randomGenerator function(max) -> [0, max)
     */
    RandomMove( Function< Integer, Integer > randomGenerator ) {
        this.randomGenerator = randomGenerator;
    }

    Optional< Move > select( Set< Move > moves ) {
        int movesAvailable = moves.size();
        if ( movesAvailable == 0 ) { //terminal
            return Optional.empty();
        }
        int moveToPeek = randomGenerator.apply(movesAvailable);
        Iterator<Move> moveIterator = moves.iterator();

        for ( int moveIndex = 0; moveIndex < moveToPeek; moveIndex++ ) {
            moveIterator.next();
        }

        return Optional.of( moveIterator.next() );
    }


}
