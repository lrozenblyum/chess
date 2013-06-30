package com.leokom.chess.engine;

import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 30.06.13 21:51
 */
public class RookAllowedMovesTest {
	@Test
	public void a1() {
		Position position = new Position( null );
		position.add( Side.BLACK, "a1", PieceType.ROOK );

		PositionAsserts.assertAllowedMoves(
			position,
			"a1",
			"a2", "a3", "a4", "a5", "a6", "a7", "a8",
			"b1", "c1", "d1", "e1", "f1", "g1", "h1" );
	}
}
