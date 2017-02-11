package com.leokom.chess.player.simple;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.evaluator.common.DecisionMaker;

import java.util.*;

/**
 * Brain that implements Simple player in Legal player's infrastructure
 * Created by Leonid on 09.02.17.
 */
public class SimpleBrains implements DecisionMaker {
    @Override
    public Optional< Move > findBestMove( Position position ) {
        if ( position.isTerminal() ) {
            return Optional.empty();
        }

        List< Move > suggestedMoves = Arrays.asList(
         new Move( "e2", "e4" ),
         new Move( "d2", "d4" ),
         new Move( "e7", "e5" ),
         new Move( "d7", "d5" ),
         Move.RESIGN );

        Set< Move > legalMoves = position.getMoves();
        return Optional.of(
                suggestedMoves.stream().filter( legalMoves::contains )
                .findFirst()
                .orElse( legalMoves.iterator().next() ) );
    }
}
