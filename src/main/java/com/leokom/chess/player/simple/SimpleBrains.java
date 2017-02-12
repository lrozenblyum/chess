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
    public List< Move > findBestMove( Position position ) {
        if ( position.isTerminal() ) {
            return Collections.emptyList();
        }

        List< List< Move > > suggestedMoves = Arrays.asList(
            Collections.singletonList(new Move("e2", "e4")),
            Collections.singletonList(new Move("d2", "d4")),
            Collections.singletonList(new Move("e7", "e5")),
            Collections.singletonList(new Move("d7", "d5")),
            Collections.singletonList(Move.RESIGN));

        Set< Move > legalMoves = position.getMoves();
        return suggestedMoves.stream().filter(suggestedMove -> legalMoves.contains(suggestedMove.get(0)))
                .findFirst()
                .orElse( Collections.singletonList( legalMoves.iterator().next() ) );
    }
}
