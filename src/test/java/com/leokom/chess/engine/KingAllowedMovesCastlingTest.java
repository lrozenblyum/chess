package com.leokom.chess.engine;

import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 16.02.14 22:02
 */
public class KingAllowedMovesCastlingTest {
	@Test
	public void castlingIsAllowed() {
		Position position = new Position( null );
		position.add( Side.WHITE, "e1", PieceType.KING );

		position.add( Side.WHITE, "h1", PieceType.ROOK );

		PositionAsserts.assertAllowedMovesInclude(
				position, "e1", "g1" );
	}

	@Test
	public void castlingIsAllowedBlack() {
		Position position = new Position( null );
		position.add( Side.BLACK, "e8", PieceType.KING );

		position.add( Side.BLACK, "h8", PieceType.ROOK );

		PositionAsserts.assertAllowedMovesInclude(
				position, "e8", "g8" );
	}

	@Test
	public void castlingQueenSide() {
		Position position = new Position( null );
		position.add( Side.BLACK, "e8", PieceType.KING );

		position.add( Side.BLACK, "a8", PieceType.ROOK );

		PositionAsserts.assertAllowedMovesInclude(
				position, "e8", "c8" );
	}

	@Test
	public void rightToCastlingIsLostIfKingMoved() {
		Position position = new Position( null );
		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "h1", PieceType.ROOK );

		position.add( Side.BLACK, "a8", PieceType.KING );

		Position newPosition = position
			.move( "e1", "e2" )
			.move( "a8", "a7" ) //any valid black move
			.move( "e2", "e1" )
			.move( "a7", "a8" ); //any valid black move

