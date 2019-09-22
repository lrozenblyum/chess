package com.leokom.games.chess.player.legal.brain.normalized;


import com.leokom.games.chess.engine.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

//it's move integration than NormalizedBrainPositionMoveTest
public class NormalizedBrainMasterEvaluatorTest {
    @Test
    public void resignIsNotTheBest() {
        List<Move> bestMove = new NormalizedChessBrain(new MasterEvaluator(), 2)
                .findBestMove(Position.getInitialPosition());
        assertNotEquals( Move.RESIGN, bestMove.get(0) );
    }

    @Test
    public void drawOfferIsNotTheBest() {
        List<Move> bestMove = new NormalizedChessBrain(new MasterEvaluator(), 2)
                .findBestMove(Position.getInitialPosition());
        assertNotEquals( Move.OFFER_DRAW, bestMove.get(0) );
    }

    //it's not a strict requirement. I just want to exclude draw offer from the algorithm
    //we're not ready for it yet.
    //TODO: extract draw offer 2'nd level evaluation to a separate ticket
    @Test
    public void drawOfferIsNotTheBestResponse() {
        List<Move> bestMove = new NormalizedChessBrain(new MasterEvaluator(), 2)
                .findBestMove(Position.getInitialPosition().move( "e2", "e4" ));
        assertNotEquals( Move.OFFER_DRAW, bestMove.get(0) );
    }

    @Test
    public void winningMaterialNotBestIdeaIfCanCheckmate() {
        Position position = new PositionBuilder()
                .add(Side.BLACK, "a1", PieceType.KING)
                .add(Side.WHITE, "c1", PieceType.KING)
                .add(Side.WHITE, "c3", PieceType.ROOK)
                .add(Side.BLACK, "h3", PieceType.BISHOP)
                .build();

        List<Move> result = new NormalizedChessBrain(new MasterEvaluator(), 2).findBestMove(position);
        //we shouldn't try c3: h3
        assertEquals( new Move( "c3", "a3" ), result.get( 0 )  );
    }
}
