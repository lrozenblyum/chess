package com.leokom.games.chess.player.legal.brain.random;

import com.google.common.collect.ImmutableSet;
import com.leokom.games.chess.engine.Move;

import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

class MoveFilter implements UnaryOperator< Set<Move> >  {
    private static final Set<Move> UNWANTED_MOVES = ImmutableSet.of( Move.OFFER_DRAW, Move.RESIGN );

    @Override
    public Set<Move> apply(Set<Move> moves) {
        return moves.stream().
                filter( move -> ! UNWANTED_MOVES.contains( move ) ).
                collect( Collectors.toSet() );
    }
}
