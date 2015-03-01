package com.leokom.chess.engine;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author: Leonid
 * Date-time: 29.06.13 17:36
 */
public class KnightNewPositionTest {
	@Test
	public void simpleMove() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "b1", PieceType.KNIGHT );

		final Position newPosition = position.build().move( "b1", "c3" );

		PositionAsserts.assertHasPiece( newPosition, PieceType.KNIGHT, Side.WHITE, "c3" );

	}

	@Test
	public void blackMove() {
		PositionBuilder position = new PositionBuilder()
			.add( Side.BLACK, "b1", PieceType.KNIGHT )
				.setSideOf( "b1" );

		final Position newPosition = position.build().move( "b1", "c3" );

		PositionAsserts.assertHasPiece( newPosition, PieceType.KNIGHT, Side.BLACK, "c3" );

	}

	@Test
	public void otherPiecesRemain() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.BLACK, "b1", PieceType.KNIGHT );
		position.add( Side.BLACK, "a2", PieceType.PAWN );

		final Position newPosition = position.setSideOf( "b1" ).build().move( "b1", "c3" );

		PositionAsserts.assertHasPiece( newPosition, PieceType.KNIGHT, Side.BLACK, "c3" );

		PositionAsserts.assertHasPiece( newPosition, PieceType.PAWN, Side.BLACK, "a2" );

	}

	@Test
	public void movingKnightRemoved() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.BLACK, "b1", PieceType.KNIGHT );

		final Position newPosition = position.setSideOf( "b1" ).build().move( "b1", "a3" );

		PositionAsserts.assertEmptySquare( newPosition, "b1" );

	}

	@Test
	public void enPassantPossibilityClearance() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.BLACK, "h1", PieceType.KNIGHT );

		position.add( Side.WHITE, "a2", PieceType.PAWN );

		Position afterPawnMove = position.setSideOf( "a2" ).build().move( "a2", "a4" );
		//this assert is just to ensure previous conditions are ok
		assertEquals( "a", afterPawnMove.getPossibleEnPassantFile() );

		final Position afterKnightMove = afterPawnMove.move( "h1", "g3" );

		Assert.assertNull( afterKnightMove.getPossibleEnPassantFile() );

	}
}
