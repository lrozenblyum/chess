package com.leokom.chess.engine;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Author: Leonid
 * Date-time: 20.05.13 22:37
 */
public class PositionImmutabilityTest {
	@Test
	public void positionAfterMoveNotModified() {
		Position position = new Position( null );
		position.add( Side.WHITE, "e2", PieceType.PAWN );
		position.add( Side.WHITE, "d2", PieceType.PAWN );
		position.add( Side.BLACK, "g2", PieceType.PAWN );


		position
			.move( "e2", "e4" )
			.move( "g2", "g1Q" )
			.move( "d2", "d4" );


		//TODO: check full equality:
		//no other pieces on board
		//other conditions (like 3'd repetition?)
		PositionAsserts.assertHasPiece( position, PieceType.PAWN, Side.WHITE, "e2" );
		PositionAsserts.assertHasPiece( position, PieceType.PAWN, Side.WHITE, "d2" );
		PositionAsserts.assertHasPiece( position, PieceType.PAWN, Side.BLACK, "g2" );

	}

	@Test
	public void positionEnPassantStatusClear() {
		Position position = new Position( null );
		position.add( Side.WHITE, "e2", PieceType.PAWN );
		position.add( Side.WHITE, "d2", PieceType.PAWN );
		position.add( Side.BLACK, "g2", PieceType.PAWN );

		position
				.move( "e2", "e4" )
				.move( "g2", "g1Q" )
				.move( "d2", "d4" );


		//en passant
		assertNull( position.getPossibleEnPassantFile() );
	}

	@Test
	public void positionEnPassantStatusSet() {
		Position position = new Position( "e" );
		position.add( Side.WHITE, "e4", PieceType.PAWN );
		position.add( Side.WHITE, "d2", PieceType.PAWN );
		position.add( Side.BLACK, "g4", PieceType.PAWN );

		position
				.move( "g4", "g3" )
				.move( "d2", "d4" );

		assertEquals( "e", position.getPossibleEnPassantFile() );
	}
}
