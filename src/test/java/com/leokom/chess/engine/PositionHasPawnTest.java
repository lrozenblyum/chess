package com.leokom.chess.engine;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Develop position#hasPawn
 * Author: Leonid
 * Date-time: 01.09.12 21:35
 */
public class PositionHasPawnTest {
	private Position position;

	@Before
	public void prepare() {
		position = new Position( null );
	}

	@Test
	public void noPawn() {
		assertFalse( position.hasPawn( "a2" ) );
	}

	@Test
	public void presentPawn() {
		final String square = "g6";
		position.addPawn( Side.WHITE, square );
		assertTrue( position.hasPawn( square ) );
	}

	@Test
	public void presentPawnInitial() {
		final String square = "a2";
		position.addPawn( Side.WHITE, square );
		assertTrue( position.hasPawn( square ) );
	}

	@Test
	public void presentBlackPawn() {
		final String square = "c4";
		position.addPawn(Side.BLACK, square );
		assertTrue( position.hasPawn( square ) );
	}
}
