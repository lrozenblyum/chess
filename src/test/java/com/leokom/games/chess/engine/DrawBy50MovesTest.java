package com.leokom.games.chess.engine;

import org.jooq.lambda.Seq;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        final Position result = prepareReadyForClaimPosition();

        //NOTE: this assert should itself be thought over
        //did white receive the claim draw right now ?
        //could blacks claim draw previous move after their moving?
        //according to the rules a player should claim BEFORE actual movement which makes 50
        //so to check ...
        PositionAsserts.assertAllowedMovesInclude( result, Move.CLAIM_DRAW );
    }

    @Test
    public void claimDrawMakesItTerminal() {
        assertTrue( prepareReadyForClaimPosition().move( Move.CLAIM_DRAW ).isTerminal() );
    }

    @Test
    public void claimDrawReason() {
        Position position = prepareReadyForClaimPosition().move(Move.CLAIM_DRAW);
        assertEquals( Result.DRAW_BY_CLAIM, position.getGameResult() );
    }

    private Position prepareReadyForClaimPosition() {
        final Position initialPosition = Position.getInitialPosition( Rules.DEFAULT );
        final Seq<Move> moves = get50MovesToDraw();

        return new PositionMover( initialPosition, moves ).run();
    }

    private Seq<Move> get50MovesToDraw() {
        //knights moving forth and back
        //50 * 2 = 100 / 4 = 25
        final int iterationsToEnd = 25;
        return Seq.of( new Move( "g1", "f3" ), new Move( "g8", "f6" ),
                new Move( "f3", "g1" ), new Move( "f6", "g8" ) )
                .cycle( iterationsToEnd );
    }

    @Test
    public void cannotClaimDrawBeforeTheLastMove() {
        final Position initialPosition = Position.getInitialPosition(Rules.DEFAULT);

        Seq<Move> moves = removeLastMove( get50MovesToDraw() );

        Position position = new PositionMover(initialPosition, moves).run();
        PositionAsserts.assertAllowedMovesOmit(position, Move.CLAIM_DRAW);
    }

    private Seq<Move> removeLastMove(Seq<Move> sequence) {
        return sequence.reverse().skip(1).reverse();
    }

    //The game is drawn, upon a correct claim by a player having the move, if:
    //9.3.1
    //he writes his move, which cannot be changed, on his scoresheet and declares to the arbiter his intention
    // to make this move which will result in the last 50 moves by each player having been made
    // without the movement of any pawn and without any capture, or
}
