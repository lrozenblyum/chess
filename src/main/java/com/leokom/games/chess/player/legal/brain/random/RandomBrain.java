package com.leokom.games.chess.player.legal.brain.random;

import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.engine.Position;
import com.leokom.games.chess.player.legal.brain.common.Brain;

import java.util.*;
import java.util.function.UnaryOperator;

/**
 * The brain supports selection of a random move from the set of legal moves in a position.
 * Following moves are excluded:
 * <ul>
 *     <li>RESIGN - because we don't want to give up fast</li>
 *     <li>OFFER_DRAW - because https://github.com/lrozenblyum/chess/issues/161 is not solved yet.
 *     Simulation has shown that our LegalPlayer simply exits in such situation</li>
 * </ul>
 *
 * CONFIRM_DRAW is not excluded because it seems reasonable to keep it as a possibility.
 */
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