		PositionAsserts.assertAllowedMovesOmit(
				newPosition, "e1", "g1" );
	}

	@Test
	public void rightToCastlingNoInfluenceToOppositeSide() {
		Position position = new Position( null );
		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "h1", PieceType.ROOK );

		position.add( Side.BLACK, "e8", PieceType.KING );
		position.add( Side.BLACK, "a8", PieceType.ROOK );

		Position newPosition = position
				.move( "e1", "g1" ); //castling

		PositionAsserts.assertAllowedMovesInclude(
				newPosition, "e8", "c8" );
	}

	@Test //not just one move lost
	public void rightToCastlingIsLostPermanently() {
		Position position = new Position( null );
		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "h1", PieceType.ROOK );
		position.add( Side.WHITE, "a2", PieceType.PAWN );

		position.add( Side.BLACK, "a8", PieceType.KING );

		Position newPosition = position
				.move( "e1", "e2" )
				.move( "a8", "a7" ) //any valid black move
				.move( "e2", "e1" )
				.move( "a7", "a8" ) //any valid black move
				.move( "a2", "a4" ) //non-king, non-rook related move
				.move( "a8", "a7" ); //any valid black move

		PositionAsserts.assertAllowedMovesOmit(
				newPosition, "e1", "g1" );
	}

	@Test //not just one move lost
	public void castlingIsImpossibleAfterCastling() {
		Position position = new Position( null );
		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "h1", PieceType.ROOK );
		position.add( Side.WHITE, "a2", PieceType.PAWN );

		position.add( Side.BLACK, "a8", PieceType.KING );

		Position newPosition = position
				.move( "e1", "g1" ) //castle
				.move( "a8", "a7" ) //any valid black move
				.move( "f1", "f2" ) //rook movement started
				.move( "a7", "a8" ) //any valid black move
				.move( "f2", "h2" ) //regrouping rook to initial position
				.move( "a8", "a7" )
				.move( "h2", "h1" ) //rook is on the base
				.move( "a7", "a8" )
				.move( "g1", "f1" ) //regrouping king
				.move( "a8", "a7" ) //any valid black move
				.move( "f1", "e1" ) //king is on the base
				.move( "a7", "a8" );

		PositionAsserts.assertAllowedMovesOmit(
				newPosition, "e1", "g1" );
	}

	@Test
	public void castlingRightLostIfRookMoved() {
		Position position = new Position( null );

		position.add( Side.WHITE, "h1", PieceType.KING );

		position.add( Side.BLACK, "e8", PieceType.KING );
		position.add( Side.BLACK, "a8", PieceType.ROOK );

		Position newPosition = position
				.move( "a8", "a7" )
				.move( "h1", "h2" ); //any valid white move

		PositionAsserts.assertAllowedMovesOmit(
				newPosition, "e8", "c8" );
	}

	@Test
	public void castlingRightLostPermanentlyIfRookMoved() {
		Position position = new Position( null );

		position.add( Side.WHITE, "h1", PieceType.KING );

		position.add( Side.BLACK, "e8", PieceType.KING );
		position.add( Side.BLACK, "a8", PieceType.ROOK );

		Position newPosition = position
			.move( "a8", "a7" )
			.move( "h1", "h2" ) //any valid white move
			.move( "a7", "a8" )
			.move( "h2", "h1" );

		PositionAsserts.assertAllowedMovesOmit(
				newPosition, "e8", "c8" );
	}

	@Test
	public void castlingRightNotLostWithAnotherRook() {
		Position position = new Position( null );

		position.add( Side.WHITE, "h3", PieceType.PAWN );
		position.add( Side.WHITE, "h1", PieceType.KING );

		position.add( Side.BLACK, "a8", PieceType.ROOK );
		position.add( Side.BLACK, "e8", PieceType.KING );
		position.add( Side.BLACK, "h8", PieceType.ROOK );

		Position newPosition = position
				.move( "a8", "a7" )
				.move( "h1", "h2" ); //any valid white move

		PositionAsserts.assertAllowedMovesInclude(
				newPosition, "e8", "g8" );
	}

	@Test
	public void bothRooksMovedCannotCastle() {
		Position position = new Position( null );

		position.add( Side.WHITE, "h3", PieceType.PAWN );
		position.add( Side.WHITE, "h1", PieceType.KING );

		position.add( Side.BLACK, "a8", PieceType.ROOK );
		position.add( Side.BLACK, "e8", PieceType.KING );
		position.add( Side.BLACK, "h8", PieceType.ROOK );

		Position newPosition = position
				.move( "a8", "a7" )
				.move( "h1", "h2" )
				.move( "h8", "h5" )
				.move( "h2", "h1" )
				.move( "a7", "a8" )
				.move( "h1", "h2" );

		PositionAsserts.assertAllowedMovesOmit(
				newPosition, "e8", "g8" );

		PositionAsserts.assertAllowedMovesOmit(
				newPosition, "e8", "c8" );
	}

	@Test
	public void preventTemporaryCastlingIfAttacked() {
		Position position = new Position( null );

		//attacking the black king
		position.add( Side.WHITE, "e1", PieceType.ROOK );
		position.add( Side.WHITE, "a1", PieceType.KING );

		position.add( Side.BLACK, "a8", PieceType.ROOK );
		position.add( Side.BLACK, "e8", PieceType.KING );
		position.add( Side.BLACK, "h8", PieceType.ROOK );

		PositionAsserts.assertAllowedMovesOmit(
				position, "e8", "g8" );

		PositionAsserts.assertAllowedMovesOmit(
				position, "e8", "c8" );
	}

	@Test
	public void checkIsJustTemporaryPrevention() {
		Position position = new Position( null );

		//attacking the black king
		position.add( Side.WHITE, "e1", PieceType.ROOK );
		position.add( Side.WHITE, "a1", PieceType.KING );

		position.add( Side.BLACK, "a8", PieceType.ROOK );
		position.add( Side.BLACK, "e8", PieceType.KING );
		position.add( Side.BLACK, "h7", PieceType.ROOK );

		//blocking check
		Position newPosition = position
				.move( "h7", "e7" )
				.move( "e1", "e2" ); //non-check move


		PositionAsserts.assertAllowedMovesInclude(
				newPosition, "e8", "c8" );
	}

	@Test
	public void preventCastlingIfCrossSquareAttacked() {
		Position position = new Position( null );

		//attacking rank f
		position.add( Side.WHITE, "f1", PieceType.ROOK );
		position.add( Side.WHITE, "a1", PieceType.KING );

		position.add( Side.BLACK, "a8", PieceType.ROOK );
		position.add( Side.BLACK, "e8", PieceType.KING );
		position.add( Side.BLACK, "h8", PieceType.ROOK );

		//f is crossed
		PositionAsserts.assertAllowedMovesOmit(
				position, "e8", "g8" );

		PositionAsserts.assertAllowedMovesInclude(
				position, "e8", "c8" );
	}

	@Test
	public void preventCastlingIfCrossSquareAttackedQueenSide() {
		Position position = new Position( null );

		//attacking rank d
		position.add( Side.WHITE, "d1", PieceType.QUEEN );
		position.add( Side.WHITE, "a1", PieceType.KING );

		position.add( Side.BLACK, "a8", PieceType.ROOK );
		position.add( Side.BLACK, "e8", PieceType.KING );
		position.add( Side.BLACK, "h8", PieceType.ROOK );


		PositionAsserts.assertAllowedMovesInclude(
				position, "e8", "g8" );

		//d is crossed
		PositionAsserts.assertAllowedMovesOmit(
				position, "e8", "c8" );
	}

	@Test
	public void cannotCastleIfExposeKingToCheck() {
		Position position = new Position( null );
		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "h1", PieceType.ROOK );

		position.add( Side.BLACK, "a8", PieceType.KING );

		//controls g1
		position.add( Side.BLACK, "h2", PieceType.BISHOP );

		PositionAsserts.assertAllowedMovesOmit(
				position, "e1", "g1" );
	}

	@Test
	public void ourPieceInMiddlePreventsCastling() {
		Position position = new Position( null );
		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "a1", PieceType.ROOK );
		position.add( Side.WHITE, "b1", PieceType.KNIGHT );


		position.add( Side.BLACK, "h8", PieceType.KING );

		PositionAsserts.assertAllowedMovesOmit(
				position, "e1", "c1" );
	}

	@Test
	public void opponentPieceInMiddlePreventsCastling() {
		Position position = new Position( null );
		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "h1", PieceType.ROOK );

		position.add( Side.BLACK, "f1", PieceType.KNIGHT );


		position.add( Side.BLACK, "h8", PieceType.KING );

		PositionAsserts.assertAllowedMovesOmit(
				position, "e1", "g1" );
	}
}
