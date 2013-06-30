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
				position, "a1",
				"b2", "c3", "d4", "e5", "f6", "g7","h8" );
	}

	@Test
	public void fromB1() {
		Position position = new Position( null );
		position.add( Side.BLACK, "b1", PieceType.BISHOP );

		PositionAsserts.assertAllowedMoves(
				position, "b1",
				"a2", "c2", "d3", "e4", "f5", "g6","h7" );
	}

	@Test
	public void centralPoint() {
		Position position = new Position( null );
		position.add( Side.WHITE, "d4", PieceType.BISHOP );

		PositionAsserts.assertAllowedMoves(
				position, "d4",
				"e5", "f6", "g7", "h8", "c3", "b2","a1",
				"c5", "b6", "a7", "e3", "f2", "g1" );
	}

	@Test
	public void cannotMoveOverIntervening() {
		Position position = new Position( null );
		position.add( Side.WHITE, "a1", PieceType.BISHOP );

		position.add( Side.WHITE, "c3", PieceType.KNIGHT ); //any

		PositionAsserts.assertAllowedMoves(
				position, "a1",
				"b2" );
	}
}
