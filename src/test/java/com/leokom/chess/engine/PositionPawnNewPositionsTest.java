package com.leokom.chess.engine;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

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
		position.addPawn( Side.WHITE, "c3" );

		Position newPosition = position.move( "c3", "c4" );

		assertNotNull( "New position must be not null", newPosition );
	}
}
