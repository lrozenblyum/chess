package com.leokom.games.chess.player.legal.brain.normalized;

import com.leokom.games.chess.engine.*;
import com.leokom.games.chess.player.legal.brain.common.EvaluatorAsserts;
import com.leokom.games.commons.brain.GenericEvaluator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith( Parameterized.class )
//normalized specific test
public class CastlingSafetyEvaluatorTest {
    @Parameterized.Parameter
    public GenericEvaluator<Position, Move> evaluator;

    @Parameterized.Parameters( name = "{0}" )
    public static List< GenericEvaluator<Position, Move> > parameters() {
        return Arrays.asList(
            new CastlingSafetyEvaluator(),
            new TwoPliesEvaluator<>( new CastlingSafetyEvaluator() )
        );
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
    public void moreRoomForCastlingBetter() {
        Position position = Position.getInitialPosition();

        new EvaluatorAsserts( this.evaluator ).assertFirstBetter( position,
                new Move( "g1", "f3" ), //more room for castling
                new Move( "a2", "a3" ) ); //no room for castling
    }

    @Test
    public void castlingIsDesired() {
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
}