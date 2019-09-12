package com.leokom.games.chess.engine;

import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Author: Leonid
 * Date-time: 18.09.12 21:50
 */
public class PawnEnPassantNewPositionTest {
	@Test
	public void enPassantLeft() {
		Position position = new Position( Side.WHITE );
		position.setEnPassantFile( 'd' );
		position.addPawn( Side.BLACK, "d5" );

		position.addPawn( Side.WHITE, "e5" );

		Position newPosition = position.move( "e5", "d6" );

		//en passant right now is absent
		assertNull( newPosition.getPossibleEnPassantFile() );
		PositionAsserts.assertEmptySquare( newPosition, "e5" );
		PositionAsserts.assertHasPawn( newPosition, "d6", Side.WHITE );
		PositionAsserts.assertEmptySquare( newPosition, "d5" );
	}

	@Test
	public void enPassantLeftTriangle() {
		Position position = new Position( Side.WHITE );
		position.setEnPassantFile( 'e' );

		position.addPawn( Side.BLACK, "e5" );

		position.addPawn( Side.WHITE, "f5" );

		Position newPosition = position.move( "f5", "e6" );

		//en passant right now is absent
		assertNull( newPosition.getPossibleEnPassantFile() );

		PositionAsserts.assertEmptySquare( newPosition, "f5" );
		PositionAsserts.assertHasPawn( newPosition, "e6", Side.WHITE );
		PositionAsserts.assertEmptySquare( newPosition, "e5" );
	}

	@Test
	public void right() {
		Position position = new Position( Side.WHITE );
		position.setEnPassantFile( 'g' );
		position.addPawn( Side.BLACK, "g5" );

		position.addPawn( Side.WHITE, "f5" );

		Position newPosition = position.move( "f5", "g6" );

		//en passant right now is absent
		assertNull( newPosition.getPossibleEnPassantFile() );

		PositionAsserts.assertEmptySquare( newPosition, "f5" );
		PositionAsserts.assertEmptySquare( newPosition, "g5" );
		PositionAsserts.assertHasPawn( newPosition, "g6", Side.WHITE );
	}

	@Test
	public void enPassantBlackLeft() {
		Position position = new Position( Side.BLACK );
		position.setEnPassantFile( 'a' );
		position.addPawn( Side.WHITE, "a4" );

		position.addPawn( Side.BLACK, "b4" );
		Position newPosition = position.move( "b4", "a3" );

		PositionAsserts.assertEmptySquare( newPosition, "b4" );
		PositionAsserts.assertHasPawn( newPosition, "a3", Side.BLACK );
		PositionAsserts.assertEmptySquare( newPosition, "a4" );
	}

	@Test
	public void enPassantBlackTriangulate() {
		Position position = new Position( Side.BLACK );
		position.setEnPassantFile( 'g' );
		position.addPawn( Side.WHITE, "g4" );

		position.addPawn( Side.BLACK, "h4" );
		Position newPosition = position.move( "h4", "g3" );

		PositionAsserts.assertEmptySquare( newPosition, "h4" );
		PositionAsserts.assertHasPawn( newPosition, "g3", Side.BLACK );
		PositionAsserts.assertEmptySquare( newPosition, "g4" );
	}

	@Test
	public void enPassantRightBlack() {
		Position position = new Position( Side.BLACK );
		position.setEnPassantFile( 'c' );
		position.addPawn( Side.WHITE, "c4" );

		position.addPawn( Side.BLACK, "b4" );
		Position newPosition = position.move( "b4", "c3" );

		//en passant right now is absent
		assertNull( newPosition.getPossibleEnPassantFile() );

		PositionAsserts.assertEmptySquare( newPosition, "b4" );
		PositionAsserts.assertHasPawn( newPosition, "c3", Side.BLACK );
		PositionAsserts.assertEmptySquare( newPosition, "c4" );
	}
}