package com.leokom.chess.engine;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class PositionAllowedMovesDuringOpponentMoveTest {
    @Test
    public void canResignWhenNotOurMove() {
        Set<Move> movesForOpponent = Position.getInitialPosition().getMovesForOpponent();

        Set<Move> expected = new HashSet<>(Collections.singletonList(Move.RESIGN));
        assertEquals( expected, movesForOpponent );
    }

}
