package com.leokom.chess.engine;

import static org.junit.Assert.assertTrue;

/**
 * Validate position status
 * Author: Leonid
 * Date-time: 05.09.12 21:41
 */
public final class PositionAsserts {
	private PositionAsserts() {}

	static void assertHasPiece( Position position, PieceType pieceType, Side notMovedPieceSide, String notMovedPieceSquare ) {
		if ( pieceType != PieceType.PAWN ) {
			throw new IllegalArgumentException( "Piece type is not supported yet: " + pieceType );
		}

		assertHasPawn( position, notMovedPieceSquare, notMovedPieceSide );
	}

	static void assertEmptySquare( Position position, String square ) {
		assertTrue( "The square must be empty: " + square, position.isEmptySquare( square ) );
	}

	/**
	 * Assert that position has a pawn on the square
	 * @param position
	 * @param square
	 * @param side
	 */
	static void assertHasPawn( Position position, String square, Side side ) {
		assertTrue( "Pawn of " + side + " is expected to be on square: " + square,
				position.hasPawn( square, side ) );
	}
}
