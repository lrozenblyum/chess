package com.leokom.games.chess.engine;

import org.junit.Test;
import org.mutabilitydetector.unittesting.MutabilityAssert;

import static org.junit.Assert.*;

public class MoveTest {
	@Test
	public void moveShouldBeImmutable() {
		MutabilityAssert.assertImmutable( Move.class );
	}


	@Test
	public void claimDrawToString() {
		assertEquals( "CLAIM_DRAW1", Move.CLAIM_DRAW.toString() );
	}

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