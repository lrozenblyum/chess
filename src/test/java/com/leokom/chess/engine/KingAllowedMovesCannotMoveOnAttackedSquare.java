package com.leokom.chess.engine;

import org.junit.Test;

/**
 * Test for phrase 'not attacked by one or more of the opponent’s pieces'
 * in 3.8 a
 * Author: Leonid
 * Date-time: 09.07.13 22:17
 */
public class KingAllowedMovesCannotMoveOnAttackedSquare {
	@Test
	public void withPawnNotPromotion() {
		Position position = new Position( null );
		position.addPawn( Side.WHITE, "a2" ); //attacks b3
		position.add( Side.BLACK, "a3", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
			position, "a3",
			"a2", "a4", "b4", "b2" );

	}

	@Test
	public void withPawnPromotion() {
		Position position = new Position( null );
		position.addPawn( Side.BLACK, "h2" ); //may be promoted to h1 or capture g1
		position.add( Side.WHITE, "f1", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "f1",
				"e1", "e2", "f2", "g2" ); //but not g1
	}

	@Test
	public void knightAttacked() {
		Position position = new Position( null );
		position.add( Side.BLACK, "c3", PieceType.KNIGHT ); //controls b1, a2

		position.add( Side.WHITE, "a1", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "a1",
				"b2" );
	}

	@Test
	public void ourSideKnightIsNotAttacker() {
		Position position = new Position( null );
		position.add( Side.WHITE, "c3", PieceType.KNIGHT ); //controls b1, a2

		position.add( Side.WHITE, "a1", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "a1",
				"b2", "b1", "a2" );
	}

	@Test
	public void knightAndPawnAttack() {
		Position position = new Position( null );
		position.add( Side.BLACK, "c3", PieceType.KNIGHT ); //controls b1, a2
		position.add( Side.BLACK, "a2", PieceType.PAWN ); //controls b1

		position.add( Side.WHITE, "a1", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "a1"
				, "b2");
	}

	@Test
	public void knightAndPawnAttackNoWay() {
		Position position = new Position( null );
		position.add( Side.BLACK, "c3", PieceType.KNIGHT ); //controls b1, a2
		position.add( Side.BLACK, "a3", PieceType.PAWN ); //controls b2

		position.add( Side.WHITE, "a1", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "a1" );
	}

	@Test
	public void cannotCaptureIfControlledByPawn() {
		Position position = new Position( null );
		position.add( Side.WHITE, "g7", PieceType.PAWN );
		position.add( Side.WHITE, "h6", PieceType.PAWN ); //protects the pawn

		position.add( Side.BLACK, "f7", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "f7",
				"g8", "e8", "e7", "e6", "f6", "g6" ); //but not protected g7
	}

	@Test
	public void canCaptureAPawnThatControlsOtherPiece() {
		Position position = new Position( null );
		position.add( Side.WHITE, "g8", PieceType.QUEEN );
		position.add( Side.WHITE, "h7", PieceType.PAWN ); //protects the queen

		position.add( Side.BLACK, "h8", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "h8",
				"g7", "h7" ); //cannot capture the protected rook, but can capture the protector: pawn
	}

	@Test
	public void bishopProtected() {
		Position position = new Position( null );
		position.add( Side.WHITE, "c3", PieceType.BISHOP ); //controls b2, a1


		position.add( Side.BLACK, "a2", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "a2",
				"a3", "b1", "b3" );
	}

	@Test
	public void bishopProtectedCannotCapture() {
		Position position = new Position( null );
		position.add( Side.WHITE, "c3", PieceType.BISHOP ); //controls b2, a1
		position.add( Side.WHITE, "a1", PieceType.BISHOP ); //controls b2, a1


		position.add( Side.BLACK, "a2", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "a2",
				"a3", "b1", "b3" );
	}

	@Test
	public void rookControl() {
		Position position = new Position( null );
		position.add( Side.WHITE, "b3", PieceType.ROOK ); //controls file b, rank 3

		position.add( Side.BLACK, "a1", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "a1",
				"a2" ); //b is protected
	}
}
