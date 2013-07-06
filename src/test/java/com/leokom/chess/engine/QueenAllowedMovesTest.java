package com.leokom.chess.engine;

import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 06.07.13 14:18
 */
public class QueenAllowedMovesTest {
	@Test
	public void h1() {
		Position position = new Position( null );
		position.addQueen( Side.BLACK, "h1" );

		PositionAsserts.assertAllowedMoves(
			position, "h1",
			"a1", "b1", "c1", "d1", "e1", "f1", "g1",
			"h2", "h3", "h4", "h5", "h6", "h7", "h8",
			"g2", "f3", "e4", "d5", "c6", "b7", "a8" );
	}
}
