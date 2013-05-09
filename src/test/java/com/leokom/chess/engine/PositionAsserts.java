package com.leokom.chess.engine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Validate position status
 * Author: Leonid
 * Date-time: 05.09.12 21:41
 */
public final class PositionAsserts {
	private PositionAsserts() {}

	static void assertHasPiece( Position position, PieceType pieceType, Side side, String square ) {
		switch ( pieceType ) {
			case PAWN:
				assertHasPawn( position, square, side );
				return;
			case QUEEN:
				assertHasQueen( position, square, side );
				return;
			default:
				throw new IllegalArgumentException( "Piece type is not supported yet: " + pieceType );
		}
	}

	private static void assertHasQueen( Position position, String square, Side side ) {
		assertTrue(
			"Queen of " + side + " is expected to be on square: " + square +
			" Whole position is: " + position,
			position.hasQueen( side, square ) );
	}

	static void assertEmptySquare( Position position, String square ) {
		assertTrue( "The square must be empty: " + square, position.isEmptySquare( square ) );
	}

	static void assertHasPawn( Position position, String square, Side side ) {
		assertTrue( "Pawn of " + side + " is expected to be on square: " + square,
				position.hasPawn( side, square ) );
	}

	static void assertHasNoPawn( Position position, String square, Side side ) {
		assertFalse( "Pawn of " + side + " is NOT expected to be on square: " + square,
				position.hasPawn( side, square ) );
	}

	/**
	 * Check that inside the position, starting from initial field,
	 * we can legally reach EVERY reachableSquares (and ONLY them)
	 * (basing on position's feedback)
	 * @param position position to validate
	 * @param initialField field to check moves from
	 * @param expectedReachableSquares exact squares that must be reachable
	 */
	static void assertAllowedMoves( Position position, String initialField, String... expectedReachableSquares ) {
		Set<String> squares = position.getMovesFrom( initialField );
		assertEquals( new HashSet<String>( Arrays.asList( expectedReachableSquares ) ), squares );
	}
}
