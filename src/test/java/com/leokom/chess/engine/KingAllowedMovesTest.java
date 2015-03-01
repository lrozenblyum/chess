package com.leokom.chess.engine;

import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 07.07.13 21:37
 */
public class KingAllowedMovesTest {
	@Test
	public void a1() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "a1", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position,
				"a1",
				"b1", "a2", "b2"
		);
	}

	@Test
	public void h8() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.BLACK, "h8", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position,
				"h8",
				"h7", "g7", "g8"
		);
	}

	@Test
	public void a8() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.BLACK, "a8", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position,
				"a8",
				"a7", "b7", "b8"
		);
	}

	@Test
	public void e4() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.BLACK, "e4", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position,
				"e4",
				"e3", "d3", "f3",
				"e5", "d5", "f5",
				"d4", "f4"
		);
	}

	@Test
	public void cannotMoveOnBlockedSquare() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "a1", PieceType.KING );

		position.add( Side.WHITE, "b1", PieceType.QUEEN );
		position.add( Side.WHITE, "a2", PieceType.KNIGHT );

		PositionAsserts.assertAllowedMoves(
				position,
				"a1",
				"b2"
		);
	}

	@Test
	public void canCapture() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "a1", PieceType.KING );

		position.add( Side.BLACK, "b1", PieceType.KNIGHT );
		position.add( Side.BLACK, "a2", PieceType.KNIGHT );

		PositionAsserts.assertAllowedMoves(
				position,
				"a1",
				"b2", "a2", "b1"
		);
	}
}
