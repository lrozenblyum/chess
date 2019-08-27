package com.leokom.chess.player.legal.brain.random;

import com.leokom.chess.engine.Move;

import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

class MoveFilter implements UnaryOperator< Set<Move> >  {

    @Override
    public Set<Move> apply(Set<Move> moves) {
        return moves.stream().
                filter( move -> !move.equals( Move.RESIGN ) ).
                collect(Collectors.toSet() );
    }
}
