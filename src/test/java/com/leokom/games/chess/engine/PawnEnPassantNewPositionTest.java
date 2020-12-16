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
		position.add( Side.BLACK, "d5", PieceType.PAWN );

		position.add( Side.WHITE, "e5", PieceType.PAWN );

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

		position.add( Side.BLACK, "e5", PieceType.PAWN );

		position.add( Side.WHITE, "f5", PieceType.PAWN );

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
		position.add( Side.BLACK, "g5", PieceType.PAWN );

		position.add( Side.WHITE, "f5", PieceType.PAWN );

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
		position.add( Side.WHITE, "a4", PieceType.PAWN );

		position.add( Side.BLACK, "b4", PieceType.PAWN );
		Position newPosition = position.move( "b4", "a3" );

		PositionAsserts.assertEmptySquare( newPosition, "b4" );
		PositionAsserts.assertHasPawn( newPosition, "a3", Side.BLACK );
		PositionAsserts.assertEmptySquare( newPosition, "a4" );
	}

	@Test
	public void enPassantBlackTriangulate() {
		Position position = new Position( Side.BLACK );
		position.setEnPassantFile( 'g' );
		position.add( Side.WHITE, "g4", PieceType.PAWN );

		position.add( Side.BLACK, "h4", PieceType.PAWN );
		Position newPosition = position.move( "h4", "g3" );

		PositionAsserts.assertEmptySquare( newPosition, "h4" );
		PositionAsserts.assertHasPawn( newPosition, "g3", Side.BLACK );
		PositionAsserts.assertEmptySquare( newPosition, "g4" );
	}

	@Test
	public void enPassantRightBlack() {
		Position position = new Position( Side.BLACK );
		position.setEnPassantFile( 'c' );
		position.add( Side.WHITE, "c4", PieceType.PAWN );

		position.add( Side.BLACK, "b4", PieceType.PAWN );
		Position newPosition = position.move( "b4", "c3" );

		//en passant right now is absent
		assertNull( newPosition.getPossibleEnPassantFile() );

		PositionAsserts.assertEmptySquare( newPosition, "b4" );
		PositionAsserts.assertHasPawn( newPosition, "c3", Side.BLACK );
		PositionAsserts.assertEmptySquare( newPosition, "c4" );
	}
}