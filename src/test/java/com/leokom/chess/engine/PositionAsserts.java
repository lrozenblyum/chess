package com.leokom.chess.engine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
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
		assertTrue( "Queen of " + side + " is expected to be on square: " + square,
				position.hasQueen( side, square ) );
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
				position.hasPawn( side, square ) );
	}

	/**
	 * Check that inside the position, starting from initial field,
	 * we can legally reach EVERY reachableSquares (and ONLY them)
	 * (basing on position's feedback)
	 * @param position
	 * @param initialField
	 * @param reachableSquares
	 */
	static void assertAllowedMoves( Position position, String initialField, String... reachableSquares ) {
		Set<String> squares = position.getMovesFrom( initialField );
		assertEquals( new HashSet<String>( Arrays.asList( reachableSquares ) ), squares );
	}
}
