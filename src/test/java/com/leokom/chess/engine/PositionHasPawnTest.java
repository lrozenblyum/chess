package com.leokom.chess.engine;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

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
}
