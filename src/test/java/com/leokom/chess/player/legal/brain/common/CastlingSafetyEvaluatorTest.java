package com.leokom.chess.player.legal.brain.common;

import com.leokom.chess.engine.*;
import org.junit.Test;


public class CastlingSafetyEvaluatorTest extends EvaluatorTestCase {
	@Test
	public void movingRookToLoseOneOfCastlingIsNotFine() {
		Position position = new PositionBuilder()
				.add(Side.WHITE, "e1", PieceType.KING)
				.add(Side.WHITE, "b1", PieceType.KNIGHT)
				.add(Side.WHITE, "a1", PieceType.ROOK)
				.add(Side.WHITE, "h1", PieceType.ROOK)
				.add(Side.WHITE, "h2", PieceType.PAWN)
				.add(Side.BLACK, "e8", PieceType.KING)
				.build();

		asserts.assertFirstBetter( position, new Move( "h2", "h3" ), new Move( "a1", "a2" ) );
	}

	@Test
	public void noCastlingSafetyAfterKingMovement() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "h1", PieceType.BISHOP );

		position.add( Side.BLACK, "e8", PieceType.KING );
		position.add( Side.BLACK, "h8", PieceType.ROOK );
		position.add( Side.BLACK, "a8", PieceType.ROOK );
		position.add( Side.BLACK, "e7", PieceType.PAWN );

		Position prepare = position.move( "e1", "f1" ).move("e7", "e5" );

		//0 for us. 0.25 (fine because empty between king and rooks) for the opponent -> 0.125
		//0, 0 would be 0.5
		//0, 1 would be 0 however it doesn't seem possible now - the opponent cannot castle when you move
		asserts.assertEvaluation( 0.125,  prepare, new Move( "h1", "g2" ) );
	}

	@Test
	public void shouldKingMovementNotGoodInitially() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "h1", PieceType.ROOK );
		position.add( Side.WHITE, "c1", PieceType.BISHOP );
		position.add( Side.BLACK, "h8", PieceType.KING );

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
				.add( Side.WHITE, "d3", PieceType.KNIGHT )
				.add( Side.BLACK, "h8", PieceType.KING );

		Move noCastlingPrevention = new Move( "d3", "e5" );
		Move preventCastlingByBlock = new Move( "d3", "c1" );

		asserts.assertFirstBetter( position, noCastlingPrevention, preventCastlingByBlock );
	}

	@Test
	public void occupyingSpaceToBlockOpponentCastlingIsGood() {
		final PositionBuilder position = new PositionBuilder()
				.add( Side.BLACK, "a8", PieceType.ROOK )
				.add( Side.BLACK, "e8", PieceType.KING )
				.add( Side.WHITE, "d6", PieceType.KNIGHT );

		Move noCastlingPrevention = new Move( "d6", "e4" );
		Move preventCastlingByBlock = new Move( "d6", "c8" );

		asserts.assertFirstBetter( position, preventCastlingByBlock, noCastlingPrevention );
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


	@Override
	EvaluatorType getEvaluatorType() {
		return EvaluatorType.CASTLING_SAFETY;
	}
}