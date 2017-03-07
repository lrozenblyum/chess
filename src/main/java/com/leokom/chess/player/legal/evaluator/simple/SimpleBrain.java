package com.leokom.chess.player.legal.evaluator.simple;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.evaluator.common.DecisionMaker;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * Brain that implements Simple player in Legal player's infrastructure
 * Created by Leonid on 09.02.17.
 *
 * Based on previous implementation with comments:
 * Run just 2 moves for white/black (central pawns)
 * Always agree to draw.
 * Resign on the 3'd move.
 * This player guarantees finite game.
 *
 * Author: Leonid
 * Date-time: 15.04.13 22:26
 */
public class SimpleBrain implements DecisionMaker {
    @Override
    public List< Move > findBestMove( Position position ) {
        if ( position.isTerminal() ) {
            return Collections.emptyList();
        }

        List< List< Move > > suggestedMoves = asList(
            singletonList( Move.ACCEPT_DRAW ),
            singletonList(new Move("e2", "e4")),
            asList(new Move("d2", "d4"), Move.OFFER_DRAW),
            singletonList(new Move("e7", "e5")),
            asList(new Move("d7", "d5"), Move.OFFER_DRAW),
            singletonList(Move.RESIGN));

        Set< Move > legalMoves = position.getMoves();
        return suggestedMoves.stream().filter(suggestedMove -> legalMoves.contains(suggestedMove.get(0)))
                .findFirst()
                .orElse( singletonList( legalMoves.iterator().next() ) );
    }

    @Override
    public Move findBestMoveForOpponent( Position position ) {
        if ( position.getMovesForOpponent().contains( Move.ACCEPT_DRAW ) ) {
            return Move.ACCEPT_DRAW;
        }
        return null;
    }
}
