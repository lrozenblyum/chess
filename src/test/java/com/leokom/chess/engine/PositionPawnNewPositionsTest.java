package com.leokom.chess.engine;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Generate positions by legal pawn moves using the initial position
 * Author: Leonid
 * Date-time: 31.08.12 22:05
 */
public class PositionPawnNewPositionsTest {
	private Position position;

	@Before
	public void prepare() {
		position = new Position( null );
	}

	@Test
	public void singleMove() {
		final String initialSquare = "c3";
		position.addPawn( Side.WHITE, initialSquare );

		final String squareToMove = "c4";
		Position newPosition = position.move( initialSquare, squareToMove );

		assertNotNull( "New position must be not null", newPosition );
		assertNotSame( newPosition, position );

		assertHasPawn( newPosition, squareToMove );
		assertHasNoPawn( newPosition, initialSquare );
	}

	private static void assertHasNoPawn( Position position, String square ) {
		assertFalse( "Pawn must be absent on square" + square, position.hasPawn( square ) );
	}

	/**
	 * Assert that position has a pawn on the square
	 * @param position
	 * @param square
	 */
	private static void assertHasPawn( Position position, String square ) {
		assertTrue( "Pawn is expected to be on square: " + square, position.hasPawn( square ) );
	}
}
