package com.leokom.chess.engine;

import org.junit.Test;

import static org.junit.Assert.*;

public class MoveTest {
	@Test
	public void resignToString() {
		assertEquals( "RESIGN", Move.RESIGN.toString() );
	}

	@Test
	public void offerDrawToString() {
		assertEquals( "OFFER_DRAW", Move.OFFER_DRAW.toString() );
	}

	@Test
	public void resignIsNotDraw() {
		assertNotEquals( Move.RESIGN, Move.OFFER_DRAW );
	}
}