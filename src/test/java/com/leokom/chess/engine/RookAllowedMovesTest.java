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

	@Test
	public void e4() {
		Position position = new Position( null );
		position.add( Side.WHITE, "e4", PieceType.ROOK );

		PositionAsserts.assertAllowedMoves(
				position,
				"e4",
				"e1", "e2", "e3", "e5", "e6", "e7", "e8",
				"a4", "b4", "c4", "d4", "f4", "g4", "h4" );
	}

	@Test
	public void blockedPieces() {
		Position position = new Position( null );
		position.add( Side.WHITE, "a1", PieceType.ROOK );
		position.add( Side.WHITE, "a3", PieceType.BISHOP ); //any

		PositionAsserts.assertAllowedMoves(
				position,
				"a1",
				"a2",
				"b1", "c1", "d1", "e1", "f1", "g1", "h1" );
	}
}
