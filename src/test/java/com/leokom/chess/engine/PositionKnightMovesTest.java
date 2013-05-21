package com.leokom.chess.engine;

import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 21.05.13 22:07
 */
public class PositionKnightMovesTest {
	@Test
	public void knightMoves() {
		Position position = new Position( null );
		position.add( Side.WHITE, "a1", PieceType.KNIGHT );

		PositionAsserts.assertAllowedMoves(
			position, "a1", "b3", "c2" );
	}
}
