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

	@Test
	public void centerBlocksAndCaptures() {
		Position position = new Position( null );
		position.addQueen( Side.WHITE, "c4" );

		position.addQueen( Side.WHITE, "d4" );
		position.addQueen( Side.WHITE, "d5" );
		position.addQueen( Side.WHITE, "b5" );

		position.addQueen( Side.BLACK, "c5" );
		position.addQueen( Side.BLACK, "b5" );
		position.addQueen( Side.BLACK, "b4" );
		position.addQueen( Side.BLACK, "b3" );
		position.addQueen( Side.BLACK, "c3" );
		position.addQueen( Side.BLACK, "d3" );


		PositionAsserts.assertAllowedMoves(
				position, "c4",
				"c5", "b5", "b4", "b3", "c3", "d3" );
	}
}
