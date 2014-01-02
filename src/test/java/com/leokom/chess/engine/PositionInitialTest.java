package com.leokom.chess.engine;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Author: Leonid
 * Date-time: 02.01.14 22:07
 */
public class PositionInitialTest {
	@Test
	public void getInitialPositionWhitePawn() {
		final Position initial = Position.getInitialPosition();

		assertTrue( initial.hasPawn( Side.WHITE, "a2" ) );
	}

	@Test
	public void getInitialPositionBlackPawn() {
		final Position initial = Position.getInitialPosition();

		assertTrue( initial.hasPawn( Side.BLACK, "h7" ) );
	}
}
