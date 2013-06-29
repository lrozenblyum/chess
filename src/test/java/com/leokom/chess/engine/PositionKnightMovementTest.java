package com.leokom.chess.engine;

import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 29.06.13 17:36
 */
public class PositionKnightMovementTest {
	@Test
	public void simpleMove() {
		Position position = new Position( null );
		position.add( Side.WHITE, "b1", PieceType.KNIGHT );

		final Position newPosition = position.move( "b1", "c3" );

		PositionAsserts.assertHasPiece( newPosition, PieceType.KNIGHT, Side.WHITE, "c3" );

	}

	@Test
	public void blackMove() {
		Position position = new Position( null );
		position.add( Side.BLACK, "b1", PieceType.KNIGHT );

		final Position newPosition = position.move( "b1", "c3" );

		PositionAsserts.assertHasPiece( newPosition, PieceType.KNIGHT, Side.BLACK, "c3" );

	}

	@Test
	public void otherPiecesRemain() {
		Position position = new Position( null );
		position.add( Side.BLACK, "b1", PieceType.KNIGHT );
		position.add( Side.BLACK, "a2", PieceType.PAWN );

		final Position newPosition = position.move( "b1", "c3" );

		PositionAsserts.assertHasPiece( newPosition, PieceType.KNIGHT, Side.BLACK, "c3" );

		PositionAsserts.assertHasPiece( newPosition, PieceType.PAWN, Side.BLACK, "a2" );

	}
}
