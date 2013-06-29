package com.leokom.chess.engine;

import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 29.06.13 22:18
 */
public class BishopAllowedMovesTest {
	@Test
	public void simpleDiagonal() {
		Position position = new Position( null );
		position.add( Side.WHITE, "a1", PieceType.BISHOP );

		PositionAsserts.assertAllowedMoves(
				position, "a1", "b2", "c3", "d4", "e5", "f6", "g7","h8" );
	}
}
