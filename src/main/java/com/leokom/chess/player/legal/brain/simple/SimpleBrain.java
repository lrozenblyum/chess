package com.leokom.chess.player.legal.brain.simple;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.legal.brain.common.Brain;

import java.util.*;

import static java.util.Collections.singletonList;

/**
 * GenericBrain that implements Simple player in Legal player's infrastructure
 * Created by Leonid on 09.02.17.
 *
 * Based on previous implementation with comments:
 * Run just 2 moves for white/black (central pawns)
 * Always agree to draw.
 * Resign on the 3'd move unless during the 2'nd move something weird happened that prevents us executing any
 * legal move.
 * This player guarantees finite game.
 *
 * Author: Leonid
 * Date-time: 15.04.13 22:26
 */
public class SimpleBrain implements Brain {
    @Override
    public List< Move > findBestMove( Position position ) {
        if ( position.isTerminal() ) {
            return Collections.emptyList();
        }

        if ( position.getMoves().contains( Move.ACCEPT_DRAW ) ) {
            return singletonList( Move.ACCEPT_DRAW );
        }

        int rankFrom = position.getSideToMove() == Side.WHITE ? 2 : 7;
        int rankTo = position.getSideToMove() == Side.WHITE ? 4 : 5;

        switch ( position.getMoveNumber() ) {
            case 1:
                return singletonList( new Move( "e" + rankFrom,  "e" + rankTo ) );
            case 2:
                List< Move > result = new ArrayList<>();
                String secondDesiredSquare = "d" + rankTo;
                if ( ! position.getSquaresOccupiedBySide( position.getSideToMove().opposite() ).contains(secondDesiredSquare) ) {
                    result.add( new Move( "d" + rankFrom, secondDesiredSquare) );
                }
                else {
                    result.add( position.getMoves().stream().filter( move -> !move.isSpecial()).findFirst().orElse( Move.RESIGN ) );
                }

                result.add( Move.OFFER_DRAW );
                return result;
            default:
                return singletonList( Move.RESIGN );
        }
    }

    @Override
    public Move findBestMoveForOpponent( Position position ) {
        if ( position.getMovesForOpponent().contains( Move.ACCEPT_DRAW ) ) {
            return Move.ACCEPT_DRAW;
        }
        return null;
    }
}
