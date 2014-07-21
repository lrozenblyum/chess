package com.leokom.chess.engine;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Author: Leonid
 * Date-time: 04.07.13 22:34
 */
public class RookNewPositionTest {
	@Test
	public void moveRook() {
		Position position = new Position();
		position.addQueen( Side.WHITE, "a1" );
		position.add( Side.BLACK, "a8", PieceType.ROOK );

		final Position captured = position.move( "a8", "a1" );
		assertTrue( captured.hasPiece( Side.BLACK, "a1", PieceType.ROOK ) );
	}
}
