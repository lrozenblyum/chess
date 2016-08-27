package com.leokom.chess.player.legal.evaluator.normalized;

import com.leokom.chess.engine.*;
import com.leokom.chess.player.legal.evaluator.common.EvaluatorAsserts;
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
	public void givingRoomForCastlingBetterThanNo() {
		final Position position = Position.getInitialPosition();

		Move giveSpaceForCastling = new Move( "b1", "c3" );
		Move noSpaceForCastling = new Move( "a2", "a3" );

		asserts.assertFirstBetter( position, giveSpaceForCastling, noSpaceForCastling );
	}

	@Test
	public void occupyingSpaceToPreventCastlingNotGood() {
		final PositionBuilder position = new PositionBuilder()
				.add( Side.WHITE, "a1", PieceType.ROOK )
				.add( Side.WHITE, "e1", PieceType.KING )
				.add( Side.WHITE, "d3", PieceType.KNIGHT );

		Move noCastlingPrevention = new Move( "d3", "e5" );
		Move preventCastlingByBlock = new Move( "d3", "c1" );

		asserts.assertFirstBetter( position, noCastlingPrevention, preventCastlingByBlock );
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