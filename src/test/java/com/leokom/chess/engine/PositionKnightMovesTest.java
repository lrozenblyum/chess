package com.leokom.chess.engine;

import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 21.05.13 22:07
 */
public class PositionKnightMovesTest {
	@Test
	public void knightMovesSquare() {
		Position position = new Position( null );
		position.add( Side.WHITE, "a1", PieceType.KNIGHT );

		PositionAsserts.assertAllowedMoves(
			position, "a1", "b3", "c2" );
	}

	@Test
	public void knightMovesAnotherSquare() {
		Position position = new Position( null );
		position.add( Side.BLACK, "h1", PieceType.KNIGHT );

		PositionAsserts.assertAllowedMoves(
				position, "h1", "g3", "f2" );
	}

	@Test
	public void anotherRank() {
		Position position = new Position( null );
		position.add( Side.WHITE, "a8", PieceType.KNIGHT );

		PositionAsserts.assertAllowedMoves(
				position, "a8", "b6", "c7" );
	}

	@Test
	public void threeDestinations() {
		Position position = new Position( null );
		position.add( Side.BLACK, "b1", PieceType.KNIGHT );

		PositionAsserts.assertAllowedMoves(
				position, "b1", "a3", "c3", "d2" );
	}
}
