package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.*;
import com.leokom.chess.player.legal.brain.common.EvaluatorAsserts;
import org.junit.Before;
import org.junit.Test;

//normalized specific test
public class CastlingSafetyEvaluatorTest {
    private CastlingSafetyEvaluator evaluator;
    private TwoPliesEvaluator<Position, Move> twoPliesEvaluator;

    @Before
    public void prepare() {
        evaluator = new CastlingSafetyEvaluator();
        twoPliesEvaluator = new TwoPliesEvaluator<>( this.evaluator );
    }

    @Test
    public void allMovesShouldBeInNormalizedRange() {
        //preparing a position closer to castling
        Position position = Position.getInitialPosition().
                move( "e2", "e4" ).
                move( "e7", "e5" ).
                move( "g1", "f3" ).
                move( "g8", "f6" );

        new NormalizedEvaluatorAssert( evaluator ).assertAllMovesEvaluatedInNormalizedRange( position );
    }

    @Test
    public void singlePlyThinkingLogical() {
        Position position = Position.getInitialPosition();

        new EvaluatorAsserts( this.evaluator ).assertFirstBetter( position,
                new Move( "g1", "f3" ), //more room for castling
                new Move( "a2", "a3" ) ); //no room for castling
    }

    @Test
    public void twoPliesThinkingLogical() {
        Position position = Position.getInitialPosition();

        new EvaluatorAsserts( twoPliesEvaluator ).assertFirstBetter( position,
                new Move( "g1", "f3" ), //more room for castling
                new Move( "a2", "a3" ) ); //no room for castling
    }

    @Test
    public void singlePlyWantsCastling() {
        Position position = new PositionBuilder()
                .add(Side.WHITE, "a1", PieceType.ROOK)
                .add(Side.WHITE, "e1", PieceType.KING)
                .add(Side.WHITE, "h2", PieceType.PAWN)
                .add(Side.BLACK, "e8", PieceType.KING)
                .build();

        new EvaluatorAsserts( this.evaluator ).assertFirstBetter( position,
                new Move( "e1", "c1" ), //castling
                new Move( "h2", "h3" ) ); //not castling
    }

    @Test
    public void twoPliesWantsCastling() {
        Position position = new PositionBuilder()
                .add(Side.WHITE, "a1", PieceType.ROOK)
                .add(Side.WHITE, "e1", PieceType.KING)
                .add(Side.WHITE, "h2", PieceType.PAWN)
                .add(Side.BLACK, "e8", PieceType.KING)
                .build();

        new EvaluatorAsserts( this.twoPliesEvaluator ).assertFirstBetter( position,
                new Move( "e1", "c1" ), //castling
                new Move( "h2", "h3" ) ); //not castling
    }
}