package com.leokom.chess.engine;

import org.jooq.lambda.Seq;
import org.junit.Test;

public class DrawBy50MovesTest {
    @Test
    public void claimDrawImpossibleFromInitialPosition() {
        PositionAsserts.assertAllowedMovesOmit(
            Position.getInitialPosition(),
            Move.CLAIM_DRAW
        );
    }

    @Test
    public void canClaimDrawAfter50() {
        final Position initialPosition = Position.getInitialPosition( Rules.DEFAULT );

        //knights moving forth and back
        //50 * 2 = 100 / 4 = 25
        final int iterationsToEnd = 25;
        final Seq<Move> moves = Seq.of( new Move( "g1", "f3" ), new Move( "g8", "f6" ),
                new Move( "f3", "g1" ), new Move( "f6", "g8" ) )
                .cycle( iterationsToEnd );

        final Position result = new PositionMover( initialPosition, moves ).run();

        //NOTE: this assert should itself be thought over
        //did white receive the claim draw right now ?
        //could blacks claim draw previous move after their moving?
        //according to the rules a player should claim BEFORE actual movement which makes 50
        //so to check ...
        PositionAsserts.assertAllowedMovesInclude( result, Move.CLAIM_DRAW );
    }

}
