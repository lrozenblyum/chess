package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.*;
import org.junit.Before;
import org.junit.Test;


public class CastlingSafetyEvaluatorTest {
	private EvaluatorAsserts asserts;

	@Before
	public void prepare() {
		CastlingSafetyEvaluator evaluator = new CastlingSafetyEvaluator();
		asserts = new EvaluatorAsserts( evaluator );
	}

	@Test
	public void shouldKingMovementNotGoodInitially() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "h1", PieceType.ROOK );
		position.add( Side.WHITE, "c1", PieceType.BISHOP );

		Move kingMove = new Move( "e1", "f1" );

		Move notKingMove = new Move( "c1", "b2" );

		asserts.assertFirstBetter( position, notKingMove, kingMove );
	}

	@Test
	public void shouldRookMoveOKGivenCastlingDone() {
		PositionBuilder position = new PositionBuilder();

		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "h1", PieceType.ROOK );
		position.add( Side.BLACK, "a8", PieceType.KING );

		position.add( Side.WHITE, "a1", PieceType.KNIGHT );

		Position afterCastling = position
			.move( "e1", "g1" )
			.move( "a8", "b8" ); //any valid black move


		Move rookMove = new Move( "f1", "f8" );

		Move notRookMove = new Move( "a1", "b3" );

		asserts.assertNoDifference( afterCastling, rookMove, notRookMove );
	}

	@Test
	public void noProblemToMoveKingAfterBothRooksMoved() {
		PositionBuilder position = new PositionBuilder();

		position.add( Side.WHITE, "a1", PieceType.ROOK );
		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "h1", PieceType.ROOK );

		position.add( Side.BLACK, "a8", PieceType.KING );

		position.add( Side.WHITE, "d4", PieceType.KNIGHT );

		Position afterBothRooksMoved = position
				.move( "a1", "b1" )
				.move( "a8", "b8" ) //any valid black move
				.move( "h1", "h2" )
				.move( "b8", "a8" );

		Move kingMove = new Move( "e1", "e2" );

		Move notKingMove = new Move( "d4", "e6" );

		asserts.assertNoDifference( afterBothRooksMoved, kingMove, notKingMove );
	}


}