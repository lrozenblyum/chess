package com.leokom.games.chess.engine;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

public class PositionAllowedMovesDuringOpponentMoveTest {
    @Test
    public void canResignWhenNotOurMove() {
        Set<Move> movesForOpponent = Position.getInitialPosition().getMovesForOpponent();

        Set<Move> expected = new HashSet<>(Collections.singletonList(Move.RESIGN));
        assertEquals( expected, movesForOpponent );
    }

    @Test
    public void canAcceptDrawAfterOfferDraw() {
        Set<Move> movesForOpponent = Position.getInitialPosition().move(Move.OFFER_DRAW).getMovesForOpponent();

        assertThat( movesForOpponent, CoreMatchers.hasItem( Move.ACCEPT_DRAW ));
    }
}
